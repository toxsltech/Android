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
import com.tracolfood.databinding.FragmentChangePasswordBinding
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.checkString
import org.json.JSONObject

class ChangePasswordFragment : BaseFragment(), ViewClickHandler {
    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).setToolBar(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.clickHandle = this
        binding.passwordET.requestFocus()
    }


    private fun valid(): Boolean {
        if (binding.passwordET.text!!.isEmpty()) {
            binding.passwordET.requestFocus()
            binding.passwordET.error = (getString(R.string.please_enter_the_new_password))
        } else if (binding.passwordET.text.toString().trim().length < 6) {
            binding.passwordET.requestFocus()
            binding.passwordET.error = (baseActivity!!.getString(R.string.please_enter_valid_password))

        } else if (binding.confirmPasswordET.text!!.isEmpty()) {
            binding.confirmPasswordET.requestFocus()
            binding.confirmPasswordET.error = (getString(R.string.please_enter_the_confirm_password))

        } else if (!binding.passwordET.text.toString().trim().equals(binding.confirmPasswordET.text.toString().trim())) {
            binding.confirmPasswordET.requestFocus()
            binding.confirmPasswordET.error = (baseActivity!!.getString(R.string.password_not_match))
        } else {
            return true
        }

        return false
    }


    private fun hitChangePasswordAPI() {
        val params = Api3Params()
        params.put("User[newPassword]", binding.passwordET.checkString())
        params.put("User[confirm_password]", binding.confirmPasswordET.checkString())
        val call = api!!.apiChangePassword(params.getServerHashMap())
        baseActivity!!.restFullClient!!.sendRequest(call, this)


    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)

        try {
            val jsonObject = JSONObject(response!!)
            when {
                responseUrl == Const.Login.API_CHANGE_PASSWORD -> {
                    if (responseCode == Const.STATUS_OK) {
                        showToastOne(jsonObject.getString("message"))
                        baseActivity!!.saveProfileData(null)
                        restFullClient!!.setLoginStatus(null)
                        baseActivity!!.gotoLoginSignUpActivity()
                    } else {
                        showToast(jsonObject.getString("Message"))
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
            R.id.saveBT -> {
                if (valid()) {
                    hitChangePasswordAPI()
                }
            }
            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }
        }
    }

}
