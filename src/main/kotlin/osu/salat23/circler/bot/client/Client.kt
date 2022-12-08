package osu.salat23.circler.bot.client

abstract interface Client {
    abstract fun send(clientEntity: ClientEntity)
}