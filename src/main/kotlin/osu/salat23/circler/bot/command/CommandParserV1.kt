package osu.salat23.circler.bot.command

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.ArgumentSerialized
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.bot.command.impl.CommandClasspath
import java.lang.reflect.Field
import java.net.URLClassLoader
import java.nio.file.Paths
import java.util.*
import kotlin.reflect.full.isSubclassOf


@Component
class CommandParserV1 : CommandParser {

    /*
        In order for this parser to work, it has to be inside package where all annotated commands are located.
        It will scan its current package and find every configured command and its arguments.
    */

    private val identifierToCommandClass = mutableMapOf<String, Class<*>>()

    init {
        val paths = arrayOf(
            Paths.get(CommandClasspath::class.java.getResource("CommandClasspath.class")!!.toURI()).parent.toUri().toURL(),
        )
        val commandAnnotationClassLoader = URLClassLoader(paths, this.javaClass.classLoader)
        // this will make sure that classloader loads all command classes before creating our reflections instance


        val reflection = Reflections(
            ConfigurationBuilder()
                .setUrls(Collections.singletonList(ClasspathHelper.forClass(CommandClasspath::class.java)))
                .setScanners(Scanners.TypesAnnotated, Scanners.Resources, Scanners.SubTypes)
                .setUrls(ClasspathHelper.forClassLoader(commandAnnotationClassLoader)).addClassLoaders(commandAnnotationClassLoader)
        )

        val allAnnotatedClasses = reflection.getTypesAnnotatedWith(Command::class.java)
        for (commandClass in allAnnotatedClasses) {
            // todo rewrite for kotlin classes using Class<>.kotlin to convert
            val commandData: Command = commandClass.getAnnotation(Command::class.java)
                ?: throw IllegalStateException("Could not get command metadata in class: ${commandClass.javaClass.canonicalName}")

            val identifierToArgumentField = mutableMapOf<String, Field>()
            for (field in commandClass.declaredFields) {
                val argumentMetadata = field.getAnnotation(Argument::class.java)//field.getDeclaredAnnotation(Argument::class.java)
                    ?: throw IllegalStateException("Could not get field metadata: ${field.name} in class: ${commandClass.canonicalName}")
                argumentMetadata.identifiers.forEach {
                    if (identifierToArgumentField.contains(it))
                        throw IllegalStateException(
                            "Duplicate argument identifier found: $it in ${field.name} and ${identifierToArgumentField[it]!!.name}"
                        )
                    // todo add number argument
                    // todo make every check case insensitive
                    identifierToArgumentField[it] = field
                }
            }

            identifierToCommandClass.putAll(commandData.identifiers.associateWith { commandClass })
        }
    }


    // user salat23 -render -page 1
    override fun parse(input: String): Any {
        val argumentPrefix = "-"
        val splitRegex = "\\s(?=(?:[^\"]*\"[^\"]*\")*[^\"]*\$)".toRegex()
        // if amount of quotes is odd then throw exception
        //if (input.chars().filter { it.toChar() == '"' }.toList().size % 2 != 0)

        // split everything by white spaces and remove all quotes afterwards
        val tokens = input.split(splitRegex).map { it.replace("\"", "") }.toMutableList()

        if (tokens.isEmpty()) throw CommandParsingException(
            "Command is not present",
            CommandParsingErrorType.CommandNotPresent
        )
        val commandToken = tokens[0]
        if (!identifierToCommandClass.any { it.key.lowercase() == commandToken.lowercase() })
            throw CommandParsingException("Command not found: $commandToken", CommandParsingErrorType.CommandNotFound)
        val commandClass = identifierToCommandClass[commandToken]!!
        val commandMetadata = commandClass.getAnnotation(Command::class.java)
        val argumentIdentifierToField = mutableMapOf<String, Field>()
        val fieldToDefaultArgument = mutableMapOf<Field, String>()
        val argumentsMetadata = mutableListOf<Argument>()
        var implicitArgument: Pair<Field, Argument>? = null
        commandClass.declaredFields.forEach { field ->
            // continue if field has no Argument annotation
            val argumentMetadata = field.getDeclaredAnnotation(Argument::class.java) ?: return@forEach
            if (argumentMetadata.implicit) { // todo create checks while creating app about default values and what not
                implicitArgument = field to argumentMetadata
            }
            // add this to arguments and associate its identifiers with the field
            argumentsMetadata.add(argumentMetadata)
            argumentIdentifierToField.putAll(argumentMetadata.identifiers.map { Pair(it, field) })
            // assign a default value for this field in case it is not explicitly provided
            val defaultArgumentMetadata = field.getDeclaredAnnotation(Default::class.java)
            if (defaultArgumentMetadata != null) {
                fieldToDefaultArgument[field] = defaultArgumentMetadata.value
            }
        }

        // remove the command token afterwards
        tokens.removeFirst()
        // now we only have argument related tokens

        val instantiationArguments = mutableMapOf<String, Any>()
        while (tokens.isNotEmpty()) {
            var entriesToRemoveAmount = 0
            val currentToken = tokens.first()
            entriesToRemoveAmount++


            // check if argument name starts with '-'. throw exception if not
            if (!currentToken.startsWith(argumentPrefix) && implicitArgument == null)
                throw CommandParsingException(
                    "Unexpected argument name format: $currentToken",
                    CommandParsingErrorType.ArgumentWrongNameFormat
                )

            // this pizdec decides if we should use implicit argument name or do usual logic
            var addFakeToken = false
            val argumentName = if (
                    currentToken.first().toString() != argumentPrefix && implicitArgument != null
                ) {
                    addFakeToken = true
                    implicitArgument!!.second.identifiers.first() // return
                } else {
                    currentToken.substring(1) // return
                }

            if (implicitArgument != null && addFakeToken) {
                tokens.add(1, tokens.first())
            }

            val argumentMetadata = argumentsMetadata.firstOrNull {
                it.identifiers.any { identifier -> identifier.lowercase() == argumentName.lowercase() }
            }
                ?: throw CommandParsingException(
                    "Could not parse argument: $currentToken",
                    CommandParsingErrorType.NoSuchArgumentName
                )
            val argumentField = argumentIdentifierToField[argumentMetadata.identifiers[0]]
                ?: throw CommandParsingException(
                    "Could not obtain argument field: $currentToken",
                    CommandParsingErrorType.CouldNotObtainArgumentField
                )

            val argumentType = argumentField.type


            run {
                // in case this is the boolean argument
                // if boolean arguments are declared without value specifiers then we count them as truthful
                if (argumentType == Boolean::class.java) {
                    // in case this is the only argument left, we say that it is true
                    if (tokens.size - entriesToRemoveAmount == 0) {
                        instantiationArguments[argumentField.name] = true
                        return@run
                    }

                    // if the next token is another argument name - then we count our current boolean token as just declared
                    if (tokens[1].startsWith(argumentPrefix)) {
                        instantiationArguments[argumentField.name] = true
                        return@run
                    }

                    val booleanValue = tokens[1]
                    entriesToRemoveAmount++


                    try {
                        instantiationArguments[argumentField.name] = convertStringValueToArgumentValue(booleanValue, argumentType)
                    } catch (exception: IllegalStateException) {
                        throw CommandParsingException(
                            "Could not determine boolean value: $booleanValue for argument: $currentToken",
                            CommandParsingErrorType.ArgumentValueTypeIsWrong
                        )
                    }
                }


                if (argumentType == String::class.java) {
                    // if there is no tokens left, that means value is not provided
                    if (tokens.size - entriesToRemoveAmount < 1) {
                        throw CommandParsingException(
                            "Argument -$argumentName value is not provided!",
                            CommandParsingErrorType.ArgumentValueIsNotProvided
                        )
                    }

                    // even if the next token starts with -, we still count it as a value
                    // not the new argument name

                    val stringValue = tokens[1]
                    entriesToRemoveAmount++

                    instantiationArguments[argumentField.name] = convertStringValueToArgumentValue(stringValue, argumentType)
                }

                if (argumentType::class.isSubclassOf(Number::class)) {
                    // if there is no tokens left, that means value is not provided
                    if (tokens.size - entriesToRemoveAmount < 1) {
                        throw CommandParsingException(
                            "Argument -$argumentName value is not provided!",
                            CommandParsingErrorType.ArgumentValueIsNotProvided
                        )
                    }

                    // even if the next token starts with -, we still count it as a value
                    // not the new argument name

                    val numberValue = tokens[1]
                    entriesToRemoveAmount++



                }

                if (argumentType.isEnum) {
                    // if there is no tokens left, that means value is not provided
                    if (tokens.size - entriesToRemoveAmount < 1) {
                        throw CommandParsingException(
                            "Argument -$argumentName value is not provided!",
                            CommandParsingErrorType.ArgumentValueIsNotProvided
                        )
                    }

                    // even if the next token starts with -, we still count it as a value
                    // not the new argument name

                    val enumValue = tokens[1]
                    entriesToRemoveAmount++

                    instantiationArguments[argumentField.name] = convertStringValueToArgumentValue(enumValue, argumentType)
                }
            }

            // remove parsed tokens
            while (entriesToRemoveAmount > 0) {
                tokens.removeFirst()
                entriesToRemoveAmount--
            }
        }

        // prepare field in the right order for creating an instance
        val allArgumentsNeededPrepared = mutableListOf<Any>()
        for (field in commandClass.declaredFields) {
            val fieldType = field.type
            val providedArgument =
                instantiationArguments[field.name]
                    ?: convertStringValueToArgumentValue(fieldToDefaultArgument[field]
                        ?: throw CommandParsingException(
                            "Could not create command because default argument is missing: ${field.name}",
                            CommandParsingErrorType.CouldNotGetDefaultArgumentValue
                        ), fieldType)
            allArgumentsNeededPrepared.add(providedArgument)
        }

        if (allArgumentsNeededPrepared.size != commandClass.declaredFields.size)
        // if this thing pops up - it means that you either declared excess class
        // fields that are not annotated with @Argument or something went really wrong
            throw IllegalStateException("Could not prepare all fields for calling class instance constructor")

        val commandClassConstructor = commandClass.declaredConstructors[0]
        val instance = commandClassConstructor.newInstance(*allArgumentsNeededPrepared.toTypedArray())
        // instance containing all arguments is now created
        return instance
    }

    private val booleanTruthIdentifiers = listOf("true", "екгу", "t", "е")
    private val booleanFalseIdentifiers = listOf("false", "афдыу", "f", "а")
    private fun convertStringValueToArgumentValue(value: String, targetType: Class<*>): Any {
        if (targetType == Boolean::class.java) {
            if (booleanTruthIdentifiers.contains(value)) return true
            if (booleanFalseIdentifiers.contains(value)) return false
            throw IllegalStateException("Can't parse boolean value")
        }

        if (targetType == String::class.java) {
            return value
        }


        try {
            when (targetType) {
                Double::class.java -> return value.toDouble()
                Float::class.java -> return value.toFloat()
                Long::class.java -> return value.toLong()
                Int::class.java -> return value.toInt()
                Short::class.java -> return value.toShort()
                Byte::class.java -> return value.toByte()
            }
        } catch (exception: NumberFormatException) {
            throw CommandParsingException(
                "Can't parse number value: $value for type ${targetType.name}", CommandParsingErrorType.CantParseNumberValue
            )
        }

        if (targetType.isEnum) {
            val identifierToEnumConstantField = mutableMapOf<String, Field>()
            val enumConstantFieldToOrdinal = mutableMapOf<Field, Int>()
            val enumConstantFields = targetType.declaredFields.filter { field ->
                field.isEnumConstant && field.getDeclaredAnnotation(ArgumentSerialized::class.java) != null
            }

            var currentOrdinal = 0
            enumConstantFields.forEach { constantField ->
                val identifiers =
                    constantField.getDeclaredAnnotation(ArgumentSerialized::class.java)!!.identifiers

                identifierToEnumConstantField.putAll(identifiers.associateWith { constantField })

                enumConstantFieldToOrdinal[constantField] = currentOrdinal
                currentOrdinal++
            }



            if (!identifierToEnumConstantField.containsKey(value))
                throw CommandParsingException(
                    "No such value available: $value", CommandParsingErrorType.NoSuchValueInEnum
                )

            val selectedField = identifierToEnumConstantField[value]!!
            val ordinalIndex = enumConstantFieldToOrdinal[selectedField]!!
            val enumValue = targetType.enumConstants[ordinalIndex]!!

            return enumValue
        }

        throw IllegalStateException("Unsupported type")
    }
}