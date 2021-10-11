package com.amirhparhizgar.revision.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.amirhparhizgar.revision.R

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

    data class Bar(
        @StringRes val label: Int,
        val count: Int,
        @ColorRes val color: Int,
        var ratio: Float = 0F
    )

    companion object {
        val mock = BarChart(
            listOf(
                Bar(R.string.unseen, 2, R.color.unseen_color),
                Bar(R.string.learning, 5, R.color.learning_color),
                Bar(R.string.young, 10, R.color.young_color),
                Bar(R.string.mature, 4, R.color.mature_color),
            )
        )
    }
}