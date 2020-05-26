package io.github.redstoneparadox.paradoxconfig.screen.widget

// Thx Juuxel
interface PageContainer {
    var currentPage: Int // 0-indexed
    val pageCount: Int

    fun hasPreviousPage(): Boolean
    fun hasNextPage(): Boolean

    fun showPreviousPage()
    fun showNextPage()
}