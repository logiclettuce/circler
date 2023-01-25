package osu.salat23.circler.bot.telegram

import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.client.ClientImage
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.CommandParser
import osu.salat23.circler.bot.command.exceptions.NotABotCommandException
import osu.salat23.circler.osu.OsuCommandHandler
import osu.salat23.circler.properties.TelegramProperties
import java.io.InputStream

@Component
class Telegram(
    private val telegramProperties: TelegramProperties,
    val osuCommandHandler: OsuCommandHandler,
    val commandParser: CommandParser
) : TelegramLongPollingBot(DefaultBotOptions()), Client {

    override fun getBotToken(): String = telegramProperties.token

    override fun getBotUsername(): String = "Circler - osu! bot"

    override fun onUpdateReceived(update: Update?) {
        lateinit var text: String
        lateinit var chatId: String
        lateinit var userId: String
        when {
            update != null && update.message != null -> {
                text = TelegramTextParsingTools.removePing(update.message.text, telegramProperties.botUserName)
                chatId = update.message.chatId.toString()
                userId = update.message.from.id.toString()
            }
//            update != null && update.inlineQuery != null -> {
//                text = TelegramTextParsingTools.removePing(update.inlineQuery.query, telegramProperties.botUserName)
//                chatId = update.inlineQuery.id
//            }
            else -> return
        }
        val command: Command
        try { // todo normal input stream handling
            command = commandParser.parse(text)
        } catch (exception: NotABotCommandException) {
            return
        }
        // todo IMPORTANT!!!! make isAdmin there (right now always false)
        osuCommandHandler.handle(command, this, UserContext(chatId, userId, ClientType.TELEGRAM, false, InputStream.nullInputStream()))
    }

    override fun send(clientEntity: ClientEntity) {
        try {
            when (clientEntity) {
                is ClientImage -> {
                    val sendPhoto = SendPhoto.builder()
                        .chatId(clientEntity.chatId)
                        .photo(InputFile(clientEntity.image, "preview.png"))
                        .build()
                    execute(sendPhoto)
                }

                is ClientMessage -> {
                    val sendMessage = SendMessage.builder()
                        .chatId(clientEntity.chatId)
                        .text(clientEntity.text)
                        .build()
                    execute(sendMessage)
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}