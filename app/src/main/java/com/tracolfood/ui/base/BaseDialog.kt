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

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import com.tracolfood.R
import com.tracolfood.utils.API
import com.tracolfood.utils.Const
import com.tracolfood.utils.PrefStore
import com.toxsl.restfulClient.api.RestFullClient
import com.toxsl.restfulClient.api.SyncEventListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class BaseDialog : Dialog, View.OnClickListener, SyncEventListener, CompoundButton.OnCheckedChangeListener {
    var baseActivity: BaseActivity?
    var store: PrefStore? = null
    var restFullClient: RestFullClient? = null
    var api: API? = null

    constructor(context: Context) : super(context, R.style.animateDialog) {
        baseActivity = context as BaseActivity
        store = baseActivity?.store
        if (baseActivity!!.restFullClient == null) {
            baseActivity!!.restFullClient = RestFullClient.getInstance(baseActivity!!)
            baseActivity!!.apiInstance = restFullClient!!.getRetrofitInstance(Const.SERVER_REMOTE_URL)
            baseActivity!!.api = baseActivity!!.apiInstance!!.create(API::class.java)

        }
        restFullClient = baseActivity!!.restFullClient
        api = baseActivity!!.api
    }

    constructor(context: BaseActivity, customDialog: Int) : super(context, customDialog) {
        baseActivity = context
        store = baseActivity?.store
        if (baseActivity!!.restFullClient == null) {
            baseActivity!!.restFullClient = RestFullClient.getInstance(baseActivity!!)
            baseActivity!!.apiInstance = restFullClient!!.getRetrofitInstance(Const.SERVER_REMOTE_URL)
            baseActivity!!.api = baseActivity!!.apiInstance!!.create(API::class.java)

        }
        restFullClient = baseActivity!!.restFullClient
        api = baseActivity!!.api
    }


    override fun onClick(v: View) {
        hideKeyBoardDialog()
    }

    //hide keyboard on click outside the edittext
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = baseActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun hideKeyBoardDialog() {
        val im = baseActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (im != null && window != null) {
            im.hideSoftInputFromWindow(window?.decorView?.windowToken, 0)
        }
    }

    override fun onSyncStart() {
        baseActivity?.onSyncStart()
    }

    override fun onSyncFinish() {
        baseActivity?.onSyncFinish()
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {

    }

    override fun onSyncFailure(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        baseActivity!!.onSyncFailure(errorCode, t, response, call, callBack)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

    }


    fun showToast(msg: String) {
        baseActivity?.showToast(msg)
    }

    fun showToastOne(s: String) {
        baseActivity?.showToastOne(s)
    }


    fun setOnDialogClickListener(onDialogClickListener: OnDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener
    }

    private var onDialogClickListener: OnDialogClickListener? = null

    fun onDialogClick(vararg itemData: Any) {
        if (onDialogClickListener != null) {
            onDialogClickListener!!.onDialogClick(*itemData)
        }
    }

    interface OnDialogClickListener {
        fun onDialogClick(vararg itemData: Any)
    }

    fun errorHandle(jsonObject: JSONObject) {
        if (jsonObject.has("error")) {
            showToastOne(jsonObject.getString("error"))
        }
    }
}

