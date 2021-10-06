package com.amirhparhizgar.revision.service.human_readable_date

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.absoluteValue

class HumanReadableDate @Inject constructor(val strings: DateTranslatableStrings) {

    private var timeProvider: () -> Long = System::currentTimeMillis
    fun setTimeProvider(provider: () -> Long) {
        timeProvider = provider
    }

    fun convertToRelative(milliseconds: Long): String {
        val toConvert = Calendar.getInstance()
        toConvert.timeInMillis = milliseconds
        val diffDays = diffDays(toConvert)
        return when {
            (diffDays.absoluteValue >= 365) ->
                when {
                    diffDays.year <= -2 -> strings.yearsAgo.format(abs(diffDays.absoluteValue.year))
                    diffDays.year == -1 -> strings.oneYearAgo
                    diffDays.year >= 2 -> strings.years.format(diffDays.year)
                    else -> strings.oneYear
                }
            (diffDays.absoluteValue >= daysOfMonth) ->
                when {
                    diffDays.month <= -2 -> strings.monthsAgo.format(abs(diffDays.absoluteValue.month))
                    diffDays.month == -1 -> strings.oneMonthAgo
                    diffDays.month >= 2 -> strings.months.format(diffDays.month)
                    else -> strings.oneMonth
                }
            else ->
                when {
                    diffDays <= -2 -> strings.daysAgo.format(abs(diffDays.absoluteValue))
                    diffDays == -1 -> strings.oneDayAgo
                    diffDays >= 3 -> strings.days.format(diffDays)
                    diffDays >= 2 -> strings.twoDays.format(diffDays)
                    diffDays >= 1 -> strings.oneDay
                    else -> strings.today
                }
        }
    }

    private fun diffDays(calendar: Calendar): Int {
        val start = Calendar.getInstance().apply { timeInMillis = timeProvider() }
        start[Calendar.HOUR_OF_DAY] = 0
        start[Calendar.MINUTE] = 0
        start[Calendar.SECOND] = 0
        start[Calendar.MILLISECOND] = 0

        val end = Calendar.getInstance()
        end.timeInMillis = calendar.timeInMillis
        end[Calendar.HOUR_OF_DAY] = 0
        end[Calendar.MINUTE] = 0
        end[Calendar.SECOND] = 0
        end[Calendar.MILLISECOND] = 0

        return ((end.timeInMillis - start.timeInMillis) / millisOfDay).toInt()
    }

    private val Int.month: Int
        get() =
            (this / daysOfMonth).toInt()

    private val Int.year: Int
        get() =
            (this / 365)

    private val daysOfMonth: Float = (365F / 12)

    fun convertToAbsolute(milliseconds: Long): String {
        val that = Calendar.getInstance().apply { timeInMillis = milliseconds }
        val format: String = when {
            diffDays(that) == 0 -> return strings.today
            diffDays(that).absoluteValue <= 6 -> {
                "EEE" // Mon
            }
            diffDays(that).absoluteValue >= 365 -> "yyyy MMM" //2021 Oct
            else -> "MMM d"
        }
        val dateFormat = SimpleDateFormat(format, strings.locale)
        val date = Date(milliseconds)
        return dateFormat.format(date)
    }

    private val millisOfDay: Long = 24 * 60 * 60 * 1000L
}