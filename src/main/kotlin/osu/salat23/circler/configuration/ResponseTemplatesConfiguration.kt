package osu.salat23.circler.configuration

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import osu.salat23.circler.bot.response.templates.ResponseTemplates
import osu.salat23.circler.bot.response.templates.Template
import java.io.InputStream

@Configuration
class ResponseTemplatesConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun responseTemplate(): Map<ResponseTemplates, Template> {
        // go through each ResponsTemplates enum entry and try to load appropriate file
        // get text file and if present -  html file contents
        // map each entry to file content with matching file name

        val htmlTemplatesPath = "response/html/"
        val htmlTemplatesExtension = ".html"
        val textTemplatesPath = "response/text/"
        val textTemplatesExtension = ".txt"
        val classLoader = javaClass.classLoader

        val resultMap = mutableMapOf<ResponseTemplates, Template>()
        ResponseTemplates.values().forEach {
            var loadedHtmlResource: InputStream? = null
            if (it.hasHtml) loadedHtmlResource =
                classLoader.getResourceAsStream(htmlTemplatesPath + it.filename + htmlTemplatesExtension)
            val loadedTextResource =
                classLoader.getResourceAsStream(textTemplatesPath + it.filename + textTemplatesExtension)
            if (loadedHtmlResource == null && it.hasHtml) throw IllegalAccessException("Could not load '${it.filename}' html template.")
            if (loadedTextResource == null) throw IllegalAccessException("Could not load '${it.filename}' text template.")
            val text = loadedTextResource.bufferedReader().readLines().joinToString("\n")
            var html = ""
            if (it.hasHtml) html = loadedHtmlResource?.bufferedReader()?.readLines()?.joinToString("\n") ?: ""
            resultMap[it] = Template(
                text = text,
                html = html
            )
        }
        return resultMap
    }
}