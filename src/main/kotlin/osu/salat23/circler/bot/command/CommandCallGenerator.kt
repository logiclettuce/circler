package osu.salat23.circler.bot.command

interface CommandCallGenerator {

    /**
     * @param command passed object's class must have this [annotation][osu.salat23.circler.bot.command.annotations.Command]
     * for call to be generated correctly.
     */
    fun generateCall(command: Any): String

}