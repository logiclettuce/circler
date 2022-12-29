package osu.salat23.circler.bot.vk

import api.longpoll.bots.LongPollBot
import api.longpoll.bots.exceptions.VkApiResponseException
import api.longpoll.bots.model.events.Update
import api.longpoll.bots.model.events.messages.MessageNew
import api.longpoll.bots.model.objects.media.Attachment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.bot.commands.NotABotCommandException
import osu.salat23.circler.osu.OsuCommandHandler
import osu.salat23.circler.properties.VkProperties

@Component
class Vk(
    val vkProperties: VkProperties,
    val osuCommandHandler: OsuCommandHandler,
) : LongPollBot(), Client, ApplicationRunner {

    private val logger: Logger = LoggerFactory.getLogger(Vk::class.java)

    override fun handle(updates: MutableList<Update>?) {
        updates!!.forEach { update ->
            lateinit var text: String
            lateinit var chatId: String
            lateinit var userId: String
            when (update.type) {
                Update.Type.MESSAGE_NEW -> {
                    val messageNew = update.`object` as MessageNew
                    logger.info(messageNew.toString())
                    text = VkTextParsingTools.tryRemovePing(messageNew.message.text)
                    for (attachment in messageNew.message.attachments) {
                        if (attachment.type == Attachment.Type.LINK) {
                            text += " ${attachment.link.url}"
                        }
                    }
                    chatId = messageNew.message.peerId.toString()
                    userId = messageNew.message.fromId.toString()
                }

                else -> return
            }
            val command: Command
            try {
                command = Command.Builder().from(text).build()
            } catch (exception: NotABotCommandException) {
                return
            }
            osuCommandHandler.handle(command, this, UserContext(chatId, userId, ClientType.VK))
        }
    }

    override fun getAccessToken(): String = vkProperties.accessToken

    override fun run(args: ApplicationArguments?) {
        this.startPolling()
    }

    override fun send(clientEntity: ClientEntity) {
        try {
            val sendMessage = vk.messages.send()
                .setPeerId(clientEntity.chatId!!.toInt())
                .setMessage(clientEntity.text)
            sendMessage.execute()
        } catch (exception: VkApiResponseException) {
            exception.printStackTrace()
        }
    }
}