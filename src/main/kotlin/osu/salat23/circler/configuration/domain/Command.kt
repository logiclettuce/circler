package osu.salat23.circler.configuration.domain

class Command(
    val name: String,
    val description: String,
    val arguments: List<Argument>,
    val identifiers: List<String>
)