package osu.salat23.circler.bot.command.arguments.factories

import osu.salat23.circler.bot.command.arguments.Argument

interface ArgumentCallProducer<T: Argument> {

    fun produceCall(argument: T): String

}