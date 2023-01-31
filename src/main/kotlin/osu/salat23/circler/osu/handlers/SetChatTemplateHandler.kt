package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.SetChatTemplateCommand
import osu.salat23.circler.bot.response.templates.ResponseTemplates
import osu.salat23.circler.service.ChatService
import java.io.InputStream

@Component
class SetChatTemplateHandler(
    val chatService: ChatService
): ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, clientBotContext: ClientBotContext) {
        val command = command as SetChatTemplateCommand

        if (!command.templateArgument.isPresent()) {
            client.send(
                ClientMessage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    text = "No template type argument provided!"
                )
            )
        }
        val templateType = command.templateArgument.getArgument().value
        val isHtml = command.isHtmlArgument.getArgument().value


        if (clientBotContext.fileAttachment == InputStream.nullInputStream()) {
            client.send(
                ClientMessage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    text = "No template file provided!"
                )
            )
        }
        //todo constants and maybe logic
        when (templateType) {
            "profile" -> chatService.setChatTemplate(
                ResponseTemplates.Profile,
                clientBotContext.chatId,
                clientBotContext.clientType,
                clientBotContext.fileAttachment,
                isHtml
            )
            else -> {
                client.send(
                    ClientMessage(
                        userId = clientBotContext.userId,
                        chatId = clientBotContext.chatId,
                        text = "No template type found: $templateType"
                    )
                )
                return
            }
        }

        client.send(
            ClientMessage(
                userId = clientBotContext.userId,
                chatId = clientBotContext.chatId,
                text = "Successfully changed $templateType"
            )
        )
    }

    override fun canHandle(command: Command, clientBotContext: ClientBotContext): Boolean {
        return command is SetChatTemplateCommand
    }
}