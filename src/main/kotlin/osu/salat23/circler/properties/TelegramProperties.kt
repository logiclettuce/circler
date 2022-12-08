package osu.salat23.circler.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("telegram")
class TelegramProperties(
    var token: String = "",
    var botUserName: String = ""
)