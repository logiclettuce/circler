package osu.salat23.circler

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import osu.salat23.circler.config.OsuApiConfig
import osu.salat23.circler.service.OsuService

@ExtendWith(SpringExtension::class)
@SpringBootTest
class CirclerApplicationTests() {

    @Autowired
    lateinit var osuService: OsuService

    @Test
    fun contextLoads() {

    }

}
