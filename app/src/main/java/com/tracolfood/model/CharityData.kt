package com.tracolfood.model

import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class CharityData : Parcelable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("image_file")
    @Expose
    var imageFile: String? = null

    @SerializedName("goal_amount")
    @Expose
    var goalAmount: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("raised_amount")
    @Expose
    var raisedAmount: String? = null

    @SerializedName("min_amount")
    @Expose
    var minAmount: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("max_amount")
    @Expose
    var maxAmount: String? = null

    @SerializedName("state_id")
    @Expose
    var stateId: Int? = null

    @SerializedName("type_id")
    @Expose
    var typeId: Int? = null

    @SerializedName("created_on")
    @Expose
    var createdOn: String? = null

    @SerializedName("created_by_id")
    @Expose
    var createdById: Int? = null

}