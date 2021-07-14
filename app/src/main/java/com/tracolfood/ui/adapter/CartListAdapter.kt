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
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.tracolfood.R
import com.tracolfood.databinding.AdapterCartBinding
import com.tracolfood.model.CartProductData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.loadImageUrl
import java.lang.String

class CartListAdapter(val baseActivity: BaseActivity, private val productData: ArrayList<CartProductData>) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterCartBinding>(LayoutInflater.from(parent.context), R.layout.adapter_cart, parent, false)
        return BaseViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterCartBinding
        val data = productData[holder.adapterPosition]
        binding.productNameTV.text = data.productName
        binding.countTV.text = data.addedQuantity.toString()

        binding.productIV.loadImageUrl(data.imageFile,R.mipmap.ic_youtube)

        binding.priceTV.text = baseActivity.getString(R.string.rm_sign).plus(((data.price)!!.toInt() * String.valueOf(binding.countTV.text).toInt()).toString())

        binding.minusTV.setOnClickListener {
            var count: Int = String.valueOf(binding.countTV.text).toInt()
            if (count > 1) {
                count--
                binding.countTV.text = count.toString()
                binding.priceTV.text = baseActivity.getString(R.string.rm_sign).plus(((data.price)!!.toInt() * count).toString())
                data.addedQuantity = count
                onItemClick(position, Const.UPDATE_QUANTITY, count)
            } else if (count == 1) {
                count -= 1
                data.addedQuantity = count
                onItemClick(position, Const.DELETE_PRODUCT, count)
                if (productData.size > 0){
                    productData.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }

        binding.plusTV.setOnClickListener {
            var count = String.valueOf(binding.countTV.text).toInt()
            count++
            binding.countTV.text = count.toString()
            binding.priceTV.text = baseActivity.getString(R.string.rm_sign).plus(((data.price)!!.toInt() * count).toString())
            data.addedQuantity = count
            onItemClick(position, Const.UPDATE_QUANTITY, count)

        }
        binding.productIV.shapeAppearanceModel = binding.productIV.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 40f)
                .build()

    }

    override fun getItemCount(): Int {
        return productData.size
    }


}
