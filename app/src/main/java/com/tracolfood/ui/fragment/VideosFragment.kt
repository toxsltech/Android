/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentVideosBinding
import com.tracolfood.model.VideoData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.VideosAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.handleException
import com.tracolfood.utils.extensions.replaceFragment
import com.tracolfood.utils.extensions.visibleView
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class VideosFragment : BaseFragment(), BaseAdapter.OnItemClickListener, BaseAdapter.OnPageEndListener {
    private lateinit var binding: FragmentVideosBinding
    private var videoList: ArrayList<VideoData>? = ArrayList()
    private var adapter: VideosAdapter? = null
    private var pageCount = 0
    private var singleHit = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(isShow = false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.videoRV.layoutManager = GridLayoutManager(baseActivity!!, 2)
        clearData()
        apiVideoList()

    }

    private fun clearData() {
        videoList!!.clear()
        pageCount = 0
        adapter = null
        singleHit = false
    }

    private fun setAdapter() {
        if (adapter == null) {
            adapter = VideosAdapter(baseActivity!!, videoList!!)
            adapter!!.setOnItemClickListener(this)
            adapter!!.setOnPageEndListener(this)
            binding.videoRV.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }

    }

    private fun apiVideoList() {
        if (!singleHit) {
            val call = api!!.apiVideoList(pageCount)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        val jsonObject = JSONObject(response!!)
        try {
            if (responseUrl.contains(Const.Videos.API_VIDEO_LIST)) {
                if (responseCode == Const.STATUS_OK) {
                    val listJsonArray = jsonObject.getJSONArray("list")
                    if (listJsonArray.length() > 0) {
                        for (i in 0 until listJsonArray.length()) {
                            val json = listJsonArray.getJSONObject(i)
                            val data = Gson().fromJson(json.toString(), VideoData::class.java)
                            videoList!!.add(data)
                        }
                        pageCount++
                        singleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                        setAdapter()
                    }
                    binding.noDataTV.visibleView(videoList!!.size == 0)
                }
            }
        } catch (e: JSONException) {
            handleException(e)
        }

    }

    override fun onItemClick(vararg itemData: Any) {
        if (itemData.isNotEmpty()) {
            val pos = itemData[0] as Int
            val type = itemData[1] as Int
            when (type) {
                Const.Videos.PLAY -> {
                    val bundle = Bundle()
                    bundle.putParcelable("list", videoList!![pos])
                    baseActivity!!.replaceFragment(ViewVideoFragment(), args = bundle)
                }
            }
        }
    }

    override fun onPageEnd(vararg itemData: Any) {
        apiVideoList()
    }


}