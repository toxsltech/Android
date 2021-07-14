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
class ItemData : Parcelable {
    @SerializedName("id")
    @Expose
     val id: Int? = null

    @SerializedName("order_id")
    @Expose
     val orderId: Int? = null

    @SerializedName("amount")
    @Expose
     val amount: String? = null

    @SerializedName("quantity")
    @Expose
     val quantity: String? = null

    @SerializedName("product_id")
    @Expose
     val productId: Int? = null

    @SerializedName("item")
    @Expose
     val product: ProductData? = null

}