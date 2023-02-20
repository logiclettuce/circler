package osu.salat23.circler.bot.vk

import api.longpoll.bots.LongPollBot
import api.longpoll.bots.exceptions.VkApiException
import api.longpoll.bots.exceptions.VkApiHttpException
import api.longpoll.bots.exceptions.VkApiResponseException
import api.longpoll.bots.model.events.Update
import api.longpoll.bots.model.events.messages.MessageNew
import api.longpoll.bots.model.objects.additional.Keyboard
import api.longpoll.bots.model.objects.additional.buttons.Button
import api.longpoll.bots.model.objects.additional.buttons.TextButton
import api.longpoll.bots.model.objects.media.Attachment
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.bot.client.*
import osu.salat23.circler.bot.command.CommandParser
import osu.salat23.circler.bot.command.CommandParsingErrorType
import osu.salat23.circler.bot.command.CommandParsingException
import osu.salat23.circler.bot.command.exceptions.NotABotCommandException
import osu.salat23.circler.osu.OsuCommandHandler
import osu.salat23.circler.properties.VkProperties
import java.io.InputStream
import java.net.URL


@Component
class Vk(
    val vkProperties: VkProperties,
    val osuCommandHandler: OsuCommandHandler,
    val commandParser: CommandParser
) : LongPollBot(), Client, ApplicationRunner {

    private val logger: Logger = LoggerFactory.getLogger(Vk::class.java)

    override fun handle(updates: MutableList<Update>?) {

        updates!!.forEach { update ->
            lateinit var text: String
            lateinit var chatId: String
            lateinit var userId: String
            var attachedFile: InputStream? = null
            var isUserAdmin: Boolean = false
            when (update.type) {
                Update.Type.MESSAGE_NEW -> {
                    val messageNew = update.`object` as MessageNew
                    text = VkTextParsingTools.tryRemovePing(messageNew.message.text)
                    if (messageNew.message.payload != null && messageNew.message.payload.asJsonObject.has("value")) {
                        text = messageNew.message.payload.asJsonObject.get("value").asString
                    }
                    for (attachment in messageNew.message.attachments) {
                        if (attachment.type == Attachment.Type.LINK) {
                            text += " ${attachment.link.url}"
                        }
                    }
                    chatId = messageNew.message.peerId.toString()
                    userId = messageNew.message.fromId.toString()
                    try {
                        val members = vk.messages.conversationMembers
                            .setPeerId(chatId.toInt())
                            .execute().response
                        var isAdmin = false
                        for (member in members.items) {
                            if (member.memberId == userId.toInt() && member.isAdmin) isAdmin = true
                        }
                        isUserAdmin = isAdmin
                    } catch (vkException: VkApiException) {
                        // todo maybe do something here?
                    }


                    if (messageNew.message.hasAttachments()) {
                        for (attachment in messageNew.message.attachments) {
                            if (attachment.type == Attachment.Type.DOC) {
                                attachedFile = URL(attachment.doc.url).openStream()
                            }
                        }
                    }
                }

                else -> return
            }
            val command: Any
            try {
                command = commandParser.parse(text)
            } catch (exception: CommandParsingException) {
                if (exception.type == CommandParsingErrorType.CommandNotFound
                    || exception.type == CommandParsingErrorType.CommandNotPresent) return
                exception.printStackTrace()
                return
            }
            try {
                // todo check if bot has previleges for chat admin (right now it just dies =( if there are no rights  )
                // todo context creation seems kinda primitive. think about other ways of doing this bit
                osuCommandHandler.handle(
                    command,
                    this,
                    ClientBotContext(
                        chatId = chatId,
                        userId = userId,
                        clientType = ClientType.VK,
                        isUserAdmin = isUserAdmin,
                        fileAttachment = attachedFile ?: InputStream.nullInputStream()
                    )
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    override fun getAccessToken(): String = vkProperties.accessToken

    override fun run(args: ApplicationArguments?) {
        while (true) {
            try {
                this.startPolling()
            } catch (exception: VkApiHttpException) {
                runBlocking { delay(5000) } // wait 5 seconds before trying to initiate connection again
                continue
            }
        }
    }

    override fun send(clientEntity: ClientEntity) {
        try {

            when (clientEntity) {
                is ClientImage -> {
                    val sendImage = vk.messages.send()
                        .setPeerId(clientEntity.chatId.toInt())
                        .setMessage(clientEntity.text)
                        .addPhoto(clientEntity.image, "preview.png")

                    val keyboard = createKeyboard(clientEntity.furtherActions)
                    if (keyboard != null)
                        sendImage.setKeyboard(
                            keyboard
                        )

                    sendImage.execute()
                }

                is ClientMessage -> {
                    val sendMessage = vk.messages.send()
                        .setPeerId(clientEntity.chatId.toInt())
                        .setMessage(clientEntity.text)

                    val keyboard = createKeyboard(clientEntity.furtherActions)
                    if (keyboard != null)
                        sendMessage.setKeyboard(
                            keyboard
                        )

                    sendMessage.execute()
                }
            }
        } catch (exception: VkApiResponseException) {
            exception.printStackTrace()
        }
    }

    private fun createKeyboard(furtherActions: List<FurtherAction>): Keyboard? {
        if (furtherActions.isEmpty()) return null
        val buttons = mutableListOf<Button>()
        furtherActions.forEach {
            val payload = JsonObject()
            payload.addProperty("value", it.value)
            buttons.add(
                TextButton(
                    Button.Color.POSITIVE, TextButton.Action(
                        it.displayName,
                        payload
                    )
                )
            )
        }// this is basically list of lists, ok? be careful
        return Keyboard(listOf(buttons)).setInline(true)
    }
}