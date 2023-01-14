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
        val dimensions = htmlRenderTemplate.getDimensions()

        page.setViewportSize(dimensions.first, dimensions.second)
        page.setContent(htmlRenderTemplate.getHtml())
        val byteArray = page.screenshot()
        page.close()

        return ByteArrayInputStream(byteArray)
    }

}