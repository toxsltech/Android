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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.toxsl.restfulClient.api.Api3Params
import com.tracolfood.BuildConfig
import com.tracolfood.R
import com.tracolfood.databinding.FragmentForgotPasswordBinding
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.checkString
import com.tracolfood.utils.extensions.isBlank
import org.json.JSONObject

class ForgotPasswordFragment : BaseFragment(), ViewClickHandler {
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandle = this
        binding.emailET.requestFocus()
        binding.submitBT.setOnClickListener(this)
    }


    private fun isValid(): Boolean {
        when {
            binding.emailET.isBlank() -> baseActivity!!.showToastOne(baseActivity!!.getString(R.string.please_enter_email))
            !baseActivity!!.isValidMail(binding.emailET.checkString()) -> baseActivity!!.showToastOne(baseActivity!!.getString(R.string.please_enter_valid_email))
            else -> return true
        }
        return false
    }

    private fun hitApiForgotPassword() {
        val params = Api3Params()
        params.put("User[email]", binding.emailET.checkString())
        val call = api!!.apiForgotPassWord(params.getServerHashMap())
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)

        val jsonObject = JSONObject(response!!)
        try {
            when (responseUrl) {
                Const.Login.API_FORGOT -> {
                    if (responseCode == Const.STATUS_OK) {
                        showToastOne(jsonObject.getString("message"))
                        baseActivity!!.onBackPressed()
                    } else {
                        showToastOne(jsonObject.getString("message"))
                    }
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }
            R.id.submitBT -> {
                if (isValid()) {
                    hitApiForgotPassword()
                }
            }
        }
    }

}
