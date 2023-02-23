package osu.salat23.circler.bot.command

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.annotations.ArgumentSerialized
import osu.salat23.circler.bot.command.keymap.KeymapTranslator
import osu.salat23.circler.bot.command.keymap.LayoutKeymap
import kotlin.reflect.full.isSubclassOf


@Component
class CommandParserV1(
    private final val commandContext: CommandContext,
    private final val keymaps: List<LayoutKeymap>
) : CommandParser {

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
        if (!commandContext.identifierToCommandInfo.any { it.key.lowercase() == commandToken.lowercase() })
            throw CommandParsingException("Command not found: $commandToken", CommandParsingErrorType.CommandNotFound)
        val commandInfo = commandContext.identifierToCommandInfo[commandToken.lowercase()]!!
        val commandClass = commandInfo.`class`.java
        val commandMetadata = commandInfo.metadata

//        var implicitArgument: Pair<Field, Argument>? = null
//        commandClass.declaredFields.forEach { field ->
//            // continue if field has no Argument annotation
//            val argumentMetadata = field.getDeclaredAnnotation(Argument::class.java) ?: return@forEach
//            if (argumentMetadata.implicit) { // todo create checks while creating app about default values and what not
//                implicitArgument = field to argumentMetadata
//            }
//            // add this to arguments and associate its identifiers with the field
//            argumentsMetadata.add(argumentMetadata)
//            argumentIdentifierToField.putAll(argumentMetadata.identifiers.map { Pair(it, field) })
//            // assign a default value for this field in case it is not explicitly provided
//            val defaultArgumentMetadata = field.getDeclaredAnnotation(Default::class.java)
//            if (defaultArgumentMetadata != null) {
//                fieldToDefaultArgument[field] = defaultArgumentMetadata.value
//            }
//        }

        // remove the command token afterwards
        tokens.removeFirst()
        // now we only have argument related tokens

        val instantiationArguments = mutableMapOf<String, Any>()
        while (tokens.isNotEmpty()) {
            var entriesToRemoveAmount = 0
            val currentToken = tokens.first()
            entriesToRemoveAmount++


            // check if argument name starts with '-'. throw exception if not
            if (!currentToken.lowercase().startsWith(argumentPrefix.lowercase()) && commandInfo.implicitArgument == null)
                throw CommandParsingException(
                    "Unexpected argument name format: $currentToken",
                    CommandParsingErrorType.ArgumentWrongNameFormat
                )

            // this pizdec decides if we should use implicit argument name or do usual logic
            // ================================
            var addFakeToken = false
            val argumentIdentifier = if (
                    currentToken.first().toString() != argumentPrefix && commandInfo.implicitArgument != null
                ) { // if argument is actually implicit
                    addFakeToken = true
                    commandInfo.implicitArgument.primaryIdentifier // return
                } else {
                    // remove prefix which is always 1 character long
                    currentToken.substring(1) // return
                }

            if (commandInfo.implicitArgument != null && addFakeToken) {
                tokens.add(1, tokens.first())
            }
            // =================

            val argumentInfo = commandInfo.identifierToArgument[argumentIdentifier]
            ?: throw CommandParsingException(
                "Could not parse argument: $currentToken",
                CommandParsingErrorType.NoSuchArgumentName
            )
            val argumentMetadata = argumentInfo.metadata
            val argumentField = argumentInfo.field
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
                            "Argument -$argumentIdentifier value is not provided!",
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
                            "Argument -$argumentIdentifier value is not provided!",
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
                            "Argument -$argumentIdentifier value is not provided!",
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
        for (argument in commandInfo.arguments) {
            val fieldType = argument.field.type
            val providedArgument =
                instantiationArguments[argument.field.name]
                    ?: convertStringValueToArgumentValue(argument.defaultValue
                        ?: throw CommandParsingException(
                            "Could not create command because required argument is missing: ${argument.metadata.name}",
                            CommandParsingErrorType.RequiredArgumentNotProvided
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

    private val booleanTruthIdentifiers = KeymapTranslator.translate(listOf("true", "t"), keymaps, true)
    private val booleanFalseIdentifiers = KeymapTranslator.translate(listOf("false", "f"), keymaps, true)
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
            val identifierToEnumOrdinalIndex = ArgumentSerialized.getEnumIdentifierToOrdinalMap(targetType, keymaps)

            if (!identifierToEnumOrdinalIndex.containsKey(value))
                throw CommandParsingException(
                    "No such value available: $value", CommandParsingErrorType.NoSuchValueInEnum
                )

            val ordinalIndex = identifierToEnumOrdinalIndex[value]!!
            val enumValue = targetType.enumConstants[ordinalIndex]!!

            return enumValue
        }

        throw IllegalStateException("Unsupported type")
    }
}