/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.utils

import com.tracolfood.BuildConfig
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun changeDateFormat(dateString: String?, sourceDateFormat: String = "yyyy-MM-dd", targetDateFormat: String = "dd MMM, yyyy"): String {
        if (dateString == null || dateString.isEmpty()) {
            return ""
        }
        val inputDateFormat = SimpleDateFormat(sourceDateFormat, Locale.getDefault())
        var date = Date()
        try {
            date = inputDateFormat.parse(dateString)
        } catch (e: ParseException) {
            logStack(e)
        }

        val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.getDefault())
        return outputDateFormat.format(date)
    }


    fun changeDateFormatFromDate(sourceDate: Date?, format: String? = "yyyy-MM-dd"): String {
        if (sourceDate == null || format == null || format.isEmpty()) {
            return ""
        }
        val outputDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return outputDateFormat.format(sourceDate)
    }


    fun createDateFromString(sourceDate: String?, sourceFormat: String = "yyyy-MM-dd"): Date {
        if (sourceDate == null || sourceDate.isEmpty()) {
            return Date()
        }
        val inputDateFromat = SimpleDateFormat(sourceFormat, Locale.getDefault())
        var date = Date()
        try {
            date = inputDateFromat.parse(sourceDate)
        } catch (e: ParseException) {
            logStack(e)
        }

        return date
    }


    fun createDateFromMilliSeconds(sourceDate: Long): Date {
        val inputDateFromat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var date = Date()
        try {
            date = inputDateFromat.parse(Date(sourceDate).toString())
        } catch (e: ParseException) {
            logStack(e)
        }

        return date
    }

    private fun logStack(e: Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
    }

    fun getLocalTimeFromUTCString(sourceDate: String?, currentFormat: String = "yyyy-MM-dd HH:mm:ss", targetFormat: String = "yyyy-MM-dd HH:mm:ss"): String {
        if (sourceDate.isNullOrEmpty()) {
            return ""
        }

        val date = getLocalDateFromUtcDate(sourceDate, currentFormat)

        return changeDateFormatFromDate(date, targetFormat)

    }

    fun getLocalDateFromUtcDate(sourceDate: String, currentFormat: String): Date? {
        val simpleDateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        var myDate: Date? = Date()
        try {
            myDate = simpleDateFormat.parse(sourceDate)
        } catch (e: ParseException) {
            logStack(e)
        }

        return myDate
    }


}
