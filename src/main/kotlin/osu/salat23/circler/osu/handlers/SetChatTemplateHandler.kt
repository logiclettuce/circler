package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.service.ChatService
import java.io.InputStream

@Component
class SetChatTemplateHandler(
    val chatService: ChatService
): ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {

        //todo constants and maybe logic
        when (command.options.templateType) {
            "profile" -> chatService.setUserProfileTemplate(
                userContext.chatId,
                userContext.clientType,
                command.options.attachedFile ?: InputStream.nullInputStream())
            else -> {
                client.send(
                    ClientMessage(
                        userId = userContext.userId,
                        chatId = userContext.chatId,
                        text = "No template type found: ${command.options.templateType}"
                    )
                )
                return
            }
        }

        client.send(
            ClientMessage(
                userId = userContext.userId,
                chatId = userContext.chatId,
                text = "Successfully changed ${command.options.templateType}"
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.SET_CHAT_TEMPLATE &&
                userContext.isUserAdmin
    }
}