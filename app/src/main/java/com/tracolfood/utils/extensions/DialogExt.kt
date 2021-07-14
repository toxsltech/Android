/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.utils.extensions

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.tracolfood.R
import java.util.*


//common method to show alert dialog
fun Context.showAlertDialog(message: String = "", title: String = this.getString(R.string.alert), postiveBtnText: String = this.getString(R.string.ok), negativeBtnText: String = this.getString(R.string.cancel), handleClick: (positiveClick: Boolean) -> Unit = {}, isCancelable: Boolean = true) {


    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(isCancelable)
    builder.setPositiveButton(postiveBtnText) { dialog, _ ->
        dialog.dismiss()
        handleClick(true)
    }
    if (negativeBtnText.isNotEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialog, _ ->
            dialog.dismiss()
            handleClick(false)

        }
    }
    val dialog = builder.create()
    dialog.show()
    dialog.getButton(Dialog.BUTTON_NEGATIVE).setBackgroundResource(0)
    dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
    dialog.getButton(Dialog.BUTTON_POSITIVE).setBackgroundResource(0)
    dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
}


fun View.openDatePickerDialog(setMinDate: Boolean = false, setMaxDate: Boolean = false, setMaxYear: Int = 0, onDateSelect: (calendar: Calendar) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(this.context,
            { _, year, monthOfYear, dayOfMonth ->

                val selectedCal = Calendar.getInstance()
                selectedCal.set(Calendar.YEAR, year)
                selectedCal.set(Calendar.MONTH, monthOfYear)
                selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                onDateSelect(selectedCal)


            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    if (setMinDate) {
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    if (setMaxDate) {
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        if (setMaxYear != 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -setMaxYear)
            datePickerDialog.datePicker.maxDate = cal.timeInMillis - 1000

        }
    }


    datePickerDialog.show()
    datePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setBackgroundResource(0)
    datePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
    datePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setBackgroundResource(0)
    datePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))

}

fun View.openTimePickerDialog(calendar: Calendar = Calendar.getInstance(), is24Hour: Boolean = false, onTimeSelect: (calendar: Calendar) -> Unit) {
    val timePickerDialog = TimePickerDialog(this.context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                onTimeSelect(calendar)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24Hour)

    timePickerDialog.show()
    timePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setBackgroundResource(0)
    timePickerDialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
    timePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setBackgroundResource(0)
    timePickerDialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))

}


fun Context.showSingleChoiceAlertDialog(dataArray: Array<String>, selectedArray: Int = -1, title: String = this.getString(R.string.alert), negativeBtnText: String = this.getString(R.string.cancel), handleClick: (selectedPosition: Int) -> Unit = {}, isCancelable: Boolean = true) {

    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setSingleChoiceItems(dataArray, selectedArray, object : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            dialog?.dismiss()
            handleClick(which)

        }

    })

    builder.setCancelable(isCancelable)
    if (!negativeBtnText.isEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialog, which ->
            dialog.dismiss()

        }
    }
    val dialog = builder.create()
    dialog.show()
    dialog.getButton(Dialog.BUTTON_NEGATIVE).setBackgroundResource(0)
    dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
    dialog.getButton(Dialog.BUTTON_POSITIVE).setBackgroundResource(0)
    dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

}
