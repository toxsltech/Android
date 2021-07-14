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
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationData(
        @SerializedName("created_by_id")
        var createdById: Int? = null,
        @SerializedName("created_on")
        var createdOn: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("is_read")
        var isRead: Int? = null,
        @SerializedName("model_id")
        var modelId: Int? = null,
        @SerializedName("model_type")
        var modelType: String? = null,
        @SerializedName("state_id")
        var stateId: Int? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("to_user_id")
        var toUserId: Int? = null,
        @SerializedName("type_id")
        var typeId: Int? = null
) : Parcelable