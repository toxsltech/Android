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

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface API {
    @FormUrlEncoded
    @POST(Const.API_UPDATE_LOCATION)
    fun locationUpdate(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.Login.API_CHECK)
    fun apiCheck(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.Login.API_LOGIN)
    fun apiLogin(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.Login.API_FORGOT)
    fun apiForgotPassWord(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.Login.API_CHANGE_PASSWORD)
    fun apiChangePassword(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @POST(Const.Profile.API_UPDATE_PROFILE)
    fun apiUpdateProfile(@Body serverHashMap: RequestBody): Call<String>

    @POST(Const.Profile.API_PROFILE_IMAGE_REMOVE)
    fun apiProfileImageRemove(): Call<String>

    @GET(Const.Login.API_LOGOUT)
    fun apiLogout(): Call<String>

    /*Static View Regarding Apis*/

    @GET(Const.StaticPage.API_STATIC_PAGE)
    fun apiGetStaticPage(@Query("type_id") typeId: Int): Call<String>

    @FormUrlEncoded
    @POST(Const.ReportPages.API_CONTACT_US)
    fun apiContactUs(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @GET(Const.Notification.API_NOTIFICATION_LIST)
    fun apiNotificationList(@Query("page") page: Int): Call<String>


    @GET(Const.Orders.API_MY_ORDERS)
    fun apiOrder(@Query("page") page: Int): Call<String>

    @GET(Const.HomeApi.API_PACKAGE_LIST)
    fun apiPackageList(@Query("page") page: Int): Call<String>

    @GET(Const.Fruits.API_PRODUCT_LIST)
    fun apiProductList(): Call<String>

    @GET(Const.Fruits.API_PRODUCT_DETAIL)
    fun apiProductDetail(@Query("id") type: Int): Call<String>

    // video api
    @GET(Const.Videos.API_VIDEO_LIST)
    fun apiVideoList(@Query("page") page: Int): Call<String>

    @FormUrlEncoded
    @POST(Const.API_FAVORITE_PACKAGE)
    fun apiFavoritePackage(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>


    @GET(Const.API_PACKAGE_DETAIL)
    fun apiPackageDetail(@Query("id") packageId: Int): Call<String>

    @GET(Const.API_WISH_LIST)
    fun apiWishList(@Query("page") page: Int): Call<String>

    @GET(Const.API_FAVORITE_PACKAGE_LIST)
    fun apiFavPackageList(@Query("page") page: Int): Call<String>

    @GET(Const.HomeApi.API_TRENDING_VIDEO)
    fun apiTrendingVideo(@Query("page") page: Int): Call<String>

    @GET(Const.API_SET_DEFAULT_ADDRESS)
    fun apiSetDefaultAddress(@Query("id") id: Int): Call<String>

    @GET(Const.API_ADDRESS_LIST)
    fun apiAddressList(@Query("page") page: Int): Call<String>

    @GET(Const.API_SEARCH_API)
    fun apiSearchApi(@Query("search") searchText: String): Call<String>


    @GET(Const.API_DELETE_ADDRESS)
    fun apiDeleteAddress(@Query("id") addressId: Int): Call<String>

    @GET(Const.API_ORDER_DETAIL_API)
    fun apiOrderDetail(@Query("id") orderId: Int): Call<String>

    @GET(Const.Charity.API_CHARITY_DETAIL)
    fun apiCharityDetail(): Call<String>

    @FormUrlEncoded
    @POST(Const.Orders.API_PLACE_ORDER)
    fun apiPlaceOrder(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.Orders.API_TRANSITION_DETAIL)
    fun apiTransitionDetail(@FieldMap serverHashMap: HashMap<String,Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.Charity.API_CHARITY_AMOUNT)
    fun apiCharityAmount(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.Fruits.API_ADD_WISH_LIST)
    fun apiHitAddToWishList(@FieldMap serverHashMap: HashMap<String,Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.API_ADD_ADDRESS)
    fun apiAddAddress(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @GET(Const.API_CHARITY_LIST)
    fun apiCharityList(@Query("page") page: Int): Call<String>

    @FormUrlEncoded
    @POST(Const.API_UPDATE_ADDRESS)
    fun apiUpdateAddress(@Query("id") addressId: Int, @FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>


}