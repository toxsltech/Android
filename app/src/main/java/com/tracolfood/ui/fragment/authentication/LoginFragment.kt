/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.fragment.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.toxsl.restfulClient.api.Api3Params
import com.tracolfood.BuildConfig
import com.tracolfood.R
import com.tracolfood.databinding.FragmentLoginBinding
import com.tracolfood.model.UserDetail
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.checkString
import com.tracolfood.utils.extensions.isBlank
import com.tracolfood.utils.extensions.replaceFragWithArgs
import com.tracolfood.utils.extensions.replaceFragment
import org.json.JSONObject

class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

    }

    private fun isValid(): Boolean {
        if (binding.emailET.isBlank()) {
            baseActivity!!.showToastOne(baseActivity!!.getString(R.string.please_enter_email))
        } else if (!baseActivity!!.isValidMail(binding.emailET.checkString())) {
            baseActivity!!.showToastOne(baseActivity!!.getString(R.string.please_enter_valid_email))
        } else if (binding.passwordET.isBlank()) {
            baseActivity!!.showToastOne(baseActivity!!.getString(R.string.please_enter_password))
        } else if (binding.passwordET.text.toString().length < 6) {
            baseActivity!!.showToastOne(baseActivity!!.getString(R.string.password_should_be_atleast_six_characters))
        } else {
            return true
        }

        return false
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        val signIn = resources.getString(R.string.don_t_have_an_account_sign_up)
        val spanStr = SpannableString(signIn)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val bundle = Bundle()
                bundle.putString("url", Const.Login.SIGN_UP_URL)
                baseActivity!!.replaceFragWithArgs(SignUpFragment(), args = bundle)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(baseActivity!!, R.color.colorPrimary)
            }
        }
        spanStr.setSpan(clickableSpan, 24, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanStr.setSpan(StyleSpan(Typeface.BOLD), 24, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanStr.setSpan(RelativeSizeSpan(1f), 24, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signUpTV.text = spanStr
        binding.signUpTV.movementMethod = LinkMovementMethod.getInstance()
        binding.signUpTV.highlightColor = ContextCompat.getColor(baseActivity!!, R.color.transparent)

        getSharedPref()
        binding.forPasswordTV.setOnClickListener(this)
        binding.loginBT.setOnClickListener(this)
        if (BuildConfig.DEBUG) {
            binding.emailET.setText("abhi@g.c")
            binding.passwordET.setText("admin123")
        }
    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.forPasswordTV -> baseActivity?.replaceFragment(ForgotPasswordFragment())
            R.id.loginBT -> {
                rememberCheck()
                if (isValid()) {
                    loginAPI()
                }
            }
        }
    }


    private fun loginAPI() {
        val params = Api3Params()
        params.put("LoginForm[username]", binding.emailET.checkString())
        params.put("LoginForm[password]", binding.passwordET.checkString())
        params.put("LoginForm[device_type]", Const.ANDROID_DEVICE_TYPE)
        params.put("LoginForm[device_token]", baseActivity!!.store!!.getString(Const.DEVICE_TOKEN)!!)
        params.put("LoginForm[device_name]", Build.MANUFACTURER + "/" + Build.MODEL)
        val call = api!!.apiLogin(params.getServerHashMap())
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)

        try {
            val jsonObject = JSONObject(response!!)
            when (responseUrl) {
                Const.Login.API_LOGIN -> {
                    when (responseCode) {
                        Const.STATUS_OK -> {
                            val userData = Gson().fromJson(jsonObject.getJSONObject("User-Detail").toString(), UserDetail::class.java)
                            userData.accessToken = jsonObject.getString("access-token")
                            restFullClient!!.setLoginStatus(userData.accessToken)
                            baseActivity!!.saveProfileData(userData)
                            gotoMainActivity()
                        }
                        else -> {
                            showToastOne(jsonObject.getString("message"))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }


    private fun setSharedPreference() {
        store!!.saveString(Const.USERNAME, binding.emailET.checkString())
        store!!.saveString(Const.PASSWORD, binding.passwordET.checkString())
    }


    private fun rememberCheck() {
        when {
            binding.rememberMeCB.isChecked -> setSharedPreference()
            else -> cleanPref()
        }
    }

    private fun cleanPref() {
        store!!.cleanKeyPref(Const.USERNAME)
        store!!.cleanKeyPref(Const.PASSWORD)
    }


    private fun getSharedPref() {
        val user = store!!.getString(Const.USERNAME, "")
        val pass = store!!.getString(Const.PASSWORD, "")

        binding.emailET.setText(user)
        binding.passwordET.setText(pass)

        binding.rememberMeCB.isChecked = user!! != ""
    }


    private fun gotoMainActivity() {
        val intent = Intent(baseActivity!!, MainActivity::class.java)
        startActivity(intent)
        baseActivity!!.finish()
    }


}
