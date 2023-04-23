package osu.salat23.circler.bot.command

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.ArgumentSerialized
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import kotlin.reflect.full.isSubclassOf

@Component
class CommandCallGeneratorV1 : CommandCallGenerator {
    override fun generateCall(command: Any): String {
        val commandClass = command::class.java
        val commandMetadata: Command = commandClass.getAnnotation(Command::class.java)
            ?: throw IllegalStateException("Could not get command metadata in class: ${commandClass.javaClass.canonicalName}")

        var commandCall = ""
        commandCall += commandMetadata.identifiers.first()

        for (field in commandClass.declaredFields) {
            val argumentMetadata =
                field.getAnnotation(Argument::class.java)//field.getDeclaredAnnotation(Argument::class.java)
                    ?: throw IllegalStateException("Could not get field metadata: ${field.name} in class: ${commandClass.canonicalName}")

            var defaultValue: String? = null
            if (!argumentMetadata.required) {
                val defaultArgumentMetadata = field.getDeclaredAnnotation(Default::class.java)
                    ?: throw IllegalStateException("Argument: ${field.name} in command: ${commandClass.canonicalName} does not have default value. Make argument required or add @Default annotation.")
                defaultValue = defaultArgumentMetadata.value
            }

            // get the field value of the command instance
            field.isAccessible = true
            // field.trySetAccessible()
            val fieldValue = field.get(command)
            val argumentIdentifier = argumentMetadata.identifiers.first()
            val argumentValue = if (fieldValue is Enum<*>) {
                val enumClass = fieldValue::class.java
                val enumField = enumClass.getDeclaredField(fieldValue.name)
                val serializationMetadata = enumField.getDeclaredAnnotation(ArgumentSerialized::class.java)
                serializationMetadata.identifiers.first()
            } else fieldValue.toString()

            val argument = "-$argumentIdentifier $argumentValue"
            commandCall += " $argument"
        }
        return commandCall
    }
}