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

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.shape.CornerFamily
import com.tracolfood.R
import com.tracolfood.databinding.AdapterVideosBinding
import com.tracolfood.model.VideoData
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.loadImageUrl


class VideosAdapter(val baseActivity: BaseActivity, val list: ArrayList<VideoData>) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterVideosBinding>(LayoutInflater.from(parent.context), R.layout.adapter_videos, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterVideosBinding
        val data = list[holder.adapterPosition]
        binding.textTV.text = data.title
        binding.descriptionTV.text = Html.fromHtml(data.description)
        if (data.imageFile!!.contains("?")) {
            binding.imageIV.loadImageUrl(data.imageFile + "&thumbnail=150",R.mipmap.ic_youtube)
        } else {
            binding.imageIV.loadImageUrl(data.imageFile + "?thumbnail=150",R.mipmap.ic_youtube)
        }

        binding.imageIV.shapeAppearanceModel = binding.imageIV.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 30f)
                .build()

        binding.imageIV.setOnClickListener {
            onItemClick(position, Const.Videos.PLAY)
        }
        binding.playIV.setOnClickListener {
            onItemClick(position, Const.Videos.PLAY)
        }
        if (position + 1 == list.size) {
            onPageEnd()
        }


    }


}
