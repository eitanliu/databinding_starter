package com.eitanliu.utils

import java.util.Calendar
import java.util.Date

object DateUtil {

    val Date.timeSeconds get() = this.time / 1000

    val Date.yearOfLocale
        get() = Calendar.getInstance().let { calendar ->
            calendar.time = this
            calendar[Calendar.YEAR]
        }

    // 月份从0开始
    val Date.monthOfYear
        get() = Calendar.getInstance().let { calendar ->
            calendar.time = this
            calendar[Calendar.MONTH]
        }

    val Date.dayOfMonth
        get() = Calendar.getInstance().let { calendar ->
            calendar.time = this
            calendar[Calendar.DAY_OF_MONTH]
        }

    // 每月最大天数
    val Date.maxDayOfMonth
        get() = Calendar.getInstance().let { calendar ->
            calendar.time = this
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

    val Date.hour
        get() = Calendar.getInstance().let { calendar ->
            calendar.time = this
            calendar[Calendar.HOUR_OF_DAY]
        }

    val Date.minute
        get() = Calendar.getInstance().let { calendar ->
            calendar.time = this
            calendar[Calendar.MINUTE]
        }

    val Date.second
        get() = Calendar.getInstance().let { calendar ->
            calendar.time = this
            calendar[Calendar.SECOND]
        }

    fun Date.trimDate() = Calendar.getInstance().let { calendar ->
        calendar.time = this
        calendar[Calendar.YEAR] = 1970
        calendar[Calendar.MONTH] = Calendar.JANUARY
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.time
    }

    fun Date.trimTime() = Calendar.getInstance().let { calendar ->
        calendar.time = this
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar.time
    }

    // 年份差
    fun Date.betweenYears(other: Date) = yearOfLocale - other.yearOfLocale

    // 月份差
    fun Date.betweenMonths(other: Date) = run {
        betweenYears(other) * 12 + (monthOfYear - other.monthOfYear)
    }

    // 天数差
    fun Date.betweenDays(other: Date) = run {
        // val dayMillis = 0x7FFFFFFF_F8000000L
        // val between = (this.time and dayMillis) - (other.time and dayMillis)
        val between = trimTime().time - other.trimTime().time
        between / (1000 * 60 * 60 * 24)
    }

    // 小时差
    fun Date.betweenHours(other: Date) = (this.time - other.time) / (1000 * 60 * 60)

    // 分钟差
    fun Date.betweenMinutes(other: Date) = (this.time - other.time) / (1000 * 60)

    // 秒差
    fun Date.betweenSeconds(other: Date) = (this.time - other.time) / 1000

    // 毫秒差
    fun Date.betweenMillis(other: Date) = this.time - other.time
}