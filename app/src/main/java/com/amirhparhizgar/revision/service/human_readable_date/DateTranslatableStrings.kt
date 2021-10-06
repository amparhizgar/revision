package com.amirhparhizgar.revision.service.human_readable_date

import java.util.*

interface DateTranslatableStrings {
    val today: String
    val oneDay: String
    val twoDays: String
    val days: String
    val oneDayAgo: String
    val daysAgo: String
    val oneMonth: String
    val months: String
    val oneMonthAgo: String
    val monthsAgo: String
    val oneYear: String
    val years: String
    val oneYearAgo: String
    val yearsAgo: String
    val locale: Locale?
}