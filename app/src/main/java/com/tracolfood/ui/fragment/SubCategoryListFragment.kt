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

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentSubCategoryListBinding
import com.tracolfood.model.CartProductData
import com.tracolfood.model.ProductData
import com.tracolfood.room.RoomUtils
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.SubCategoryAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.handleException
import com.tracolfood.utils.extensions.replaceFragWithArgs
import org.json.JSONException
import org.json.JSONObject


class SubCategoryListFragment : BaseFragment(), RoomUtils.roomCallbackListener, BaseAdapter.OnItemClickListener, BaseAdapter.OnPageEndListener {
    private lateinit var binding: FragmentSubCategoryListBinding
    private var productList: ArrayList<ProductData> = ArrayList()
    private var adapter: SubCategoryAdapter? = null
    private var cartData: ArrayList<CartProductData>? = ArrayList()
    private var sellerId: ArrayList<Int>? = ArrayList()
    private var cartCount: String = "0"
    private var menuItem: MenuItem? = null
    private var pageCount = 0
    private var singleHit = false


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true, isShow = false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_category_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.subCategoryRV.layoutManager = LinearLayoutManager(baseActivity!!, LinearLayoutManager.HORIZONTAL, false)
        setHasOptionsMenu(true)
        baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
        clearData()
        hitProductListApi()
    }

    private fun clearData() {
        productList.clear()
        singleHit = false
        pageCount = 0
        adapter = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cart_menu, menu)
        menuItem = menu.findItem(R.id.badge)
        val icon: LayerDrawable = menuItem!!.icon as LayerDrawable
        baseActivity!!.setBadgeCount(baseActivity!!, icon, cartCount)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.badge -> {
                (baseActivity as MainActivity).setBottomSelection(R.id.cart)
                val bundle = Bundle()
                bundle.putInt("id", productList[0].id!!)
                bundle.putParcelable("data", productList[0])
                baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
                baseActivity!!.replaceFragWithArgs(CartFragment(), R.id.container, bundle)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hitProductListApi() {
        if (!singleHit) {
            val call = api!!.apiProductList()
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.Fruits.API_PRODUCT_LIST)) {
                if (responseCode == Const.STATUS_OK) {
                    for (i in 0 until jsonObject.getJSONArray("list").length()) {
                        val productData = Gson().fromJson(jsonObject.getJSONArray("list").getJSONObject(i).toString(), ProductData::class.java)
                        productList.add(productData)

                    }
                    for (i in 0 until productList.size) {
                        for (j in 0 until cartData!!.size) {
                            if (productList[i].id == cartData!![j].productId) {
                                productList[i].isSelected = true
                            }
                        }
                    }
                    pageCount++
                    singleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                    setAdapter()
                }
            }
        } catch (e: JSONException) {
            handleException(e)
        }
    }

    private fun setAdapter() {
        if (adapter == null) {
            adapter = SubCategoryAdapter(baseActivity!!, productList)
            adapter!!.setOnItemClickListener(this)
            adapter!!.setOnPageEndListener(this)
            binding.subCategoryRV.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
    }


    private fun onClickCartFunction(position: Int, productData: ArrayList<ProductData>) {
        val data = CartProductData()
        data.productId = productData[position].id!!
        data.sellerId = baseActivity!!.getProfileData().id
        data.addedQuantity = 1
        data.productName = productData[position].title
        data.price = productData[position].amount
        data.itemType = Const.TYPE_PRODUCT
        data.imageFile = productData[position].imageFile
        baseActivity!!.roomUtils!!.getCartCount(Const.GET_COUNT, this)
        baseActivity!!.roomUtils!!.addData(data, Const.ADD_PRODUCT, this)
        baseActivity!!.store?.setInt("productId", productData[position].id!!)


    }

    override fun cartData(data: CartProductData?) {

    }

    override fun onSuccess() {

    }

    override fun onDataReceived(vararg objects: Any?) {
        if (objects.isNotEmpty()) {
            val type: Int = objects[0] as Int
            val isSuccess: Boolean = objects[1] as Boolean
            when (type) {
                Const.ADD_PRODUCT -> {
                    if (isSuccess) {
                        getCartData()
                        baseActivity!!.showToastOne(baseActivity!!.getString(R.string.product_added_to_cart_successfully))
                    }
                }
                Const.GET_ALL_ITEM -> {
                    if (objects[2] != null && (objects[2] as ArrayList<CartProductData>).size > 0) {
                        cartData = objects[2] as ArrayList<CartProductData>
                        cartCount = cartData!!.size.toString()
                    } else {
                        cartCount = "0"
                        cartData!!.clear()
                        setAdapter()
                    }
                    val icon: LayerDrawable = menuItem!!.icon as LayerDrawable
                    baseActivity!!.setBadgeCount(baseActivity!!, icon, cartCount)
                    getAllCartData(cartData!!)
                }
            }
        }
    }

    private fun getCartData() {
        if (baseActivity!!.roomUtils != null) {
            baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
        }
    }

    override fun onItemClick(vararg itemData: Any) {
        if (itemData.isNotEmpty()) {
            val position = itemData[0] as Int
            val type = itemData[1] as Int
            when (type) {
                Const.ADD -> {
                    onClickCartFunction(position, productList)
                    productList[position].isSelected = true
                    setAdapter()
                }
            }
        }
    }


    private fun getAllCartData(data: List<CartProductData>) {
        if (data.isNotEmpty()) {
            sellerId!!.clear()
            for (element in data) {
                sellerId!!.add(element.id)
            }
        }
    }

    override fun onPageEnd(vararg itemData: Any) {
        hitProductListApi()
    }

}



