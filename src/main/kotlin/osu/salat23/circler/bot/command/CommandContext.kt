package osu.salat23.circler.bot.command

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.bot.command.impl.CommandClasspath
import osu.salat23.circler.bot.command.keymap.KeymapTranslator
import osu.salat23.circler.bot.command.keymap.LayoutKeymap
import java.lang.reflect.Field
import java.util.*
import kotlin.reflect.KClass

@Component
class CommandContext(
    private val keymaps: List<LayoutKeymap>
) {
    data class CommandInfo(
        val metadata: Command,
        val primaryIdentifier: String,
        val `class`: KClass<*>,
        val arguments: List<ArgumentInfo>,
        val identifierToArgument: Map<String, ArgumentInfo>,
        val implicitArgument: ArgumentInfo?
    )

    data class ArgumentInfo(
        val metadata: Argument,
        val primaryIdentifier: String,
        val defaultValue: String?,
        val field: Field
    )

    final val identifierToCommandInfo: Map<String, CommandInfo>
    final val commands: List<CommandInfo>

    init {
        val identifierToCommandInfo = mutableMapOf<String, CommandInfo>()
        val commands = mutableSetOf<CommandInfo>()

        val reflection = Reflections(
            ConfigurationBuilder()
                .setUrls(Collections.singletonList(ClasspathHelper.forClass(CommandClasspath::class.java)))
                .setScanners(Scanners.TypesAnnotated, Scanners.Resources, Scanners.SubTypes)
        )

        val allAnnotatedClasses = reflection.getTypesAnnotatedWith(Command::class.java)
        for (commandClass in allAnnotatedClasses) {
            // todo rewrite for kotlin classes using Class<>.kotlin to convert
            val commandMetadata: Command = commandClass.getAnnotation(Command::class.java)
                ?: throw IllegalStateException("Could not get command metadata in class: ${commandClass.javaClass.canonicalName}")

            val identifierToArgumentInfo = mutableMapOf<String, ArgumentInfo>()
            val arguments = mutableSetOf<ArgumentInfo>()
            var hasImplicitArgument = false
            var implicitArgument: ArgumentInfo? = null
            for (field in commandClass.declaredFields) {
                val argumentMetadata =
                    field.getAnnotation(Argument::class.java)//field.getDeclaredAnnotation(Argument::class.java)
                        ?: throw IllegalStateException("Could not get field metadata: ${field.name} in class: ${commandClass.canonicalName}")

                // command can contain only 1 implicit argument. this rule is being checked here
                if (argumentMetadata.implicit) {
                    // this check uses boolean variable instead of checking implicitArgument for null.
                    // probably should change to avoid code duplication
                    if (hasImplicitArgument) throw IllegalStateException("Command ${commandClass.canonicalName} contains several implicit arguments.")
                    hasImplicitArgument = true
                }

                // command must have default value if it is required. this rule is being checked here
                var defaultValue: String? = null
                if (!argumentMetadata.required) {
                    val defaultArgumentMetadata = field.getDeclaredAnnotation(Default::class.java)
                        ?: throw IllegalStateException("Argument: ${field.name} in command: ${commandClass.canonicalName} does not have default value. Make argument required or add @Default annotation.")
                    defaultValue = defaultArgumentMetadata.value
                }

                // first identifier specified is the primary one
                val primaryIdentifier = argumentMetadata.identifiers[0].lowercase()
                val info = ArgumentInfo(
                    metadata = argumentMetadata,
                    primaryIdentifier = primaryIdentifier,
                    defaultValue = defaultValue,
                    field = field
                )
                if (argumentMetadata.implicit) implicitArgument = info

                val assignIdentifiers = { identifier: String ->
                    // every key in map will be lowercase'd for case-insensitive checks
                    if (identifierToArgumentInfo.containsKey(identifier.lowercase()) && identifierToArgumentInfo[identifier.lowercase()]!! != info)
                        throw IllegalStateException("Duplicate argument identifier found: $identifier in ${field.name} and ${identifierToArgumentInfo[identifier]!!.field.name}")

                    if (identifier.isEmpty()) throw IllegalStateException("Identifier cannot be empty. Argument ${field.name} in ${commandClass.canonicalName}")
                    identifierToArgumentInfo[identifier.lowercase()] = info
                    arguments += info
                }
                // english identifiers
                argumentMetadata.identifiers.forEach(assignIdentifiers)
                // other language layouts
                argumentMetadata.identifiers.forEach { identifier ->
                    KeymapTranslator.translate(identifier, keymaps).forEach(assignIdentifiers)
                }
            }

            val primaryIdentifier = commandMetadata.identifiers[0].lowercase()
            val info = CommandInfo(
                metadata = commandMetadata,
                primaryIdentifier = primaryIdentifier,
                `class` = commandClass.kotlin,
                arguments = arguments.toList(),
                identifierToArgument = identifierToArgumentInfo,
                implicitArgument = implicitArgument
            )

            val assignIdentifiers = { identifier: String ->
                // every key in map will be lowercase'd for case-insensitive checks
                if (identifierToCommandInfo.contains(identifier.lowercase()) && identifierToCommandInfo[identifier.lowercase()]!! != info)
                    throw IllegalStateException("Duplicate command identifier found: $identifier in ${commandClass.canonicalName} and ${identifierToCommandInfo[identifier]!!.`class`.java.canonicalName}")

                if (identifier.isEmpty()) throw IllegalStateException("Identifier cannot be empty. Command ${commandClass.canonicalName}")

                identifierToCommandInfo[identifier.lowercase()] = info
                commands += info
            }

            // english identifiers
            commandMetadata.identifiers.forEach(assignIdentifiers)
            // other language layouts
            commandMetadata.identifiers.forEach { identifier ->
                KeymapTranslator.translate(identifier, keymaps).forEach(assignIdentifiers)
            }
        }

        this.identifierToCommandInfo = identifierToCommandInfo
        this.commands = commands.toList()
    }
}