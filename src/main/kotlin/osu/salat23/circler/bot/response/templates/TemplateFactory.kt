package osu.salat23.circler.bot.response.templates

import org.json.JSONObject
import org.springframework.stereotype.Component
import osu.salat23.circler.configuration.TypeFormat

@Component
class TemplateFactory(
    val templateType: Map<TypeFormat, Template>
) {
    fun applyTemplateContext(templateType: TemplateType, templateFormat: TemplateFormat, customTemplate: Template, jsonContext: JSONObject): Template {
        val rawTemplate = this.templateType[templateType to templateFormat]!!
        val bakedTemplateValue = applyContextToString(customTemplate.value.ifEmpty { rawTemplate.value }, jsonContext)
        return Template(customTemplate, bakedTemplateValue)
    }

    private fun applyContextToString(input: String, jsonContext: JSONObject): String {
        return input.replace("{{json_context}}", jsonContext.toString())
    }
}