/*
 *   @copyright : ToXSL Technologies Pvt. Ltd. < www.toxsl.com >
 *   @author     : Shiv Charan Panjeta < shiv@toxsl.com >
 *
 *   All Rights Reserved.
 *   Proprietary and confidential :  All information contained herein is, and remains
 *    the property of ToXSL Technologies Pvt. Ltd. and its partners.
 *   Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.utils

import com.google.android.gms.maps.model.LatLng

class SendMapData {
    private var setData: MySetDataListener? = null

    fun sendData(latLong: LatLng, address: String, isDrop: Boolean) {
        if (setData != null) {
            setData!!.getAvailablityData(latLong, address, isDrop)
        }
    }

    fun setDataListener(myScannerListener: MySetDataListener) {
        this.setData = myScannerListener
    }

    interface MySetDataListener {
        fun getAvailablityData(latLong: LatLng, address: String, isDrop: Boolean)
    }

    companion object {
        val instance = SendMapData()
    }

}
