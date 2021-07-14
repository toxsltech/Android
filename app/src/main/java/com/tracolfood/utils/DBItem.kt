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

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable


class DBItem(`in`: Parcel) : Parcelable {
    var id: Long = 0
    var title: String? = null

    init {
        id = `in`.readLong()
        title = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<DBItem> = object : Parcelable.Creator<DBItem> {
            override fun createFromParcel(`in`: Parcel): DBItem {
                return DBItem(`in`)
            }

            override fun newArray(size: Int): Array<DBItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}
