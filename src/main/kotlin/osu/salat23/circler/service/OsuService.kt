package osu.salat23.circler.service

import org.springframework.stereotype.Service
import osu.salat23.circler.api.osu.OsuApi
import osu.salat23.circler.osu.Server

@Service
class OsuService(private val bancho: OsuApi) {

    fun getOsuApiByServer(server: Server): OsuApi {
        return when (server) {
            Server.Bancho -> bancho
            else -> bancho
        }
    }


}