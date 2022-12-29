package osu.salat23.circler.api.osu.exceptions

class RequestFailedException(val code: Int, override val message: String) : RuntimeException() {
}