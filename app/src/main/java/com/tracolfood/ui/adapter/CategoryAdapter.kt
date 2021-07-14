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
import com.tracolfood.databinding.AdapterCategoryBinding
import com.tracolfood.model.CategoryData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder

class CategoryAdapter(val baseActivity: BaseActivity, private val categoryArrayList: ArrayList<CategoryData>, val typeId: Int) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterCategoryBinding>(LayoutInflater.from(parent.context), R.layout.adapter_category, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterCategoryBinding
        binding.categoryData = categoryArrayList[position]
        binding.executePendingBindings()
        binding.root.setOnClickListener {
            onItemClick(position, typeId)
        }
    }


}


