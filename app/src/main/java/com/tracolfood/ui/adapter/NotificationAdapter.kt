/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.AdapterNotificationBinding
import com.tracolfood.model.NotificationData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.ui.fragment.OrderDetailFragment
import com.tracolfood.utils.extensions.replaceFragWithArgs
import java.util.*

class NotificationAdapter(private val notificationList: ArrayList<NotificationData>, val baseActivity: BaseActivity) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterNotificationBinding>(LayoutInflater.from(parent.context), R.layout.adapter_notification, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterNotificationBinding
        val data = notificationList[holder.adapterPosition]
        binding.notifyTV.text = data.title
        binding.dateTV.text = data.createdOn
        binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("orderId", notificationList[position].modelId!!)
            baseActivity.replaceFragWithArgs(OrderDetailFragment(), args = bundle)
        }

    }


}
