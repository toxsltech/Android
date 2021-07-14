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
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.tracolfood.R
import com.tracolfood.databinding.AdapterWishlistBinding
import com.tracolfood.model.ProductData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.ui.fragment.SubCategoryDetailFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.loadImageUrl
import com.tracolfood.utils.extensions.replaceFragWithArgs

class WishListAdapter(val baseActivity: BaseActivity, private val arrayList: ArrayList<ProductData>, val productType: Int = 1) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterWishlistBinding>(LayoutInflater.from(parent.context), R.layout.adapter_wishlist, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterWishlistBinding
        binding.priceTV.text = baseActivity.getString(R.string.rm_sign).plus(arrayList[position].amount)
        binding.imageIV.loadImageUrl(arrayList[position].imageFile,R.mipmap.ic_youtube)
        binding.imageIV.shapeAppearanceModel = binding.imageIV.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 30f)
                .build()
        binding.textTV.text = arrayList[position].title
        binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", arrayList[position].id!!)
            bundle.putInt("type", arrayList[position].typeId!!)
            baseActivity.replaceFragWithArgs(SubCategoryDetailFragment(), args = bundle)
        }
    }


}
