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

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.gson.Gson
import com.toxsl.restfulClient.api.*
import com.tracolfood.BuildConfig
import com.tracolfood.R
import com.tracolfood.model.CartProductData
import com.tracolfood.model.UserDetail
import com.tracolfood.room.AppDatabase
import com.tracolfood.room.RoomUtils
import com.tracolfood.snackBar.ActionClickListener
import com.tracolfood.snackBar.Snackbar
import com.tracolfood.snackBar.SnackbarManager
import com.tracolfood.snackBar.SnackbarType
import com.tracolfood.ui.activity.LoginSignUpActivity
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.utils.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


open class BaseActivity : AppCompatActivity(), SyncEventListener, View.OnClickListener, RoomUtils.roomCallbackListener {
    var roomUtils: RoomUtils? = null
    var apiInstance: Retrofit? = null
    var restFullClient: RestFullClient? = null
    var api: API? = null
    private var gpsAlert: AlertDialog? = null

    var inflater: LayoutInflater? = null
    var store: PrefStore? = null
    var permCallback: PermCallback? = null
    private var progressDialog: Dialog? = null
    private var txtMsgTV: TextView? = null
    private var reqCode: Int = 0
    private var networksBroadcast: NetworksBroadcast? = null
    private val networkAlertDialog: AlertDialog? = null
    private var networkStatus: String? = null
    private var inputMethodManager: InputMethodManager? = null
    private var failureDailog: android.app.AlertDialog.Builder? = null
    private var failureAlertDialog: android.app.AlertDialog? = null
    private var exit: Boolean = false
    var googleApisHandle: GoogleApisHandle? = null

    val uniqueDeviceId: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager
                    .activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        restFullClient = RestFullClient.getInstance(this)
        apiInstance = restFullClient!!.getRetrofitInstance(Const.SERVER_REMOTE_URL)
        api = apiInstance!!.create(API::class.java)

        googleApisHandle = GoogleApisHandle.getInstance(this)

        val database: AppDatabase? = AppDatabase.getDatabase(this)
        roomUtils = RoomUtils.instance
        roomUtils!!.setDatabase(database)
        this@BaseActivity.overridePendingTransition(R.anim.slide_in,
                R.anim.slide_out)
        inputMethodManager = this
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        store = PrefStore(this)
        initializeNetworkBroadcast()
        store?.saveString(Const.PrefConst.DEVICE_TOKEN, uniqueDeviceId)
        strictModeThread()
        transitionSlideInHorizontal()
        progressDialog()
        failureDailog = android.app.AlertDialog.Builder(this)

    }


    fun initFCM() {
        if (checkPlayServices()) {
            if (restFullClient?.getLoginStatus() != null) {
                checkApi()
            } else {
                gotoLoginSignUpActivity()
            }
        }
    }

    private fun checkApi() {
        val params = Api3Params()
        params.put("DeviceDetail[device_token]", store?.getString(Const.PrefConst.DEVICE_TOKEN)
                ?: "")
        params.put("DeviceDetail[device_type]", Const.ANDROID_DEVICE_TYPE)
        params.put("DeviceDetail[device_name]", Build.MODEL)
        val call = api!!.apiCheck(params.getServerHashMap())
        restFullClient?.sendRequest(call, this)

    }

    fun gotoLoginSignUpActivity() {
        startActivity(Intent(this, LoginSignUpActivity::class.java))
        finish()
    }

    fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    fun saveProfileData(userData: UserDetail?) {
        store?.saveString("user_data", Gson().toJson(userData))
    }

    fun getProfileData(): UserDetail {
        return Gson().fromJson(store!!.getString("user_data"), UserDetail::class.java)
    }

    private fun initializeNetworkBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        networksBroadcast = NetworksBroadcast()
        registerReceiver(networksBroadcast, intentFilter)
    }

    fun setActionBarTitleInCenter(title: String) {
        val view = inflater!!.inflate(R.layout.custom_action_bar, null)
        val titleTV = view.findViewById<View>(R.id.titleTV) as TextView
        titleTV.text = title

        val params = ActionBar.LayoutParams( //Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(view, params)
    }


    private fun unregisterNetworkBroadcast() {
        try {
            if (networksBroadcast != null) {
                unregisterReceiver(networksBroadcast)
            }
        } catch (e: IllegalArgumentException) {
            networksBroadcast = null
        }

    }

    private fun showNoNetworkDialog(status: String?) {
        networkStatus = status
        if (SnackbarManager.currentSnackbar != null) {
            SnackbarManager.currentSnackbar!!.dismiss()
        }
        SnackbarManager.create(
                Snackbar.with(this)
                        .type(SnackbarType.SINGLE_LINE)
                        .text(status!!).duration(Snackbar
                                .SnackbarDuration.LENGTH_INDEFINITE)
                        .actionLabel(getString(R.string.retry_caps))
                        .actionListener(object : ActionClickListener {
                            override fun onActionClicked(snackbar: Snackbar) {
                                if (!isNetworkAvailable) {
                                    showNoNetworkDialog(networkStatus)
                                } else
                                    SnackbarManager.currentSnackbar!!.dismiss()
                            }
                        }))!!.show()
    }

    fun changeDateFormat(dateString: String?, sourceDateFormat: String, targetDateFormat: String): String {
        if (dateString == null || dateString.isEmpty()) {
            return ""
        }
        val inputDateFromat = SimpleDateFormat(sourceDateFormat, Locale.getDefault())
        var date = Date()
        try {
            date = inputDateFromat.parse(dateString)
        } catch (e: ParseException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }

        val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.getDefault())
        return outputDateFormat.format(date)
    }


    fun isValidMail(email: String): Boolean {
        return email.matches("^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$".toRegex())
    }

    fun changeDateFormatFromDate(sourceDate: Date?, targetDateFormat: String?): String {
        if (sourceDate == null || targetDateFormat == null || targetDateFormat.isEmpty()) {
            return ""
        }
        val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.getDefault())
        return outputDateFormat.format(sourceDate)
    }

    protected fun checkDate(checkDate: String) {
        val cal = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        var serverDate: Date? = null
        try {
            serverDate = dateFormat.parse(checkDate)
            cal.time = serverDate
            val currentcal = Calendar.getInstance()
            if (currentcal.after(cal)) {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomMaterialDialog)
                builder.setMessage(getString(R.string.contact_company_info))
                builder.setTitle(getString(R.string.demo_expired))
                builder.setCancelable(false)
                builder.setNegativeButton(getString(R.string.close)) { _, _ -> exitFromApp() }
                val alert = builder.create()
                alert.show()
            }
        } catch (e: ParseException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }

    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, Const.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show()
            } else {
                log(getString(R.string.this_device_is_not_supported))
                finish()
            }
            return false
        }
        return true
    }

    fun checkPermissions(perms: Array<String>, requestCode: Int, permCallback: PermCallback): Boolean {
        this.permCallback = permCallback
        this.reqCode = requestCode
        val permsArray = ArrayList<String>()
        var hasPerms = true
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                permsArray.add(perm)
                hasPerms = false
            }
        }
        if (!hasPerms) {
            val permsString = arrayOfNulls<String>(permsArray.size)
            for (i in permsArray.indices) {
                permsString[i] = permsArray[i]
            }
            ActivityCompat.requestPermissions(this@BaseActivity, permsString, Const.PERMISSION_ID)
            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        var permGrantedBool = false
        when (requestCode) {
            Const.PERMISSION_ID -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showToast(getString(R.string.not_sufficient_permissions)
                                + getString(R.string.app_name)
                                + getString(R.string.permissionss))
                        permGrantedBool = false
                        break
                    } else {
                        permGrantedBool = true
                    }
                }
                if (permCallback != null) {
                    if (permGrantedBool) {
                        permCallback!!.permGranted(reqCode)
                    } else {
                        permCallback!!.permDenied(reqCode)
                    }
                }
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun exitFromApp() {
        finish()
        finishAffinity()
    }

    fun hideSoftKeyboard(): Boolean {
        try {
            if (currentFocus != null) {
                inputMethodManager!!.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
                return true
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }

        return false
    }


    fun keyHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                if (BuildConfig.DEBUG) {
                    Log.e("KeyHash:>>>>>>>>>>>>>>>", "" + Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        } catch (e: NoSuchAlgorithmException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }

    }

    fun log(string: String) {
        if (BuildConfig.DEBUG) {
            Log.e("BaseActivity", string)
        }
    }


    fun log(title: String, message: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(title, message ?: "")
        }
    }

    private fun progressDialog() {
        progressDialog = Dialog(this)
        val view = View.inflate(this, R.layout.progress_dialog, null)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(view)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        txtMsgTV = view.findViewById<View>(R.id.txtMsgTV) as TextView
        progressDialog!!.setCancelable(false)
    }

    fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }

        }
    }

    fun stopProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }

        }
    }

    override fun onSyncStart() {
        startProgressDialog()
    }

    override fun onSyncFinish() {
        stopProgressDialog()
    }

    open fun errorSnackBar(errorString: String, call: Call<String>?, callBack: Callback<String>?): SnackbarManager? {
        val buttontext: String
        buttontext = if (call != null && callBack != null) {
            getString(R.string.retry_cap)
        } else {
            getString(R.string.exit_caps)
        }
        val snackBar: Snackbar = Snackbar.with(this)
                .type(SnackbarType.MULTI_LINE)
                .text(errorString)
                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .actionLabel(buttontext)
                .actionListener(object : ActionClickListener {
                    override fun onActionClicked(snackbar: Snackbar) {
                        if (call != null && callBack != null) {
                            onSyncStart()
                            call.clone().enqueue(callBack)
                        } else {
                            finish()
                        }
                    }

                })
        return SnackbarManager.create(snackBar)
    }

    override fun onSyncFailure(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        log("error_message", if (response != null) response.message() else "")
        log("error_code", errorCode.toString())
        if (this.isFinishing) return
        if (failureAlertDialog != null && failureAlertDialog!!.isShowing) {
            failureAlertDialog!!.dismiss()
        }
        if (errorCode == HTTPS_RESPONSE_CODE.FORBIDDEN_ERROR || errorCode == HTTPS_RESPONSE_CODE.UN_AUTHORIZATION) {
            log(getString(R.string.error), getString(R.string.session_timeout_redirecting))
            showToast(getString(R.string.session_timeout_redirecting))
            if (roomUtils!=null){
                roomUtils!!.deleteAll(Const.DELETE_ALL, this)
            }
            restFullClient!!.setLoginStatus(null)
            store?.saveProfileData(null)
            gotoLoginSignUpActivity()
            //--------------------------------goToLogin--------------------------
        } else if (errorCode == HTTPS_RESPONSE_CODE.SERVER_ERROR) {
            showToast(getString(R.string.problem_connecting_to_the_server))
        } else if (t is SocketTimeoutException || t is ConnectException) {
            log(getString(R.string.error), getString(R.string.request_timeout_slow_connection))
            errorSnackBar(getString(R.string.request_timeout_slow_connection), call, callBack)!!.show()
        } else if (t is AppInMaintenance) {
            log(getString(R.string.error), getString(R.string.api_is_in_maintenance))
            failureErrorDialog(t.message!!, call, callBack)!!.show()
        } else if (t is AppExpiredError) {
            log(getString(R.string.error), getString(R.string.api_is_expired))
            checkDate(t.message!!)
        }  else {
            if (response != null) response.message() else if (t != null) t.message else "".let { log(getString(R.string.error), it) }
            var message = getString(R.string.problem_connecting_to_the_server)
            try {
                val json = JSONObject(response?.body() ?: response?.errorBody()?.string() ?: "{'message':'$message'}")
                if (json.has("message")) {
                    message = if(json.get("message") is JSONObject) {
                        val keys: Iterator<String> = json.getJSONObject("message").keys()
                        val strName = keys.next()
                        json.getJSONObject("message").optString(strName)
                    }
                    else{
                        json.getString("message")
                    }
                } else if (json.has("error")) {
                    message = if(json.get("error") is JSONObject){
                        val keys: Iterator<String> = json.getJSONObject("error").keys()
                        val strName = keys.next()
                        json.getJSONObject("error").optString(strName)
                    }else{
                        json.getString("error")
                    }
                }
            } catch (e: java.lang.Exception) {
                handelException(e)
            }
            if (message.isNotEmpty()) {
                showToast(message.replace("[", "").replace("\"", "").replace("]", "").replace("\\", ""))
            }
        }
    }


    private fun failureErrorDialog(errorString: String, call: Call<String>?, callBack: Callback<String>?): android.app.AlertDialog? {
        if (call != null && callBack != null) {
            failureDailog!!.setMessage(errorString).setCancelable(false).setNegativeButton(getString(R.string.exit_caps)) { dialog, which -> finish() }.setPositiveButton(getString(R.string.retry_cap)) { dialog, which ->
                onSyncStart()
                call.clone().enqueue(callBack)
            }
        } else failureDailog!!.setMessage(errorString).setCancelable(false).setPositiveButton(getString(R.string.exit_caps)) { dialog, which -> finish() }
        failureAlertDialog = failureDailog!!.create()
        return failureAlertDialog
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        try {
            val respObject = JSONObject(response!!)
            if (responseUrl.contains(Const.Login.API_CHECK)) {
                val data = Gson().fromJson<UserDetail>(respObject.getJSONObject("detail").toString(), UserDetail::class.java)
                store?.saveProfileData(data)
                goToMainActivity()
            }

        } catch (e: JSONException) {
            handelException(e)
        }

    }


    fun createDateFromString(sourceDate: String?, sourceFormat: String = "yyyy-MM-dd"): Date {
        if (sourceDate == null || sourceDate.isEmpty()) {
            return Date()
        }
        val inputDateFromat = SimpleDateFormat(sourceFormat, Locale.getDefault())
        var date = Date()
        try {
            date = inputDateFromat.parse(sourceDate)
        } catch (e: ParseException) {
            handelException(e)
        }

        return date
    }


    fun showToast(msg: String) {
        SnackbarManager.create(
                Snackbar.with(this).duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .type(SnackbarType.MULTI_LINE)
                        .text(msg))!!.show()
    }

    fun showToastOne(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

    }

    private fun strictModeThread() {
        val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    private fun transitionSlideInHorizontal() {
        this.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left)
    }

    override fun onClick(v: View) {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkBroadcast()
    }

    fun backAction() {
        if (exit) {
            finishAffinity()
        } else {
            showToastOne(getString(R.string.press_one_more_time))
            exit = true
            Handler().postDelayed({ exit = false }, (2 * 1000).toLong())
        }
    }

    interface PermCallback {
        fun permGranted(resultCode: Int)

        fun permDenied(resultCode: Int)
    }

    inner class NetworksBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = NetworkUtil.getConnectivityStatusString(context)
            if (status == null && networkAlertDialog != null) {
                networkStatus = null
                networkAlertDialog.dismiss()
            } else if (status != null && networkStatus == null)
                showNoNetworkDialog(status)
        }
    }

    open fun handelException(e: java.lang.Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
    }


    fun setFocus(editText: EditText?) {
        if (editText == null) return
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
    }

    fun showSoftKeyboard(editText: EditText?) {
        if (editText == null) return
        val imm = getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
    }

    fun hideSoftKeyboard(editText: EditText?) {
        if (editText == null) {
            return
        }
        val im = getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun buildAlertMessageNoGps() {
        if (gpsAlert != null && gpsAlert!!.isShowing) {
            gpsAlert!!.dismiss()
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.your_gps_seems_to_be_disable))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    dialog.dismiss()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }.setNegativeButton(getString(R.string.no)) { dialog, which ->
                    dialog.dismiss()
                }
        gpsAlert = builder.create()
        gpsAlert!!.show()
        gpsAlert!!.getButton(AlertDialog.BUTTON_NEGATIVE).background = ContextCompat.getDrawable(this, R.color.transparent)
        gpsAlert!!.getButton(AlertDialog.BUTTON_POSITIVE).background = ContextCompat.getDrawable(this, R.color.transparent)
        gpsAlert!!.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        gpsAlert!!.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }


    open fun bitmapToFile(bitmap: Bitmap, activity: Activity): File? {
        val f = File(activity.getCacheDir(), System.currentTimeMillis().toString() + ".jpg")
        try {
            f.createNewFile()
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
            val bitmapdata: ByteArray = bos.toByteArray()
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (ioexception: IOException) {
            ioexception.printStackTrace()
        }
        return f
    }

    open fun setBadgeCount(context: Context?, icon: LayerDrawable, count: String?) {
        val badge: BadgeDrawable
        // Reuse drawable if possible
        val reuse: Drawable = icon.findDrawableByLayerId(R.id.ic_badge)
        badge = if (reuse != null && reuse is BadgeDrawable) {
            reuse as BadgeDrawable
        } else {
            BadgeDrawable(context!!)
        }
        badge.setCount(count!!)
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_badge, badge)
    }

    override fun cartData(data: CartProductData?) {
    }

    override fun onSuccess() {
    }

    override fun onDataReceived(vararg objects: Any?) {
    }


}
