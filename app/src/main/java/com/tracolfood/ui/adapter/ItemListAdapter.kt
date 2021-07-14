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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.AdapterCheckoutItemBinding
import com.tracolfood.model.ItemData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder

class ItemListAdapter(val baseActivity: BaseActivity, private val productData: ArrayList<ItemData>) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterCheckoutItemBinding>(LayoutInflater.from(parent.context), R.layout.adapter_checkout_item, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productData.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterCheckoutItemBinding
        val data = productData[holder.adapterPosition]
        binding.itemNameTV.text = data.product!!.title
        binding.quantityTV.text = data.amount.plus(" x ").plus(data.quantity.toString())

    }


}
