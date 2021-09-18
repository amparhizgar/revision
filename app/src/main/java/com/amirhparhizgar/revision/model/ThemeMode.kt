package com.amirhparhizgar.revision.model

import androidx.annotation.StringRes
import com.amirhparhizgar.revision.R

object ThemeMode {
    const val AUTOMATIC = 0
    const val LIGHT = 1
    const val DARK = 2

    fun getRadioOption(mode: Int) =
        when (mode) {
            AUTOMATIC -> RadioOption(R.string.automatic, AUTOMATIC)
            LIGHT -> RadioOption(R.string.light, LIGHT)
            DARK -> RadioOption(R.string.dark, DARK)
            else -> throw IllegalArgumentException("use ThemeMode.AUTOMATIC, .DARK, etc")
        }

    val list = (AUTOMATIC..DARK).map { getRadioOption(it) }

    class RadioOption(@StringRes val textResId: Int, val ThemeMode: Int)
}
