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

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentHomeBinding
import com.tracolfood.model.CategoryData
import com.tracolfood.model.PackageData
import com.tracolfood.model.VideoData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.CategoryAdapter
import com.tracolfood.ui.adapter.ProductsAdapter
import com.tracolfood.ui.adapter.TrendingAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.handleException
import com.tracolfood.utils.extensions.replaceFragWithArgs
import com.tracolfood.utils.extensions.replaceFragment
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : BaseFragment(), BaseAdapter.OnItemClickListener, ViewClickHandler, BaseAdapter.OnPageEndListener {

    private var binding: FragmentHomeBinding? = null
    private var categoryArrayList: ArrayList<CategoryData> = ArrayList()
    private var categoryCentreArrayList: ArrayList<CategoryData> = ArrayList()
    private var searchMI: MenuItem? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var packageList: ArrayList<PackageData>? = ArrayList()
    private var videoList: java.util.ArrayList<VideoData>? = java.util.ArrayList()
    private var packageAdapter: ProductsAdapter? = null
    private var trendingAdapter: TrendingAdapter? = null
    private var pageCount = 0
    private var videoPage = 0
    private var videoSingleHit = false
    private var singleHit = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (binding != null) {
            val parent = binding!!.root.parent as ViewGroup?
            parent?.removeView(view)
        }
        (baseActivity as MainActivity).setToolBar(true, isShow = true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding!!.clickHandle = this
        clearData()
        setFirstCategory()
        setSecondCategory()
        clearPackageData()
        hitPackageListApi()
        clearVideoData()
        hitTrendingApi()
    }

    private fun setAdapter() {
        if (trendingAdapter == null) {
            trendingAdapter = TrendingAdapter(baseActivity!!, videoList)
            trendingAdapter!!.setOnItemClickListener(this)
            binding!!.trendingRV.adapter = trendingAdapter
        } else {
            trendingAdapter!!.notifyDataSetChanged()
        }
    }

    private fun clearPackageData() {
        packageList!!.clear()
        pageCount = 0
        singleHit = false
        packageAdapter = null
    }


    private fun clearVideoData() {
        videoList!!.clear()
        videoPage = 0
        trendingAdapter = null
        videoSingleHit = false
    }


    private fun clearData() {
        categoryArrayList.clear()
        categoryCentreArrayList.clear()
    }

    private fun setFirstCategory() {
        val categoryList = baseActivity!!.resources.getStringArray(R.array.category_list)
        val categoryIcon = baseActivity!!.resources.obtainTypedArray(R.array.category_icons)
        val fragmentArray = arrayListOf(ConnectFragment(),
                SubCategoryListFragment(),
                OrderDetailFragment(),
                DoctorFragment(),
                DoctorFragment())
        for (i in categoryList.indices) {
            val categoryData = CategoryData()
            categoryData.itemName = categoryList[i]
            categoryData.fragment = fragmentArray[i]
            categoryData.itemIcon = categoryIcon.getResourceId(i, -1)
            categoryArrayList.add(categoryData)
        }
        categoryIcon.recycle()
        categoryAdapter = CategoryAdapter(baseActivity!!, categoryArrayList, 1)
        categoryAdapter!!.setOnItemClickListener(this)
        binding!!.categoryRV.adapter = categoryAdapter
    }

    private fun setSecondCategory() {
        val categoryCentreList = baseActivity!!.resources.getStringArray(R.array.category_list_centre)
        val categoryCentreIcon = baseActivity!!.resources.obtainTypedArray(R.array.category_icons_centre)
        val fragmentCentreArray = arrayListOf(
                VideosFragment(),
                CharityListFragment(),
                DoctorFragment(),
                DoctorFragment())
        for (i in categoryCentreList.indices) {
            val categoryData = CategoryData()
            categoryData.itemName = categoryCentreList[i]
            categoryData.fragment = fragmentCentreArray[i]
            categoryData.itemIcon = categoryCentreIcon.getResourceId(i, -1)
            categoryCentreArrayList.add(categoryData)
        }
        categoryCentreIcon.recycle()
        categoryAdapter = CategoryAdapter(baseActivity!!, categoryCentreArrayList, 2)
        categoryAdapter!!.setOnItemClickListener(this)
        binding!!.categoryCentreRV.adapter = categoryAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        searchMI = menu.findItem(R.id.search)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                baseActivity!!.replaceFragment(SearchFragment())
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onItemClick(vararg itemData: Any) {
        val pos = itemData[0] as Int
        when (itemData[1] as Int) {
            1 -> {
                fragmentHitMethod(pos, categoryArrayList)
            }
            2 -> {
                fragmentHitMethod(pos, categoryCentreArrayList)
            }
            Const.Videos.PLAY -> {
                val bundle = Bundle()
                bundle.putParcelable("list", videoList!![pos])
                baseActivity!!.replaceFragment(ViewVideoFragment(), args = bundle)
            }
        }
    }

    private fun fragmentHitMethod(pos: Int, arrayList: ArrayList<CategoryData>) {
        if (arrayList[pos].fragment is DoctorFragment) {
            val bundle = Bundle()
            bundle.putString("title", arrayList[pos].itemName)
            baseActivity!!.replaceFragWithArgs(arrayList[pos].fragment, args = bundle)
        } else if (arrayList[pos].fragment is OrderDetailFragment) {
            try {
                val viewIntent = Intent("android.intent.action.VIEW",
                        Uri.parse("https://t.me/tracolasia"))
                startActivity(viewIntent)
            } catch (e: ActivityNotFoundException) {
                showToastOne("Unable to find application")
            }
        } else {
            baseActivity!!.replaceFragment(arrayList[pos].fragment)
        }
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.orderNowBT -> {
                baseActivity!!.replaceFragment(SubCategoryListFragment())
            }
        }
    }

    private fun hitPackageListApi() {
        if (!singleHit) {
            val call = api!!.apiPackageList(pageCount)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }


    private fun hitTrendingApi() {
        if (!videoSingleHit) {
            val call = api!!.apiTrendingVideo(pageCount)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.HomeApi.API_PACKAGE_LIST)) {
                if (responseCode == Const.STATUS_OK) {
                    for (i in 0 until jsonObject.getJSONArray("list").length()) {
                        val packageData = Gson().fromJson(jsonObject.getJSONArray("list").getJSONObject(i).toString(), PackageData::class.java)
                        packageList!!.add(packageData)
                    }
                    pageCount++
                    singleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                    setPackageAdapter()
                }
            } else if (responseUrl.contains(Const.HomeApi.API_TRENDING_VIDEO)) {
                if (responseCode == Const.STATUS_OK) {
                    val listJsonArray = jsonObject.getJSONArray("list")
                    if (listJsonArray.length() > 0) {
                        for (i in 0 until listJsonArray.length()) {
                            val json = listJsonArray.getJSONObject(i)
                            val data = Gson().fromJson(json.toString(), VideoData::class.java)
                            videoList!!.add(data)
                        }
                        videoPage++
                        videoSingleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                        setAdapter()
                    }
                }
            } else if (responseUrl.contains(Const.API_SEARCH_API)) {
                if (responseCode == Const.STATUS_OK) {
                    val listJsonArray = jsonObject.getJSONArray("list")
                    if (listJsonArray.length() > 0) {
                        for (i in 0 until listJsonArray.length()) {
                            val json = listJsonArray.getJSONObject(i)
                            val data = Gson().fromJson(json.toString(), VideoData::class.java)
                            videoList!!.add(data)
                        }
                        videoPage++
                        videoSingleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                        setAdapter()
                    }
                }
            }

        } catch (e: JSONException) {
            handleException(e)
        }
    }

    private fun setPackageAdapter() {
        if (packageAdapter == null) {
            packageAdapter = ProductsAdapter(baseActivity!!, packageList!!)
            packageAdapter!!.setOnPageEndListener(this)
            binding!!.productRV.adapter = packageAdapter
        } else {
            packageAdapter!!.notifyDataSetChanged()
        }
    }


    override fun onPageEnd(vararg itemData: Any) {
        hitPackageListApi()
    }

}