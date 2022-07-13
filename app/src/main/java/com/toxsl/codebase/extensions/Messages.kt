/*
 * @copyright : ToXSL Technologies Pvt. Ltd. < www.toxsl.com >
 * @author     : Shiv Charan Panjeta < shiv@toxsl.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of ToXSL Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.toxsl.codebase.extensions

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.toxsl.codebase.BuildConfig
import com.toxsl.codebase.R

import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getMessage(json: JSONObject, baseActivity: AppCompatActivity) {
    var string: String?
    string = baseActivity.getString(R.string.success_msg)
    if (json.has("message")) {
        string = json.getString("message")
    }
    showToastMain(baseActivity, string!!)
}

fun getErrorMsg(json: JSONObject, baseActivity: AppCompatActivity) {
    var string: String?
    string = baseActivity.getString(R.string.something_went_wrong)
    if (json.has("error")) {
        string = json.getString("error")
    }
    showToastMain(baseActivity, string!!)
}

fun showToastMain(baseActivity: AppCompatActivity, string: String) {
    Toast.makeText(baseActivity, string, Toast.LENGTH_SHORT).show()
}

fun TextView.setColor(baseActivity: AppCompatActivity?, lightGrey: Int) {
    this.setTextColor(ContextCompat.getColor(baseActivity!!, lightGrey))
}


fun changeDateFormat(dateString: String): String {
    if (dateString.isEmpty()) {
        return ""
    }
    val inputDateFormat = SimpleDateFormat("yyyy-MM-DD HH:MM:SS", Locale.getDefault())
    var date = Date()
    try {
        date = inputDateFormat.parse(dateString)
    } catch (e: ParseException) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
    }

    val outputDateFormat = SimpleDateFormat("EEE, MMM d, ''yy h:mm a", Locale.getDefault())
    return outputDateFormat.format(date)
}




