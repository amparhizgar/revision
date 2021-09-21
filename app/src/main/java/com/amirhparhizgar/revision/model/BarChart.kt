package com.amirhparhizgar.revision.model

import androidx.compose.ui.graphics.Color

class BarChart(val barList: List<Bar>) {
    init {
        setRatios()
    }

    private fun setRatios() {
        this.barList.forEachIndexed { index, bar -> bar.ratio = ratioOf(index) }
    }

    val barCount get() = barList.size
    val totalCount get() = barList.sumOf { it.count }
    fun ratioOf(index: Int): Float {
        if (totalCount == 0)
            return 0F
        return barList[index].count / totalCount.toFloat()
    }

    data class Bar(val label: String, val count: Int, val color: Color, var ratio: Float = 0F)

    companion object {
        val mock = BarChart(
            listOf(
                Bar("Unseen", 2, Color(0xFF777777)),
                Bar("Learning", 5, Color(0xFFfa7339)),
                Bar("Young", 10, Color(0xFF10ad44)),
                Bar("Mature", 4, Color(0xFF0A67E0)),
            )
        )
    }
}