package osu.salat23.circler.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("osu")
class OsuProperties (
    var clientId: String = "",
    var clientSecret: String = ""
)