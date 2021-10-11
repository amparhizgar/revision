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
        @JvmStatic
        fun oldnessBarChart(unseen: Int, learning: Int, young: Int, mature: Int) =
            BarChart(
                listOf(
                    Bar(R.string.unseen, unseen, R.color.unseen_color),
                    Bar(R.string.learning, learning, R.color.learning_color),
                    Bar(R.string.young, young, R.color.young_color),
                    Bar(R.string.mature, mature, R.color.mature_color),
                )
            )

        val mock = oldnessBarChart(1, 2, 3, 2)

        val emptyOldnessChart = oldnessBarChart(0, 0, 0, 0)
    }
}