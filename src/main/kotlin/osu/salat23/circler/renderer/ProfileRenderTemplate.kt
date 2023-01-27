package osu.salat23.circler.renderer

import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.renderer.templates.ProfileTemplate
import java.io.BufferedReader

class ProfileRenderTemplate(val user: User, val server: Server, val command: Command, val customTemplate: String = "") : HtmlRenderTemplate {

    override fun getHtml(): String {

        var inputString: String
        if (customTemplate.isBlank()) {
            val classLoader = javaClass.classLoader
            val bufferedReader: BufferedReader =
                classLoader.getResourceAsStream("response/profile.html")?.bufferedReader()
                    ?: throw IllegalStateException("Could not find 'profile' template")
            inputString = bufferedReader.use { it.readText() }
        } else inputString = customTemplate

        val context = ProfileTemplate(user, server)
        for (entry in context.map.entries) {
            inputString = inputString.replace("{{${entry.key}}}", entry.value)
        }

        return inputString
    }
}