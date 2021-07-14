/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.model

import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderData : Parcelable {
    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("payment_type")
    @Expose
    val paymentType: Int? = null

    @SerializedName("payment_url")
    @Expose
    val paymentUrl: String? = null

    @SerializedName("payment_status")
    @Expose
    val paymentStatus: Int? = null

    @SerializedName("amount")
    @Expose
    val amount: String? = null

    @SerializedName("total_amount")
    @Expose
    val totalAmount: String? = null

    @SerializedName("state_id")
    @Expose
    val stateId: Int? = null

    @SerializedName("created_on")
    @Expose
    val createdOn: String? = null

    @SerializedName("items")
    @Expose
    val items: ArrayList<ItemData>? = ArrayList()
}