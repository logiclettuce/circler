package osu.salat23.circler.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import osu.salat23.circler.renderer.BrowserClient

@Configuration
class BrowserClientConfig {

    @Bean
    @Scope("singleton")
    fun browserClient(): BrowserClient {
        return BrowserClient()
    }
}