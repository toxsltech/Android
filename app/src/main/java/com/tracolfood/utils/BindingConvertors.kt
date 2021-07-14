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

import androidx.databinding.InverseMethod

class BindingConvertors {
    companion object {
        @InverseMethod("getGender")
        @JvmStatic
        fun setGender(gender: Int): String {
            val txt = when (gender) {

                else -> {
                    ""
                }
            }
            return txt
        }

        @JvmStatic
        fun getGender(gender: String): Int {
            val txt = when (gender) {

                else -> {
                    0
                }
            }
            return txt


        }

        @InverseMethod("getDateOfBirth")
        @JvmStatic
        fun setDateOfBirth(date: String): String {
            return DateUtils.changeDateFormat(date)
        }

        @JvmStatic
        fun getDateOfBirth(date: String): String {
            return DateUtils.changeDateFormat(date, sourceDateFormat = "dd MMM,yyyy", targetDateFormat = "yyyy-MM-dd")
        }

    }
}