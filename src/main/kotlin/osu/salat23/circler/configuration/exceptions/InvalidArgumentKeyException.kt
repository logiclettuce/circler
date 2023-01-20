package osu.salat23.circler.configuration.exceptions

class InvalidArgumentKeyException(invalidKey: String): RuntimeException("Invalid argument key $invalidKey. Could not found any argument with this key.")