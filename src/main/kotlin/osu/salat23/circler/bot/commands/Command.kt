package osu.salat23.circler.bot.commands

import java.util.regex.Pattern

class Command private constructor(
    val server: Server,
    val action: Action,
    val options: Options
) {

    enum class Server(val identifiers: Array<String>) {
        Bancho(
            arrayOf(
                "b", "s", "bancho", "standard", "std",
                "и", "ы", "ифтсрщ", "ыефтвфкв", "ыев"
            )
        )
    }

    enum class Action(val identifiers: Array<String>) {
        USER_GENERAL(
            arrayOf(
                "u", "user",
                "г", "гыук"
            )
        ),
        USER_TOP_SCORES(
            arrayOf(
                "t", "top",
                "е", "ещз"
            )
        ),
        USER_RECENT_SCORES(
            arrayOf(
                "r", "recent",
                "к", "кусуте"
            )
        )
    }

    class Options private constructor(
        val pageNumber: Int,
        val pageSize: Int,
        val actor: String
    ) {
        enum class ArgumentIdentifiers(val identifiers: Array<String>) {
            PAGE_NUMBER(arrayOf("p", "page", "з", "зфпу")),
            PAGE_SIZE(arrayOf("s", "size", "ы", "ышяу")),
            ACTOR(arrayOf("n", "nick", "nickname", "player", "т", "тшсл", "тшслтфьу", "здфнук")),
        }

        class Builder(defaultOptions: Options? = null) {
            private var pageNumber: Int = defaultOptions?.pageNumber ?: 0
            private var pageSize: Int = defaultOptions?.pageSize ?: 5
            private var actor: String = ""
            fun build() = Options(
                pageNumber,
                pageSize,
                actor
            )

            fun pageNumber(pageNumber: Int) = this.apply { this.pageNumber = pageNumber }
            fun pageSize(pageSize: Int) = this.apply { this.pageSize = pageSize }
            fun actor(actor: String) = this.apply { this.actor = actor }
        }
    }

    class Builder {
        private lateinit var server: Server
        private lateinit var action: Action
        private lateinit var options: Options

        fun build() = Command(server, action, options)
        fun from(input: String): Builder {
            val commandArguments = ArrayDeque(input.split((" ")))

            if (commandArguments.isEmpty()) throw NotABotCommandException()
            val server = commandArguments.removeFirst()
            this.server = when {
                Server.Bancho.identifiers.contains(server.lowercase()) -> Server.Bancho
                else -> throw NotABotCommandException()//WrongCommandFormatException(input, "Not a bot command: $server")
            }

            if (commandArguments.isEmpty()) throw NotABotCommandException()//WrongCommandFormatException(input, "Action is not specified")
            val action = commandArguments.removeFirst().lowercase()

            this.action = when {
                Action.USER_GENERAL.identifiers.contains(action) -> Action.USER_GENERAL
                Action.USER_TOP_SCORES.identifiers.contains(action) -> Action.USER_TOP_SCORES
                Action.USER_RECENT_SCORES.identifiers.contains(action) -> Action.USER_RECENT_SCORES
                // todo automate action type
                else -> throw NotABotCommandException()//WrongCommandFormatException(input, "No such action: $action")
            }

            options = parseOptions(
                input, when {
                    this.action == Action.USER_RECENT_SCORES -> Options.Builder().pageSize(1).build()
                    else -> null
                }
            )

            return this
        }

        private fun parseOptions(input: String, defaultOptions: Options?): Options {

            val matcher = Pattern.compile("-[\\w\\u0400-\\u04FF]+ [\\w\\u0400-\\u04FF]*").matcher(input)
            val args = mutableListOf<String>()
            while (matcher.find()) {
                args.add(matcher.group())
            }

            val optionsBuilder = Options.Builder(defaultOptions)
            for (argument in args) {
                val parts = argument.split(" ")
                val identifier = parts[0].substring(1)
                fun check(type: Options.ArgumentIdentifiers, arg: String) = type.identifiers.contains(arg)
                when {
                    check(Options.ArgumentIdentifiers.PAGE_NUMBER, identifier) -> {
                        if (parts.size <= 1) continue
                        if (!parts[1].matches(Regex("\\d+"))) continue
                        optionsBuilder.pageNumber(parts[1].toInt())
                    }

                    check(Options.ArgumentIdentifiers.PAGE_SIZE, identifier) -> {
                        if (parts.size <= 1) continue
                        if (!parts[1].matches(Regex("\\d+"))) continue
                        optionsBuilder.pageSize(parts[1].toInt())
                    }

                    check(Options.ArgumentIdentifiers.ACTOR, identifier) -> {
                        if (parts.size <= 1) continue
                        if (!parts[1].matches(Regex("[\\w\\u0400-\\u04FF]+"))) continue
                        optionsBuilder.actor(parts[1])
                    }
                }
            }
            return optionsBuilder.build()
        }
    }
}