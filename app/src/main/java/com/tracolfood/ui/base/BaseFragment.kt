/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.base

import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.tracolfood.utils.API
import com.tracolfood.utils.Const
import com.tracolfood.utils.PrefStore
import com.toxsl.restfulClient.api.RestFullClient
import com.toxsl.restfulClient.api.SyncEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import toxsl.imagebottompicker.ImageBottomPicker


open class BaseFragment : Fragment(), AdapterView.OnItemClickListener, View.OnClickListener, SyncEventListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener
        , ImageBottomPicker.OnImageSelectedListener, ImageBottomPicker.OnErrorListener {

    var baseActivity: BaseActivity? = null
    var store: PrefStore? = null
    var restFullClient: RestFullClient? = null
    var api: API? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity?
        if (baseActivity!!.restFullClient == null) {
            baseActivity!!.restFullClient = RestFullClient.getInstance(baseActivity!!)
            baseActivity!!.apiInstance = restFullClient!!.getRetrofitInstance(Const.SERVER_REMOTE_URL)
            baseActivity!!.api = baseActivity!!.apiInstance!!.create(API::class.java)

        }
        restFullClient = baseActivity!!.restFullClient
        api = baseActivity!!.api
        store = baseActivity!!.store

    }

    override fun onResume() {
        super.onResume()
        baseActivity!!.hideSoftKeyboard()
        requireActivity().invalidateOptionsMenu()
    }

    override fun onClick(v: View) {

    }

    fun showToast(msg: String) {
        baseActivity!!.showToast(msg)
    }

    fun showToastOne(s: String) {
        baseActivity!!.showToastOne(s)
    }

    override fun onSyncStart() {
        baseActivity!!.onSyncStart()
    }

    override fun onSyncFinish() {
        baseActivity!!.onSyncFinish()
    }

    override fun onSyncFailure(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        baseActivity!!.onSyncFailure(errorCode, t, response, call, callBack)
    }

    fun log(s: String) {
        baseActivity!!.log(s)
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

    }

    fun chooseImagePicker(requestCode: Int, showRemove: Boolean, crop: Boolean = true) {
        val bottomPicker = ImageBottomPicker.Builder(baseActivity!!, requestCode)
                .setOnImageSelectedListener(this).setOnErrorListener(this).showRemoved(showRemove)
                .setCrop(crop)
                .create()
        bottomPicker.show(baseActivity?.supportFragmentManager)
    }

    override fun onImageSelected(uri: Uri?, requestCode: Int) {

    }

    override fun onImageRemoved(isRemoved: Boolean, requestCode: Int) {
    }


    fun setCameraOption(mLocation: Location, mMap: GoogleMap) {
        val cameraPosition = CameraPosition.Builder()
                .target(LatLng(mLocation.latitude, mLocation.longitude)) // Sets the center of the map to location user
                .zoom(15f) // Sets the zoom
                .build() // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onError(message: String?) {
    }

}
