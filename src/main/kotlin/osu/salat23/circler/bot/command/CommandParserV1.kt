package osu.salat23.circler.bot.command

import org.reflections.Reflections
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.ArgumentSerialized
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import java.lang.reflect.Field
import kotlin.streams.toList

@Component
class CommandParserV1 : CommandParser {

    private val identifierToCommandClass = mutableMapOf<String, Class<*>>()

    init {
        val reflection = Reflections(this::class.java.`package`)
        val allAnnotatedClasses = reflection.getTypesAnnotatedWith(Command::class.java)
        // todo check if some command identifiers collide with each other
        // todo add limited argument type (for developer)
        for (commandClass in allAnnotatedClasses) {
            val commandData: Command = commandClass.getAnnotation(Command::class.java)
                ?: throw IllegalStateException("Could not get command metadata in class: ${commandClass.javaClass.canonicalName}")

            val identifierToArgumentField = mutableMapOf<String, Field>()
            for (field in commandClass.declaredFields) {
                val argumentMetadata = field.getDeclaredAnnotation(Argument::class.java)
                    ?: throw IllegalStateException("Could not read field metadata: ${field.name} in class: ${commandClass.canonicalName}")
                argumentMetadata.identifiers.forEach {
                    if (identifierToArgumentField.contains(it))
                        throw IllegalStateException(
                            "Duplicate argument identifier found: $it in ${field.name} and ${identifierToArgumentField[it]!!.name}"
                        )
                    // todo type check for argument (only number, string, boolean and enum)
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
        if (input.chars().filter { it.toChar() == '"' }.toList().size % 2 != 0)
            throw CommandParsingException("Odd amount of quotes", CommandParsingErrorType.OddAmountOfQuotes)
        // split everything by white spaces and remove all quotes afterwards
        val tokens = input.split(splitRegex).map { it.replace("\"", "") }.toMutableList()

        if (tokens.isEmpty()) throw CommandParsingException(
            "Command is not present",
            CommandParsingErrorType.CommandNotPresent
        )
        val commandToken = tokens[0]
        if (!identifierToCommandClass.containsKey(commandToken))
            throw CommandParsingException("Command not found: $commandToken", CommandParsingErrorType.CommandNotFound)
        val commandClass = identifierToCommandClass[commandToken]!!
        val commandMetadata = commandClass.getAnnotation(Command::class.java)
        val argumentIdentifierToField = mutableMapOf<String, Field>()
        val fieldToDefaultArgument = mutableMapOf<Field, String>()
        val argumentsMetadata = mutableListOf<Argument>()
        commandClass.fields.forEach { field ->
            // continue if field has no Argument annotation
            val argumentMetadata = field.getDeclaredAnnotation(Argument::class.java) ?: return@forEach
            // add this to arguments and associate its identifiers with the field
            argumentsMetadata.add(argumentMetadata)
            argumentIdentifierToField.putAll(argumentMetadata.identifiers.map { Pair(it, field) })
            // assign a default value for this field in case it is not explicitly provided
            val defaultMetadata = field.getDeclaredAnnotation(Default::class.java)
            if (defaultMetadata != null) {
                fieldToDefaultArgument[field] = defaultMetadata.value
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
            if (!currentToken.startsWith(argumentPrefix))
                throw CommandParsingException(
                    "Unexpected argument name format: $currentToken",
                    CommandParsingErrorType.ArgumentWrongNameFormat
                )

            val argumentName = currentToken.substring(1)
            val argumentMetadata = argumentsMetadata.firstOrNull { it.identifiers.contains(argumentName) }
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
            throw IllegalStateException("Cant parse boolean value")
        }

        if (targetType == String::class.java) {
            return value
        }

        if (targetType.isEnum) {
            val stringValue = value
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



            if (!identifierToEnumConstantField.containsKey(stringValue))
                throw CommandParsingException(
                    "No such value available: $stringValue", CommandParsingErrorType.NoSuchValueInEnum
                )

            val selectedField = identifierToEnumConstantField[stringValue]!!
            val ordinalIndex = enumConstantFieldToOrdinal[selectedField]!!
            val enumValue = targetType.enumConstants[ordinalIndex]!!

            return enumValue
        }

        throw IllegalStateException("Unsupported type")
    }
}