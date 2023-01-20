package osu.salat23.circler.configuration.domain.json

class ArgumentJSON(
    val name: String,
    val description: String,
    val identifiers: List<String> = emptyList()
)