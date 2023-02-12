package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.BooleanArgument
import osu.salat23.circler.bot.command.arguments.StringArgument
import osu.salat23.circler.bot.command.arguments.factories.DoRenderArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.TemplateArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.SetChatTemplateCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class SetChatTemplateCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val templateArgumentFactory: TemplateArgumentFactory,
    private val doRenderArgumentFactory: DoRenderArgumentFactory
    ): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "setChatTemplate"
    override fun create(input: String): Command {
        val templateArgument = templateArgumentFactory.create(input, true).withDefault(StringArgument(""))
        val isHtmlArgument = doRenderArgumentFactory.create(input).withDefault(BooleanArgument(false))

        return SetChatTemplateCommand(
            templateArgument = templateArgument,
            isHtmlArgument = isHtmlArgument
        )
    }
}