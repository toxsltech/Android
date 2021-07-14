/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.fragment.pages

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.FragmentPrivacyBinding
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.setHtmlData
import org.json.JSONObject

class PrivacyFragment : BaseFragment(), ViewClickHandler {
    private lateinit var binding: FragmentPrivacyBinding
    private var typeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            typeId = requireArguments().getInt("typeId")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_privacy, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.clickHandle = this
        binding.nameTV.movementMethod = ScrollingMovementMethod()
        if (arguments != null) {
            if (requireArguments().containsKey("title")) {
                binding.termsTV.text = arguments?.getString("title")
            }
            if (requireArguments().containsKey("isPayment")) {
                (baseActivity as MainActivity).setToolBar(false, isShow = false)
                binding.topRL.visibility = View.VISIBLE
            } else {
                (baseActivity as MainActivity).setToolBar(true, isShow = false)
                binding.topRL.visibility = View.GONE
            }
        }
        hitPageAPI(typeId)
    }

    private fun hitPageAPI(pageType: Int) {
        val call = api!!.apiGetStaticPage(pageType)
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            when (responseUrl) {

                Const.StaticPage.API_STATIC_PAGE -> when (responseCode) {
                    Const.STATUS_OK -> binding.nameTV.setHtmlData(jsonObject.getJSONObject("detail").getString("description"))
                    else -> showToastOne(jsonObject.getString("error"))
                }
            }
        } catch (e: Exception) {
            baseActivity!!.handelException(e)
        }
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.backIV -> {
                baseActivity!!.supportFragmentManager.popBackStack()
            }
        }
    }

}
