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
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.tracolfood.R
import com.tracolfood.databinding.AdapterCharityBinding
import com.tracolfood.model.CharityData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.ui.fragment.CharityFragment
import com.tracolfood.ui.fragment.PaymentFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.loadImageUrl
import com.tracolfood.utils.extensions.replaceFragWithArgs
import com.tracolfood.utils.extensions.replaceFragment

class CharityAdapter(val baseActivity: BaseActivity, var arrayList: ArrayList<CharityData>) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterCharityBinding>(LayoutInflater.from(parent.context), R.layout.adapter_charity, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterCharityBinding
        binding.packageTV.text = arrayList[position].title
        binding.descTV.text = Html.fromHtml(arrayList[position].description)
        binding.imageIV.loadImageUrl(arrayList[position].imageFile,R.mipmap.ic_youtube)
        binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("charityData", arrayList[position])
            baseActivity.replaceFragWithArgs(CharityFragment(), args = bundle)
        }
        if (position + 1 == arrayList.size) {
            onPageEnd()
        }
    }

}
