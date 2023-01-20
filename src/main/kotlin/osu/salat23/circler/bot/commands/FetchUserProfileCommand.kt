package osu.salat23.circler.bot.commands

import osu.salat23.circler.bot.commands.arguments.ActorArgument
import osu.salat23.circler.bot.commands.arguments.ProvidedArgument
import osu.salat23.circler.bot.commands.arguments.ServerArgument

class FetchUserProfileCommand(
    val actorArgument: ProvidedArgument<ActorArgument>,
    val serverArgument: ProvidedArgument<ServerArgument>
) : Command() {


}