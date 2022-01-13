package com.amirhparhizgar.revision.model

import androidx.annotation.StringRes
import com.amirhparhizgar.revision.R

enum class ThemeMode(val id: Int, @StringRes val textResId: Int) {
    AUTOMATIC(0, R.string.automatic),
    LIGHT(1, R.string.light),
    DARK(2, R.string.dark);

    companion object {
        val list = listOf(AUTOMATIC, LIGHT, DARK)
        fun fromId(id: Int): ThemeMode {
            return list.first { it.id == id }
        }
    }
}
