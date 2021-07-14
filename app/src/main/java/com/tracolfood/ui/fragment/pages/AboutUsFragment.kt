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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.FragmentAboutUsBinding
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.setHtmlData
import org.json.JSONObject

class AboutUsFragment : BaseFragment() {

    private lateinit var binding: FragmentAboutUsBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).setToolBar(isShowToolbar = true, isShow = false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        val call = api!!.apiGetStaticPage(Const.StaticPage.TYPE_ABOUT)
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            when (responseUrl) {

                Const.StaticPage.API_STATIC_PAGE -> when (responseCode) {
                    Const.STATUS_OK -> binding.contentTV.setHtmlData(jsonObject.getJSONObject("detail").getString("description"))
                    else -> showToastOne(jsonObject.getString("error"))
                }
            }
        } catch (e: java.lang.Exception) {
            baseActivity!!.handelException(e)
        }
    }
}
