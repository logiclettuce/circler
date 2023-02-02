package osu.salat23.circler.bot.response.browser

import com.microsoft.playwright.Playwright
import com.microsoft.playwright.TimeoutError
import java.io.ByteArrayInputStream
import java.io.InputStream

class BrowserClient {
    private val playwright: Playwright = Playwright.create()
    private val browser = playwright.chromium().launch()

    // this method is used for processing html and getting a screenshot of the page
    fun render(htmlTemplate: String): InputStream {
        val page = browser.newPage()
        return try {
            page.setDefaultNavigationTimeout(10000.0)
            page.setContent(htmlTemplate)
            val htmlTag = page.locator("html")
            val width = htmlTag.boundingBox().width
            val height = htmlTag.boundingBox().height
            page.setViewportSize(width.toInt(), height.toInt())
            val byteArray = page.screenshot()
            return ByteArrayInputStream(byteArray)
        } catch (timeoutError: TimeoutError) {
           return javaClass.classLoader.getResourceAsStream("response/html/timeout.png") ?: InputStream.nullInputStream()
        } finally {
            page.close()
        }
    }

    // this method is used to evaluate the input string, and it returns evaluation result in string format
    fun process(htmlTextTemplate: String): String {
        val page = browser.newPage()
        return try {
            page.setDefaultNavigationTimeout(5000.0)
            page.setContent(htmlTextTemplate)
            val htmlTag = page.locator("content")
            val result = htmlTag.textContent()
            result
        } catch (timeoutError: TimeoutError) {
            "Template could not be loaded (timed out)"
        } finally {
            page.close()
        }
    }
}