package com.amirhparhizgar.revision.service.human_readable_date

import java.util.*

open class HumanReadableStringsTestDouble : HumanReadableStrings {
    override val today: String
        get() = "Today"
    override val oneDay: String
        get() = "Tomorrow"
    override val twoDays: String
        get() = "The day after tomorrow"
    override val days: String
        get() = "%d days"
    override val oneDayAgo: String
        get() = "Yesterday"
    override val daysAgo: String
        get() = "%d days ago"
    override val oneMonth: String
        get() = "One Month"
    override val months: String
        get() = "%d months"
    override val oneMonthAgo: String
        get() = "One month ago"
    override val monthsAgo: String
        get() = "%d months ago"
    override val oneYear: String
        get() = "One Year"
    override val years: String
        get() = "%d years"
    override val oneYearAgo: String
        get() = "One year ago"
    override val yearsAgo: String
        get() = "%d years ago"
    override val locale: Locale?
        get() = Locale.US
}