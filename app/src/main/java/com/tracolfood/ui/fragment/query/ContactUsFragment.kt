/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.fragment.query

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.toxsl.restfulClient.api.Api3Params
import com.tracolfood.BuildConfig
import com.tracolfood.R
import com.tracolfood.databinding.FragmentContactUsBinding
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.ui.fragment.HomeFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.checkString
import com.tracolfood.utils.extensions.isBlank
import com.tracolfood.utils.extensions.replaceFragmentWithoutStack
import org.json.JSONObject

class ContactUsFragment : BaseFragment() {
    private var binding: FragmentContactUsBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).setToolBar(isShow = false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding!!.nameET.requestFocus()
        binding!!.sendBT.setOnClickListener(this)
        setData()
    }

    private fun setData() {
        if (!baseActivity!!.getProfileData().fullName.isBlank()) {
            binding!!.nameET.setText(baseActivity!!.getProfileData().fullName)
            binding!!.emailET.setText(baseActivity!!.getProfileData().email)
        }

    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.sendBT -> {
                if (isValid()) {
                    contactUsAPI()
                }
            }
        }
    }

    private fun isValid(): Boolean {
        when {
            binding!!.nameET.isBlank() -> {
                binding!!.nameET.requestFocus()
                binding!!.nameET.error = baseActivity!!.getString(R.string.please_enter_the_name)
            }
            binding!!.emailET.isBlank() -> {
                binding!!.emailET.requestFocus()
                binding!!.emailET.error = baseActivity!!.getString(R.string.please_enter_email)
            }
            !baseActivity!!.isValidMail(binding!!.emailET.checkString()) -> {
                binding!!.emailET.error = baseActivity!!.getString(R.string.please_enter_valid_email)
            }
            binding!!.messageET.isBlank() -> {
                binding!!.messageET.requestFocus()
                binding!!.messageET.error = baseActivity!!.getString(R.string.please_enter_the_message)
            }
            else -> {
                return true
            }
        }

        return false
    }


    private fun contactUsAPI() {
        val params = Api3Params()
        params.put("Information[full_name]", binding!!.nameET.text.toString().trim())
        params.put("Information[email]", binding!!.emailET.text.toString().trim())
        params.put("Information[message]", binding!!.messageET.text.toString().trim())
        val call = api!!.apiContactUs(params.getServerHashMap())
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        val jsonObject = JSONObject(response!!)
        try {
            when (responseUrl) {
                Const.ReportPages.API_CONTACT_US -> {
                    when (responseCode) {
                        Const.STATUS_OK -> {
                            binding!!.nameET.setText("")
                            binding!!.emailET.setText("")
                            binding!!.messageET.setText("")
                            showToastOne(jsonObject.getString("message"))
                            baseActivity!!.replaceFragmentWithoutStack(HomeFragment())
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
}
