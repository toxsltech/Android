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
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentWishListBinding
import com.tracolfood.model.ProductData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.WishListAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.visibleView
import org.json.JSONException
import org.json.JSONObject


class WishListFragment : BaseFragment(), BaseAdapter.OnPageEndListener, ViewClickHandler {
    private lateinit var binding: FragmentWishListBinding
    private var pageCount = 0
    private var singleHit = false
    private var arrayList: ArrayList<ProductData> = ArrayList()
    private var adapter: WishListAdapter? = null
    private var productType = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true, isShow = true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wish_list, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.clickHandle = this
        binding.wishListRV.layoutManager = GridLayoutManager(baseActivity, 2)
        if (baseActivity!!.store != null && baseActivity!!.store!!.containValue("lastSelection")) {
            if (baseActivity!!.store!!.getString("lastSelection", "1") == "1") {
                clearData()
                binding.productV.visibleView(true)
                binding.packageV.visibleView(false)
                hitWishListApi()
            } else {
                clearData()
                binding.packageV.visibleView(true)
                binding.productV.visibleView(false)
                hitFavPackageList()
            }
        } else {
            clearData()
            hitFavPackageList()
        }
    }

    private fun clearData() {
        singleHit = false
        arrayList.clear()
        pageCount = 0
        adapter = null
    }

    private fun hitWishListApi() {
        if (!singleHit) {
            val call = api!!.apiWishList(pageCount)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }

    private fun hitFavPackageList() {
        if (!singleHit) {
            val call = api!!.apiFavPackageList(pageCount)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.API_WISH_LIST) || responseUrl.contains(Const.API_FAVORITE_PACKAGE_LIST)) {
                if (responseCode == Const.STATUS_OK) {
                    for (i in 0 until jsonObject.getJSONArray("list").length()) {
                        val favoriteData = Gson().fromJson(jsonObject.getJSONArray("list").getJSONObject(i).toString(), ProductData::class.java)
                        arrayList.add(favoriteData)
                    }
                    pageCount++
                    singleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                    setAdapter()
                    binding.noDataTV.visibleView(arrayList.size <= 0)
                }
            }
        } catch (e: JSONException) {
            baseActivity!!.handelException(e)
        }
    }


    private fun setAdapter() {
        if (adapter == null) {
            adapter = WishListAdapter(baseActivity!!, arrayList, productType)
            adapter!!.setOnPageEndListener(this)
            binding.wishListRV.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onPageEnd(vararg itemData: Any) {
        hitWishListApi()
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.productsTV -> {
                productType = Const.TYPE_PRODUCT
                baseActivity!!.store!!.saveString("lastSelection", "1")
                binding.productV.visibleView(true)
                binding.packageV.visibleView(false)
                clearData()
                hitWishListApi()
            }
            R.id.packageTV -> {
                productType = Const.TYPE_PACKAGE
                baseActivity!!.store!!.saveString("lastSelection", "2")
                binding.packageV.visibleView(true)
                binding.productV.visibleView(false)
                clearData()
                hitFavPackageList()
            }


        }
    }


}