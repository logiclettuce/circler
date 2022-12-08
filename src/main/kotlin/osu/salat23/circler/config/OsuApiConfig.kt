package osu.salat23.circler.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import osu.salat23.circler.osu.api.Bancho
import osu.salat23.circler.osu.api.OsuApi
import osu.salat23.circler.properties.OsuProperties

@Configuration
class OsuApiConfig(
    val osuProperties: OsuProperties
) {

    @Bean
    @Scope(value = "singleton")
    fun bancho(): OsuApi {
        return Bancho(osuProperties)
    }

}