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
import com.tracolfood.R
import com.tracolfood.databinding.FragmentCartBinding
import com.tracolfood.model.CartProductData
import com.tracolfood.room.RoomUtils
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.CartListAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.replaceFragWithArgs
import org.json.JSONArray


class CartFragment : BaseFragment(), RoomUtils.roomCallbackListener, ViewClickHandler, BaseAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCartBinding
    private var cartProductList: ArrayList<CartProductData> = ArrayList()
    private var productId = 0
    private var adapter: CartListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getInt("id")
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true, isShow = true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandle = this
        reset()
        baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
    }

    private fun setAdapter() {
        if (cartProductList.size > 0) {
            binding.noDataTV.visibility = View.GONE
            if (adapter == null) {
                adapter = CartListAdapter(baseActivity!!, cartProductList)
                adapter!!.setOnItemClickListener(this)
                binding.cartListRV.adapter = adapter
            } else {
                adapter!!.notifyDataSetChanged()
            }

        } else {
            binding.noDataTV.visibility = View.VISIBLE
        }

    }


    private fun reset() {
        cartProductList.clear()
        adapter = null
    }

    private fun onClickCartFunction(type: Int, position: Int, list: ArrayList<CartProductData>, quantity: Int) {
        when (type) {
            Const.UPDATE_QUANTITY -> {
                baseActivity!!.roomUtils!!.addData(list[position], type, this)
            }

            Const.DELETE_PRODUCT -> {
                if (list.size > 0)
                    baseActivity!!.roomUtils!!.deleteProduct(list[position], type, this)
            }
        }

    }


    override fun cartData(data: CartProductData?) {


    }

    override fun onSuccess() {

    }

    private val isValid: Boolean
        get() {
            if (cartProductList.isNullOrEmpty()) {
                showToastOne("Please add item in cart")
            } else {
                return true
            }
            return false
        }

    private fun jsonArrayForCart() {
        val jsonArr = JSONArray()
        val bundle = Bundle()
        bundle.putString("array", jsonArr.toString())
    }


    override fun onDataReceived(vararg objects: Any?) {
        val isSuccess: Boolean = objects[1] as Boolean
        val type = objects[0] as Int
        when (type) {
            Const.UPDATE_QUANTITY -> {
                baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)

            }
            Const.DELETE_PRODUCT -> {
                baseActivity!!.roomUtils!!.getCartCount(Const.GET_COUNT, this)
                baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
            }
            Const.GET_ALL_ITEM -> {
                cartProductList.clear()
                cartProductList.addAll(objects[2] as ArrayList<CartProductData>)
                setAdapter()
            }
        }


    }


    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.checkOutBT -> {
                if (isValid) {
                    val bundle = Bundle()
                    bundle.putInt("quantity", cartProductList[0].addedQuantity!!)
                    bundle.putInt("id", cartProductList[0].productId!!)
                    bundle.putString("productName", cartProductList[0].productName)
                    bundle.putString("price", cartProductList[0].price)
                    baseActivity!!.replaceFragWithArgs(CheckOutFragment(), R.id.container, bundle)
                    baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
                }

            }
        }
    }

    override fun onItemClick(vararg itemData: Any) {
        val pos = itemData[0] as Int
        val type = itemData[1] as Int
        val count = itemData[2] as Int
        when (type) {
            Const.UPDATE_QUANTITY -> {
                onClickCartFunction(type, pos, cartProductList, count)
            }
            Const.DELETE_PRODUCT -> {
                onClickCartFunction(type, pos, cartProductList, count)
            }
        }
    }


}