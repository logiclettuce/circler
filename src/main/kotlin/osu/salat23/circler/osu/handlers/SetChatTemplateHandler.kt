package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.impl.SetChatTemplateCommand
import osu.salat23.circler.bot.response.templates.TemplateFormat
import osu.salat23.circler.bot.response.templates.TemplateType
import osu.salat23.circler.service.ChatService
import java.io.InputStream

@Component
class SetChatTemplateHandler(
    val chatService: ChatService
): ChainHandler() {
    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val command = command as SetChatTemplateCommand

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
        val chat = chatService.getOrCreateChat(clientBotContext.chatId, clientBotContext.clientType)

        when (command.template) {
            "profile" -> chatService.setChatTemplate(
                chat = chat,
                type = TemplateType.Profile,
                format = if (command.forRender) TemplateFormat.Html else TemplateFormat.Text,
                templateFile = clientBotContext.fileAttachment
            )
            else -> {
                client.send(
                    ClientMessage(
                        userId = clientBotContext.userId,
                        chatId = clientBotContext.chatId,
                        text = "No template type found: ${command.template}"
                    )
                )
                return
            }
        }

        client.send(
            ClientMessage(
                userId = clientBotContext.userId,
                chatId = clientBotContext.chatId,
                text = "Successfully changed ${command.template}"
            )
        )
    }

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return command is SetChatTemplateCommand
    }
}