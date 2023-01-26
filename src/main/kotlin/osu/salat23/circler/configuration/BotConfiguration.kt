package osu.salat23.circler.configuration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.Command
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.configuration.domain.json.CommandConfigurationJSON
import osu.salat23.circler.configuration.exceptions.InvalidArgumentKeyException
import java.io.BufferedReader

@Configuration
class BotConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun commandConfiguration(): CommandConfiguration {
        val mapper = jacksonObjectMapper()
        val classLoader = javaClass.classLoader
        val bufferedReader: BufferedReader =
            classLoader.getResourceAsStream("commands/configuration.json")?.bufferedReader()
                ?: throw IllegalStateException("Could not find command configuration file!")

        val configJson = mapper.readValue(bufferedReader.readText(), CommandConfigurationJSON::class.java)
        val convertedCommands = mutableMapOf<String, Command>()
        val convertedArguments = mutableMapOf<String, Argument>()

        val allArgumentIdentifiers = mutableSetOf<String>()
        val allCommandIdentifiers = mutableSetOf<String>()
        configJson.arguments.forEach { entry ->
            val argumentKey = entry.key
            val argumentName = entry.value.name
            val argumentDescription = entry.value.description
            val argumentIdentifiers = entry.value.identifiers

            argumentIdentifiers.forEach {
                if (allArgumentIdentifiers.contains(it)) throw IllegalStateException("Configuration file contains duplicate argument identifiers - $it ($argumentKey)")
                if (it.contains(' ')) throw IllegalStateException("Argument identifier cannot contain whitespace - $it ($argumentKey)")
            }
            allArgumentIdentifiers.addAll(argumentIdentifiers)

            val argument = Argument(
                name = argumentName,
                description = argumentDescription,
                identifiers = argumentIdentifiers
            )
            if (convertedArguments.containsKey(argumentKey)) throw IllegalStateException("Configuration file contains duplicate argument key - $argumentKey")
            convertedArguments[argumentKey] = argument
        }

        configJson.commands.forEach { entry ->
            val commandKey = entry.key
            val commandName = entry.value.name
            val commandDescription = entry.value.description
            val commandIdentifiers = entry.value.identifiers

            val commandArguments = mutableListOf<Argument>()
            val argumentsToGet = entry.value.availableArguments
            argumentsToGet.forEach { argumentKey ->
                if (!convertedArguments.containsKey(argumentKey)) throw InvalidArgumentKeyException(argumentKey)
                commandArguments.add(convertedArguments[argumentKey]!!)
            }

            commandIdentifiers.forEach {
                if (allCommandIdentifiers.contains(it)) throw IllegalStateException("Configuration file contains duplicate command identifiers - $it ($commandKey)")
                if (it.contains(' ')) throw IllegalStateException("Command identifier cannot contain whitespace - $it ($commandKey)")
            }
            allCommandIdentifiers.addAll(commandIdentifiers)

            val command = Command(
                name = commandName,
                description = commandDescription,
                identifiers = commandIdentifiers,
                arguments = commandArguments
            )

            if (convertedArguments.containsKey(commandKey)) throw IllegalStateException("Configuration file contains duplicate command key - $commandKey")
            convertedCommands[commandKey] = command
        }

        return CommandConfiguration(
            arguments = convertedArguments,
            commands = convertedCommands
        )
    }

}