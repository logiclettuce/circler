package osu.salat23.circler.bot.response.context

import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.with
import osu.salat23.circler.bot.response.context.ContextKeys as ckn

class AdditionalContext(
    val server: Server
): TemplateContext(
    mapOf(
        ckn.OsuServer.value with server.displayName,
    )
)