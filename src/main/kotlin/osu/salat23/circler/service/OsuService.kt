package osu.salat23.circler.service

import org.springframework.stereotype.Service
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.api.osu.OsuApi

@Service
class OsuService(private val bancho: OsuApi) {

    fun getOsuApiByServer(server: Command.Server): OsuApi {
        return when (server) {
            Command.Server.Bancho -> bancho
            else -> bancho
        }
    }


}