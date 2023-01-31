package osu.salat23.circler.bot.response.templates

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.response.context.TemplateContext

@Component
class TemplateFactory(
    val responseTemplates: Map<ResponseTemplates, Template>
) {
    fun applyTemplateContext(templateType: ResponseTemplates, customTemplate: Template, vararg context: TemplateContext): Template {
        val rawTemplate = responseTemplates[templateType]!!
        val contextMap = getContextMap(context.toList())
        val text = applyContextToString(customTemplate.text.ifEmpty { rawTemplate.text }, contextMap)
        val html = applyContextToString(customTemplate.html.ifEmpty { rawTemplate.html }, contextMap)
        return Template(text, html)
    }

    private fun applyContextToString(input: String, contextMap: Map<String, String>): String {
        var input = input
        for (entry in contextMap.entries) {
            input = input.replace("{{${entry.key}}}", entry.value)
        }
        return input
    }

    private fun getContextMap(vararg context: TemplateContext): Map<String, String> {
        return context.map { it.map }.reduce { acc, map -> map + acc}
    }

    private fun getContextMap(templateContexts: List<TemplateContext>): Map<String, String> {
        return templateContexts.map { it.map }.reduce { acc, map -> map + acc}
    }
}