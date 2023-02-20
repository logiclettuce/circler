package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default

@Command(
    name = "Set template",
    description = "Set your custom specified template.",
    identifiers = ["template", "tmpl", "еуьздфеу", "еьзд"]
)
class SetChatTemplateCommand(
    @Argument(
        name = "Template type",
        description = "The type of the template you want to set.",
        identifiers = ["template", "tmpl", "еуьздфеу", "еьзд"],
        required = true,
    )
    var template: String,
    @Argument(
        name = "For render",
        description = "Set this template for render or not.",
        identifiers = ["render", "r", "кутвук", "к"],
    )
    @Default("false")
    var forRender: Boolean
)