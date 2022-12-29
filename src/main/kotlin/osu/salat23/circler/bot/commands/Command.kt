package osu.salat23.circler.bot.commands

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

class Command private constructor(
    val server: Server,
    val action: Action,
    val options: Options
) {

    enum class Server(
        val identifiers: Array<String>,
        val beatmapsetUrl: Regex) {
        Bancho(
            arrayOf(
                "b", "s", "bancho", "standard", "std",
                "и", "ы", "ифтсрщ", "ыефтвфкв", "ыев"
            ),
            Regex("https?:\\/\\/osu.ppy.sh\\/beatmapsets\\/[\\d]+(#[\\w]+)?\\/[\\d]+")
        ),
        None(arrayOf(), Regex(""))
    }

    enum class Action(val identifiers: Array<String>, val implicitArgument: Options.ArgumentIdentifiers? = null) {
        USER_GENERAL(
            arrayOf(
                "u", "user",
                "г", "гыук"
            ),
            Options.ArgumentIdentifiers.ACTOR
        ),
        USER_TOP_SCORES(
            arrayOf(
                "t", "top",
                "е", "ещз"
            ),
            Options.ArgumentIdentifiers.ACTOR
        ),
        USER_RECENT_SCORES(
            arrayOf(
                "r", "recent",
                "к", "кусуте"
            ),
            Options.ArgumentIdentifiers.ACTOR
        ),
        SET_USER_SERVER_IDENTIFIER(
            arrayOf(
                "n", "nick",
                "т", "тшсл"
            ),
            Options.ArgumentIdentifiers.ACTOR
        ),
        FUN(
            arrayOf(
                "f", "fun",
                "а", "агт"
            )
        ),
        BEATMAP_LOOKUP(
            arrayOf()
        )

        // todo make recommend map option

        // todo make local chat top with all registered users

        // todo make mrekk cookiezi and other players roulette (casino)
    }

    class Options private constructor(
        val pageNumber: Int,
        val pageSize: Int,
        val actor: String,
        val beatmapId: String
    ) {
        enum class ArgumentIdentifiers(val identifiers: Array<String>) {
            PAGE_NUMBER(arrayOf("p", "page", "з", "зфпу")),
            PAGE_SIZE(arrayOf("s", "size", "ы", "ышяу")),
            ACTOR(arrayOf("n", "nick", "nickname", "player", "т", "тшсл", "тшслтфьу", "здфнук")),
            // BEATMAP_ID
        }

        class Builder(defaultOptions: Options? = null) {
            private var pageNumber: Int = defaultOptions?.pageNumber ?: 0
            private var pageSize: Int = defaultOptions?.pageSize ?: 5
            private var actor: String = defaultOptions?.actor ?: ""
            private var beatmapId: String = ""
            fun build() = Options(
                pageNumber,
                pageSize,
                actor,
                beatmapId
            )

            fun pageNumber(pageNumber: Int) = this.apply { this.pageNumber = pageNumber }
            fun pageSize(pageSize: Int) = this.apply { this.pageSize = pageSize }
            fun actor(actor: String) = this.apply { this.actor = actor }
            fun beatmapId(id: String) = this.apply { this.beatmapId = id }
        }
    }

    class Builder {
        private lateinit var server: Server
        private lateinit var action: Action
        private lateinit var options: Options
        val logger: Logger = LoggerFactory.getLogger(Builder::class.java)
        fun build() = Command(server, action, options)
        fun from(input: String): Builder {
            var input = input
            val indexesToChange = mutableListOf<Int>()
            var insideQuotes = false
            var quotesCount = 0;
            for (char in input) {
                if (char == '\"') quotesCount++
            }
            if (quotesCount % 2 != 0) throw NotABotCommandException() // todo change to user warning
            for (i in input.indices) {
                if (input[i] == ' ' && insideQuotes) {
                    indexesToChange.add(i);
                } else if (input[i] == '\"') insideQuotes = !insideQuotes
            }
            for (index in indexesToChange.reversed()) {
                input = input.replaceRange(index, index + 1, "%20")
            }
            input = input.replace("\"", "")


            logger.info(input)
            // = Beatmap link =================================================================
            var isBeatmapUrl = false
            var serverType = Server.None
            for (server in Server.values()) {
                if (server.beatmapsetUrl.containsMatchIn(input) && server != Server.None) {
                    isBeatmapUrl = true
                    serverType = server
                    break
                }
            }

            if (isBeatmapUrl) {
                action = Action.BEATMAP_LOOKUP
                server = serverType
                val beatmapUrl = serverType.beatmapsetUrl.findAll(input).iterator().next().value
                var beatmapId = ""
                for (index in beatmapUrl.length-1 downTo 0) {
                    if (beatmapUrl[index] == '/') break
                    beatmapId += beatmapUrl[index]
                }
                beatmapId = beatmapId.reversed()
                options = Options.Builder().beatmapId(beatmapId).build()
                return this
            }


            // = Default command here ======================================================
            val commandArguments = ArrayDeque(input.split(" "))

            if (commandArguments.isEmpty()) throw NotABotCommandException()
            val server = commandArguments.removeFirst()
            this.server = when {
                Server.Bancho.identifiers.contains(server.lowercase()) -> Server.Bancho
                else -> throw NotABotCommandException()//WrongCommandFormatException(input, "Not a bot command: $server")
            }

            if (commandArguments.isEmpty()) throw NotABotCommandException()//WrongCommandFormatException(input, "Action is not specified")
            val action = commandArguments.removeFirst().lowercase()

            // determine action type for a command
            var actionType: Action? = null
            for (type in Action.values()) {
                if (type.identifiers.contains(action)) actionType = type
            }
            if (actionType == null) throw NotABotCommandException()
            this.action = actionType

            var implicitArgument: String? = null
            if (commandArguments.size != 0 && commandArguments.first()[0] != '-') implicitArgument =
                commandArguments.removeFirst()

            options = parseOptions(
                // specify default options for concrete action here
                input, when (this.action) {
                    Action.USER_RECENT_SCORES -> {
                        Options.Builder().apply {
                            pageSize(1)
                            if (implicitArgument != null) actor(implicitArgument)
                        }.build()
                    }

                    Action.USER_TOP_SCORES -> {
                        Options.Builder().apply {
                            if (implicitArgument != null) actor(implicitArgument)
                        }.build()
                    }

                    Action.SET_USER_SERVER_IDENTIFIER -> {
                        Options.Builder().apply {
                            if (implicitArgument != null) actor(implicitArgument)
                        }.build()
                    }

                    Action.USER_GENERAL -> {
                        Options.Builder().apply {
                            if (implicitArgument != null) actor(implicitArgument)
                        }.build()
                    }

                    else -> null
                }
            )

            return this
        }

        private fun parseOptions(input: String, defaultOptions: Options?): Options {

            val matcher = Pattern.compile("-[\\w\\u0400-\\u04FF]+ [\\w\\u0400-\\u04FF%]*").matcher(input)
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
                        if (!parts[1].matches(Regex("[\\w\\u0400-\\u04FF%]+"))) continue // todo create constants for argument types just in case
                        optionsBuilder.actor(parts[1])
                    }
                }
            }
            return optionsBuilder.build()
        }
    }
}