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
class VideoData : Parcelable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("video_file")
    @Expose
    var videoFile: String? = null

    @SerializedName("image_file")
    @Expose
    var imageFile: String? = null

    @SerializedName("youtub_link")
    @Expose
    var youtubLink: String? = null

    @SerializedName("state_id")
    @Expose
    var stateId: Int? = null

    @SerializedName("type_id")
    @Expose
    var typeId: Any? = null

    @SerializedName("created_on")
    @Expose
    var createdOn: String? = null

    @SerializedName("created_by_id")
    @Expose
    var createdById: Int? = null
}