/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentSearchBinding
import com.tracolfood.model.ProductData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.WishListAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.checkString
import com.tracolfood.utils.extensions.handleException
import com.tracolfood.utils.extensions.visibleView
import org.json.JSONException
import org.json.JSONObject


class SearchFragment : BaseFragment(), ViewClickHandler {
    private var binding: FragmentSearchBinding? = null
    private var searchedText = ""
    private var singleHit = false
    private var pageCount = 0
    private var packageList: ArrayList<ProductData> = ArrayList()
    private var adapter: WishListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true, "")
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
            initUI()
        }
        return binding!!.root
    }

    private fun initUI() {
        binding!!.clickHandle = this
        clearData()
        hitSearchApi(searchedText)

        binding!!.searchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    clearData()
                    searchedText = binding!!.searchET.checkString()
                    hitSearchApi(searchedText)
                } else {
                    clearData()
                    hitSearchApi(searchedText)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

        })


//        binding!!.searchET.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                clearData()
//                searchedText = v.text.toString().trim()
//                binding!!.crossIV.visibleView(searchedText.isNotBlank())
//                hitSearchApi(searchedText)
//                true
//            } else {
//                false
//            }
//        }
    }

    private fun clearData() {
        searchedText = ""
        packageList.clear()
        pageCount = 0
        singleHit = false
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.API_SEARCH_API)) {
                if (responseCode == Const.STATUS_OK) {
                    if (jsonObject.getJSONObject("_meta").getInt("currentPage") <= 1) {
                        pageCount = 0
                        packageList.clear()
                    }
                    for (i in 0 until jsonObject.getJSONArray("list").length()) {
                        val packageData = Gson().fromJson(jsonObject.getJSONArray("list").getJSONObject(i).toString(), ProductData::class.java)
                        packageList.add(packageData)
                    }
                    pageCount++
                    singleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                    setPackageAdapter()
                    binding!!.noDataTV.visibleView(packageList.size == 0)
                }
            }
        } catch (e: JSONException) {
            handleException(e)
            baseActivity!!.stopProgressDialog()
        }
    }

    private fun setPackageAdapter() {
        if (adapter == null) {
            adapter = WishListAdapter(baseActivity!!, packageList)
            binding!!.itemRV.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onSyncStart() {

    }


    private fun hitSearchApi(s: String) {
        if (!singleHit) {
            val call = api!!.apiSearchApi(s)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.crossIV -> {
                binding!!.crossIV.visibleView(false)
                binding!!.searchET.setText("")
                clearData()
//                hitSearchApi(s)
            }
        }
    }

}