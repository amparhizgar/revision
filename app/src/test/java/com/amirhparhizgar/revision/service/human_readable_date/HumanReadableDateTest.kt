package com.amirhparhizgar.revision.service.human_readable_date

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class HumanReadableDateTest {

    private lateinit var humanReadableDate: HumanReadableDate
    private val strings = HumanReadableStringsTestDouble()

    @Before
    fun setUp() {
        humanReadableDate = HumanReadableDate(strings)
        humanReadableDate.setTimeProvider { sampleDay }
    }

    private val sampleDay = 1633461243000L // Oct 5 2021

    @Test
    fun convertToRelativeToday() {
        assertThat(humanReadableDate.convertToRelative(sampleDay + 50000)).ignoringCase()
            .isEqualTo("today")
    }

    @Test
    fun convertToRelativeTomorrow() {
        assertThat(humanReadableDate.convertToRelative(sampleDay + 1.day - 60000)).ignoringCase()
            .isEqualTo("Tomorrow")
    }

    @Test
    fun convertToRelativeNextMonth() {
        assertThat(humanReadableDate.convertToRelative(sampleDay + (1.month + 1.day))).ignoringCase()
            .isEqualTo(strings.oneMonth)
    }


    @Test
    fun convertToRelativePreviousMonth() {
        assertThat(humanReadableDate.convertToRelative(sampleDay - (1.month + 1.day))).ignoringCase()
            .isEqualTo(strings.oneMonthAgo)
    }

    @Test
    fun convertToRelativeTwoMonth() {
        humanReadableDate.setTimeProvider { sampleDay }
        assertThat(humanReadableDate.convertToRelative(sampleDay + 2.month + 2.day)).ignoringCase()
            .isEqualTo(strings.months.format(2))
    }

    @Test
    fun convertToRelativeTwoMonthAgo() {
        assertThat(humanReadableDate.convertToRelative(sampleDay - 2.month - 2.day)).ignoringCase()
            .isEqualTo(strings.monthsAgo.format(2))
    }

    @Test
    fun convertToAbsoluteToday() {
        assertThat(humanReadableDate.convertToAbsolute(sampleDay)).isEqualTo("Today")
    }

    @Test
    fun convertToAbsoluteWeek() {
        assertThat(humanReadableDate.convertToAbsolute(sampleDay + 7.day)).isEqualTo("Oct 12")
    }

    @Test
    fun convertToAbsoluteThisYear() {
        assertThat(humanReadableDate.convertToAbsolute(sampleDay + 1.month)).isEqualTo("Nov 5")
    }

    @Test
    fun convertToAbsolutePastYear() {
        assertThat(humanReadableDate.convertToAbsolute(sampleDay - 1.year)).isEqualTo("2020 Oct")
    }

    private val Int.day: Long
        get() = this * 24 * 60 * 60 * 1000L

    private val Int.month: Long
        get() = (this.day * (365F / 12)).toLong()

    private val Int.year: Long
        get() = this.day * 365
}