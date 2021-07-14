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
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import coil.api.load
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.tracolfood.R
import com.tracolfood.databinding.AdapterSubCategoryListBinding
import com.tracolfood.model.ProductData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.ui.fragment.SubCategoryDetailFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.loadCircleImageUrl
import com.tracolfood.utils.extensions.loadImageUrl
import com.tracolfood.utils.extensions.replaceFragWithArgs

class SubCategoryAdapter(val baseActivity: BaseActivity, val productList: ArrayList<ProductData>) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterSubCategoryListBinding>(LayoutInflater.from(parent.context), R.layout.adapter_sub_category_list, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterSubCategoryListBinding
        binding.itemNameTV.text = productList[position].title
        if (productList[position].description!!.length > 500) {
            binding.itemDescTV.text = Html.fromHtml(productList[position].description!!.substring(1, 497).plus("<b>More</b>"))
        } else {
            binding.itemDescTV.text = Html.fromHtml(productList[position].description!!)
        }

        binding.priceTV.text = baseActivity.getString(R.string.rm_sign).plus(productList[position].amount)
        if (!productList[position].imageFile.isNullOrEmpty()) {
            binding.fruitIV.loadImageUrl(productList[position].imageFile, R.mipmap.ic_youtube)
        } else {
            binding.fruitIV.setImageResource(R.mipmap.ic_youtube)
        }
        binding.fruitIV.shapeAppearanceModel = binding.fruitIV.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 30f)
                .build()

        if (productList[position].isSelected) {
            binding.cartIV.visibility = View.GONE
        } else {
            binding.cartIV.visibility = View.VISIBLE
        }

        binding.fruitIV.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", Const.TYPE_PRODUCT)
            bundle.putInt("id", productList[position].id!!)
            baseActivity.replaceFragWithArgs(SubCategoryDetailFragment(), args = bundle)
        }

        binding.cartIV.setOnClickListener {
            onItemClick(position, Const.ADD)
        }
        if (position + 1 == productList.size) {
            onPageEnd()
        }

    }


}
