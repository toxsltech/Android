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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentNotificationBinding
import com.tracolfood.model.NotificationData
import com.tracolfood.ui.adapter.NotificationAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.visibleView
import org.json.JSONObject
import java.util.*


class NotificationFragment : BaseFragment(), BaseAdapter.OnPageEndListener {

    private var adapter: NotificationAdapter? = null
    private var notificationList: ArrayList<NotificationData>? = ArrayList()
    private var binding: FragmentNotificationBinding? = null
    private var isSingleHit: Boolean = false
    private var pageCount: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearData()
        hitNotificationList()
    }


    private fun hitNotificationList() {
        if (!isSingleHit) {
            val call = api!!.apiNotificationList(pageCount)
            restFullClient!!.sendRequest(call, this)
        }
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.Notification.API_NOTIFICATION_LIST)) {
                if (responseCode == Const.STATUS_OK) {
                    pageCount++
                    val metaJsom = jsonObject.getJSONObject("_meta")
                    val totalPage = metaJsom.getInt("pageCount")
                    isSingleHit = pageCount >= totalPage
                    val listJsonArray = jsonObject.getJSONArray("list")
                    if (listJsonArray.length() > 0) {
                        for (i in 0 until listJsonArray.length()) {
                            val json = listJsonArray.getJSONObject(i)
                            val data = Gson().fromJson(json.toString(), NotificationData::class.java)
                            notificationList!!.add(data)
                        }
                        binding!!.noDataTV.visibleView(false)
                        binding!!.listRV.visibleView(true)
                    } else {
                        binding!!.noDataTV.visibleView(true)
                        binding!!.listRV.visibleView(false)
                    }
                    setAdapter()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setAdapter() {
        if (adapter == null) {
            adapter = NotificationAdapter(notificationList!!,baseActivity!!)
            adapter!!.setOnPageEndListener(this)
            binding!!.listRV.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }

    }

    private fun clearData() {
        isSingleHit = false
        pageCount = 0
        notificationList!!.clear()
        adapter = null
    }



    override fun onPageEnd(vararg itemData: Any) {
        hitNotificationList()
    }
}
