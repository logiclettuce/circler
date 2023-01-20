package osu.salat23.circler.configuration.domain.json

class CommandJSON(
    val name: String,
    val description: String = "",
    val availableArguments: List<String> = emptyList(),
    val identifiers: List<String> = emptyList()
)