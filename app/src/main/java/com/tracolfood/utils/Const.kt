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

object Const {
//        const val SERVER_REMOTE_URL = "http://dev.toxsl.in/tracol-food-package-delivery-yii2-1479/api/"
//        const val IMAGE_SERVER_URL = "http://192.168.2.135"
//        const val SERVER_REMOTE_URL = "http://192.168.2.135/tracol-food-package-delivery-yii2-1479/api/"

    const val IMAGE_SERVER_URL = "https://tracolasia.com"
    const val SERVER_REMOTE_URL = "https://tracolasia.com/api/"


    const val DISPLAY_MESSAGE_ACTION = "com.tracolfood.DISPLAY_MESSAGE"
    const val UPDATE_TOKEN_ACTION = "com.tracolfood.UPDATE_TOKEN"

    const val PLAY_SERVICES_RESOLUTION_REQUEST = 1234
    const val API_UPDATE_LOCATION = "user/update-current-location"

    const val SPLASH_TIMEOUT = 2000
    const val PERMISSION_ID = 99

    const val STATUS_OK = 200
    const val DEVICE_TOKEN = "device_token"
    const val ANDROID_DEVICE_TYPE = "1"


    object PrefConst {
        const val FOREGROUND = "foreground"
        const val USER_DATA = "user_data"
        const val DEVICE_TOKEN = "device_token"
    }

    object PermissionConst {
        const val IMAGE_CODE = 1234
        const val PERMISSION_LOCATION_CODE = 102
        const val SOURE: Int = 15
    }

    object Drawable {
        const val END = 2
        const val START = 0
    }

    object Login {
        const val API_CHECK = "user/check"
        const val API_LOGIN = "user/login"
        const val API_FORGOT = "user/forget-password"
        const val API_LOGOUT = "user/logout"
        const val API_CHANGE_PASSWORD = "user/change-password"
        const val SIGN_UP_URL = "https://tracolasia.com/signup"
    }


    object Profile {
        const val API_UPDATE_PROFILE = "user/update-profile"
        const val API_PROFILE_IMAGE_REMOVE = "user/match-otp"
    }

    object Videos {
        const val PLAY = 1208
        const val API_VIDEO_LIST = "product/video-list"
    }


    object StaticPage {
        const val TYPE_TERMS = 1
        const val TYPE_PRIVACY = 3
        const val TYPE_ABOUT = 2
        const val API_STATIC_PAGE = "user/get-page"
    }


    object ReportPages {
        const val API_CONTACT_US = "user/contact-us"
    }

    object Notification {
        const val API_NOTIFICATION_LIST = "user/notification-list"
    }

    object Orders {
        const val API_MY_ORDERS = "product/get-order-list"
        const val API_PLACE_ORDER = "product/place-order"
        const val API_TRANSITION_DETAIL = "transactions/order-payment" //?id=
    }


    object HomeApi {
        const val API_PACKAGE_LIST = "product/package-list"
        const val API_TRENDING_VIDEO = "video/get-trending-video" //?id=
    }

    object Fruits {
        const val API_PRODUCT_LIST = "product/product-list"
        const val API_PRODUCT_DETAIL = "product/product-detail"
        const val API_ADD_WISH_LIST = "item/favourite"
    }

    object Charity {
        const val API_CHARITY_AMOUNT = "charity/charity-payment" //?id=
        const val API_CHARITY_DETAIL = "charity/get-charity" //?id=
    }


    const val API_WISH_LIST = "item/favourite-list"
    const val API_PACKAGE_DETAIL = "product/package-detail" //?id=
    const val API_FAVORITE_PACKAGE_LIST = "item/favourite-package-list"
    const val API_FAVORITE_PACKAGE = "item/favourite-package" //?id=&access-token=
    const val API_ADD_ADDRESS = "user/add-address" //?id=
    const val API_UPDATE_ADDRESS = "user/update-address" //?id=
    const val API_SET_DEFAULT_ADDRESS = "user/default-address" //?id=
    const val API_ADDRESS_LIST = "user/get-address" //?id=
    const val API_SEARCH_API = "user/search" //?id=
    const val API_ORDER_DETAIL_API = "product/order-detail" //?id=
    const val API_DELETE_ADDRESS = "user/delete-address" //?id=25&access-control=LqB8MfzyNJ7LlQ1VGe0PTLaIiEFN_sJE_1610522017
    const val CHECK_PROFILE = "user/profile"
    const val API_CHARITY_LIST = "charity/get-charity"

    const val ADD = 40

    const val ADD_PRODUCT = 0
    const val DELETE_PRODUCT = 2
    const val GET_ALL_ITEM = 3
    const val GET_COUNT = 5
    const val UPDATE_QUANTITY = 7
    const val DELETE_ALL = 11
    const val USERNAME = "com.trackol.username"
    const val PASSWORD = "com.trackol.password"

    const val TYPE_PACKAGE = 2
    const val TYPE_PRODUCT = 1

    const val TYPE_EDIT = 1
    const val TYPE_DELETE = 2
    const val TYPE_SELECT = 3

}