package osu.salat23.circler.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("vk")
class VkProperties (
    var appId: String = "",
    var clientSecret: String = "",
    var redirectUri: String = "",
    var accessToken: String = "",
    var groupId: String = ""
)