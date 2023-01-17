package osu.salat23.circler.renderer

import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import java.io.ByteArrayInputStream
import java.io.InputStream

class BrowserClient {
    private val playwright: Playwright = Playwright.create()
    private val browser = playwright.chromium().launch()

    @Synchronized fun render(htmlRenderTemplate: HtmlRenderTemplate): InputStream {
        val page: Page = browser.newPage()

        page.setContent(htmlRenderTemplate.getHtml())
        val htmlTag = page.locator("html")
        val width = htmlTag.boundingBox().width
        val height = htmlTag.boundingBox().height
        page.setViewportSize(width.toInt(), height.toInt())
        val byteArray = page.screenshot()
        page.close()

        return ByteArrayInputStream(byteArray)
    }

}