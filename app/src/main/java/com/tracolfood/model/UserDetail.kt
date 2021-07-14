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
import androidx.databinding.BaseObservable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetail(
        @SerializedName("about_me")
        var aboutMe: String = "",
        @SerializedName("contact_no")
        var contactNo: String = "",
        @SerializedName("created_by_id")
        var createdById: String = "",
        @SerializedName("created_on")
        var createdOn: String = "",
        @SerializedName("date_of_birth")
        var dateOfBirth: String = "",
        @SerializedName("email")
        @Expose
        var email: String = "",
        @SerializedName("first_name")
        var firstName: String = "",
        @SerializedName("full_name")
        var fullName: String = "",
        @SerializedName("gender")
        var gender: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("language")
        var language: String = "",
        @SerializedName("last_action_time")
        var lastActionTime: String = "",
        @SerializedName("last_name")
        var lastName: String = "",
        @SerializedName("last_password_change")
        var lastPasswordChange: String = "",
        @SerializedName("last_visit_time")
        var lastVisitTime: String = "",
        @SerializedName("login_error_count")
        var loginErrorCount: String = "",
        @SerializedName("otp")
        var otp: Int = 0,
        @SerializedName("is_online")
        var isOnline: Int = 0,
        @SerializedName("otp_verified")
        var otpVerified: Int = 0,
        @SerializedName("profile_file")
        var profileFile: String = "",
        @SerializedName("city")
        var city: String = "",
        @SerializedName("country")
        var country: String = "",
        @SerializedName("address")
        var address: String = "",
        @SerializedName("license_no")
        var license_no: String = "",
        @SerializedName("password")
        var password: String = "",
        @SerializedName("role_id")
        var roleId: Int = 0,
        @SerializedName("state_id")
        var stateId: Int = 0,
        @SerializedName("timezone")
        var timezone: String = "",
        @SerializedName("tos")
        var tos: String = "",
        @SerializedName("access-token")
        var accessToken: String = "",
        @SerializedName("type_id")
        var typeId: Int = 0,
        @SerializedName("is_booking_accept_pending")
        var isBookingAcceptPending: Boolean? = null,
        @SerializedName("is_booking_inprogress")
        var isBookingInprogress: Boolean? = null,
) : Parcelable
