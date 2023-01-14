package osu.salat23.circler.renderer

interface HtmlRenderTemplate {
    fun getHtml(): String
    fun getDimensions(): Pair<Int, Int>
}