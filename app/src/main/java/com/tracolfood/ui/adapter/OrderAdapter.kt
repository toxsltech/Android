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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.tracolfood.R
import com.tracolfood.databinding.AdapterOrderBinding
import com.tracolfood.model.OrderData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.ui.fragment.OrderDetailFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.loadImageUrl
import com.tracolfood.utils.extensions.replaceFragWithArgs

class OrderAdapter(val baseActivity: BaseActivity, private val orderList: ArrayList<OrderData>) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterOrderBinding>(LayoutInflater.from(parent.context), R.layout.adapter_order, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterOrderBinding
        binding.dateTV.text = baseActivity.changeDateFormat(orderList[position].createdOn, "yyyy-MM-dd HH:mm:ss", "dd MMM | h:mm a")
        if (orderList[position].paymentStatus != null && orderList[position].paymentStatus!! > 0) {
            binding.stateTV.text = baseActivity.getString(R.string.paid)
            binding.stateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color.Green))
        } else {
            binding.stateTV.text = baseActivity.getString(R.string.un_paid)
            binding.stateTV.setTextColor(ContextCompat.getColor(baseActivity, R.color.Red))
        }
        if (orderList[position].items!!.size > 0) {
            val builder = StringBuilder()
            for (i in 0 until orderList[position].items!!.size) {
                builder.append(",").append(orderList[position].items!![i].product!!.title)
                val selectedId: String = builder.toString()
                binding.itemNameTV.text = setTextSelection(selectedId)
            }
            binding.orderIV.loadImageUrl(orderList[position].items!![0].product!!.imageFile,R.mipmap.ic_youtube)
            binding.orderIdTV.text = baseActivity.getString(R.string.order_id).plus(orderList[position].items!![0].orderId!!)
        }
        binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("orderId", orderList[position].id!!)
            baseActivity.replaceFragWithArgs(OrderDetailFragment(), args = bundle)
        }
    }

    private fun setTextSelection(selectedId: String): String {
        return if (selectedId.length > 1) {
            selectedId.substring(1)
        } else {
            selectedId
        }
    }


}
