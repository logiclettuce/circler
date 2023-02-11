package osu.salat23.circler.bot.response.templates

data class Template(
    val value: String,
    val type: TemplateType,
    val format: TemplateFormat
) {
    constructor(
        oldTemplate: Template,
        newValue: String
    ): this(newValue, oldTemplate.type, oldTemplate.format)
}