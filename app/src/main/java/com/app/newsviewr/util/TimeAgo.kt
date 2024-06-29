package com.app.newsviewr.util

class DateHelper {

    /**
     * Adapted from: https://medium.com/@shaktisinh/time-a-go-in-android-8bad8b171f87
     */
    fun getTimeAgo(startDateEpoch: Long): String {

        val currentTimeMillis = System.currentTimeMillis()
        val startDateTimeMillis = startDateEpoch * 1000
        val diff = currentTimeMillis - startDateTimeMillis

        return when {
            diff <= 0 -> ""
            diff < MINUTE_MILLIS -> "just now"
            diff < 2 * MINUTE_MILLIS -> "a minute ago"
            diff < 50 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
            diff < 90 * MINUTE_MILLIS -> "an hour ago"
            diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
            diff < 48 * HOUR_MILLIS -> "yesterday"
            diff < WEEK_MILLIS -> "${diff / DAY_MILLIS} days ago"
            diff < 2 * WEEK_MILLIS -> "a week ago"
            diff < MONTH_MILLIS -> "${diff / WEEK_MILLIS} weeks ago"
            diff < 2 * MONTH_MILLIS -> "a month ago"
            diff < YEAR_MILLIS -> "${diff / MONTH_MILLIS} months ago"
            diff < 2 * YEAR_MILLIS -> "a year ago"
            else -> "${diff / YEAR_MILLIS} years ago"
        }
    }

    companion object {
        private const val SECOND_MILLIS : Long = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS
        private const val WEEK_MILLIS = 7 * DAY_MILLIS
        private const val MONTH_MILLIS = 30 * DAY_MILLIS
        private const val YEAR_MILLIS = 365 * DAY_MILLIS
    }
}