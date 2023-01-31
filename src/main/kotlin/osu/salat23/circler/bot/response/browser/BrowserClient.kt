package osu.salat23.circler.bot.response.browser

import com.microsoft.playwright.Playwright
import java.io.ByteArrayInputStream
import java.io.InputStream

class BrowserClient {
    private val playwright: Playwright = Playwright.create()
    private val browser = playwright.chromium().launch()

    // this method is used for processing html and getting a screenshot of the page
    @Synchronized
    fun render(htmlTemplate: String): InputStream {
        browser.newPage().use { page ->
            page.setContent(htmlTemplate)
            val htmlTag = page.locator("html")
            val width = htmlTag.boundingBox().width
            val height = htmlTag.boundingBox().height
            page.setViewportSize(width.toInt(), height.toInt())
            val byteArray = page.screenshot()
            return ByteArrayInputStream(byteArray)
        }
    }

    // this method is used to evaluate the input string, and it returns evaluation result in string format
    @Synchronized
    fun process(input: String): String {
        browser.newPage().use { page ->
            return page.evaluate(input).toString()
        }
    }

}