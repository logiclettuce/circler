package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.SetChatTemplateCommand
import osu.salat23.circler.service.ChatService
import java.io.InputStream

@Component
class SetChatTemplateHandler(
    val chatService: ChatService
): ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val command = command as SetChatTemplateCommand

        if (!command.templateArgument.isPresent()) {
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = "No template type argument provided!"
                )
            )
        }
        val templateType = command.templateArgument.getArgument().value


        if (userContext.fileAttachment == InputStream.nullInputStream()) {
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = "No template file provided!"
                )
            )
        }
        //todo constants and maybe logic
        when (templateType) {
            "profile" -> chatService.setUserProfileTemplate(
                userContext.chatId,
                userContext.clientType,
                userContext.fileAttachment
            )
            else -> {
                client.send(
                    ClientMessage(
                        userId = userContext.userId,
                        chatId = userContext.chatId,
                        text = "No template type found: $templateType"
                    )
                )
                return
            }
        }

        client.send(
            ClientMessage(
                userId = userContext.userId,
                chatId = userContext.chatId,
                text = "Successfully changed $templateType"
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command is SetChatTemplateCommand
    }
}