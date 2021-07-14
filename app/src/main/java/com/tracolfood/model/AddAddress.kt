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
class AddAddress  : Parcelable{
    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("user_id")
    @Expose
    val userId: Int? = null

    @SerializedName("first_name")
    @Expose
    val firstName: String? = null

    @SerializedName("last_name")
    @Expose
    val lastName: String? = null

    @SerializedName("primary_address")
    @Expose
    val primaryAddress: String? = null

    @SerializedName("secondary_address")
    @Expose
    val secondaryAddress: String? = null

    @SerializedName("city")
    @Expose
    val city: String? = null

    @SerializedName("state")
    @Expose
    val state: String? = null

    @SerializedName("country")
    @Expose
    val country: String? = null

    @SerializedName("zipcode")
    @Expose
    val zipcode: String? = null

    @SerializedName("contact_no")
    @Expose
    val contactNo: String? = null

    @SerializedName("no_of_box")
    @Expose
    val noOfBox: Int? = null

    @SerializedName("is_default")
    @Expose
    var isDefault: Int? = null

    @SerializedName("date")
    @Expose
    val date: String? = null

    @SerializedName("time")
    @Expose
    val time: Any? = null

    @SerializedName("state_id")
    @Expose
    val stateId: Int? = null

    @SerializedName("type_id")
    @Expose
    val typeId: Int? = null

    @SerializedName("created_on")
    @Expose
    val createdOn: String? = null

    @SerializedName("created_by_id")
    @Expose
    val createdById: Int? = null
    var isSelected :Boolean = false
}