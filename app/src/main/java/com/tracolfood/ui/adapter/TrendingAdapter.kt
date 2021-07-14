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
import com.tracolfood.R
import com.tracolfood.databinding.AdapterTrendingBinding
import com.tracolfood.model.VideoData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.loadImageUrl
import java.util.*

class TrendingAdapter(val baseActivity: BaseActivity, private val videoList: ArrayList<VideoData>?) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterTrendingBinding>(LayoutInflater.from(parent.context), R.layout.adapter_trending, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return videoList!!.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterTrendingBinding
        val data = videoList!![holder.adapterPosition]
        binding.textTV.text = data.title
        if (data.imageFile!!.contains("?")) {
            binding.imageIV.loadImageUrl(data.imageFile + "&thumbnail=150",R.mipmap.ic_youtube)
        } else {
            binding.imageIV.loadImageUrl(data.imageFile + "?thumbnail=150",R.mipmap.ic_youtube)
        }
        binding.imageIV.setOnClickListener {
            onItemClick(position, Const.Videos.PLAY)
        }
        binding.playIV.setOnClickListener {
            onItemClick(position, Const.Videos.PLAY)
        }
        if (position + 1 == videoList.size) {
            onPageEnd()
        }
    }


}
