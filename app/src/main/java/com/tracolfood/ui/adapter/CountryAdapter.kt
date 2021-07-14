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
import com.tracolfood.databinding.AdapterCountryBinding
import com.tracolfood.model.CountryData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder

class CountryAdapter(val baseActivity: BaseActivity, val arrayList: ArrayList<CountryData>) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterCountryBinding>(LayoutInflater.from(parent.context), R.layout.adapter_country, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterCountryBinding
        binding.textTV.text = arrayList[position].title
        binding.root.setOnClickListener {
            onItemClick(position)
        }

        if (position + 1 == arrayList.size) {
            onPageEnd()
        }
    }


}
