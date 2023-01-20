package osu.salat23.circler.configuration.domain.json

class CommandConfigurationJSON(
    val arguments: Map<String, ArgumentJSON> = emptyMap(),
    val commands: Map<String, CommandJSON> = emptyMap()
)