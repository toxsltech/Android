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

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentMyOrderBinding
import com.tracolfood.model.OrderData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.OrderAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.handleException
import org.json.JSONException
import org.json.JSONObject


class MyOrderFragment : BaseFragment(), BaseAdapter.OnPageEndListener {
    private lateinit var binding: FragmentMyOrderBinding
    private var singleHit = false
    private var pageCount = 0
    private var adapter: OrderAdapter? = null
    private var orderList: ArrayList<OrderData> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(isShow = false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }


    private fun apiOrderList() {
        if (!singleHit) {
            val call = api!!.apiOrder(pageCount)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)

            if (responseUrl.contains(Const.Orders.API_MY_ORDERS)) {
                if (responseCode == Const.STATUS_OK) {
                    for (i in 0 until jsonObject.getJSONArray("list").length()) {
                        val orderData = Gson().fromJson(jsonObject.getJSONArray("list").getJSONObject(i).toString(), OrderData::class.java)
                        orderList.add(orderData)
                    }
                    pageCount++
                    singleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                    setAdapter()
                }
            }

        } catch (e: JSONException) {
            handleException(e)
        }
    }

    private fun setAdapter() {
        if (adapter == null) {
            adapter = OrderAdapter(baseActivity!!, orderList)
            adapter!!.setOnPageEndListener(this)
            binding.myOrderRV.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun initUI() {
        binding.myOrderRV.layoutManager = LinearLayoutManager(baseActivity!!)
        clearData()
        apiOrderList()
    }

    private fun clearData() {
        pageCount = 0
        singleHit = false
        orderList.clear()
        adapter = null
    }

    override fun onPageEnd(vararg itemData: Any) {
        apiOrderList()
    }

}