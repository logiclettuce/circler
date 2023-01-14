package osu.salat23.circler.renderer

import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.utility.Time
import java.io.BufferedReader
import java.lang.IllegalStateException
import java.time.format.DateTimeFormatter

class ProfileRenderTemplate(val user: User, val command: Command) : HtmlRenderTemplate {

    private final val width = 760
    private final val height = 1000

    override fun getHtml(): String {


        val classLoader = javaClass.classLoader
        val bufferedReader: BufferedReader = classLoader.getResourceAsStream("response/profile.html")?.bufferedReader() ?: throw IllegalStateException("Could not find 'profile' template")
        var inputString = bufferedReader.use { it.readText() }
        inputString = inputString.replace("|width|", width.toString())
        inputString = inputString.replace("|height|", height.toString())
        inputString = inputString.replace("|background_src|", user.coverUrl)
        inputString = inputString.replace("|avatar_src|", user.avatarUrl)
        inputString = inputString.replace("|server|", command.server.displayName)
        inputString = inputString.replace("|mode|", user.playMode.alternativeName)
        inputString = inputString.replace("|playcount|", user.playCount.toString())
        inputString = inputString.replace("|playcount|", user.playCount.toString())


        inputString = inputString.replace("|pp|", user.performance.toString())
        inputString = inputString.replace("|acc|", user.accuracy.toString())
        inputString = inputString.replace("|playcount|", user.playCount.toString())
        inputString = inputString.replace("|max_combo|", user.maximumCombo.toString())
        inputString = inputString.replace("|playtime|", Time.fromSecondsToHMS(user.playTime))
        inputString = inputString.replace("|joined_date|", user.joinDate.format(DateTimeFormatter.ISO_DATE))

        return inputString
    }

    override fun getDimensions(): Pair<Int, Int> {
        return Pair(width, height)
    }
}