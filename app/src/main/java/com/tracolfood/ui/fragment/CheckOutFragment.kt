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
import com.tracolfood.databinding.FragmentCheckOutBinding
import com.tracolfood.model.CartProductData
import com.tracolfood.room.RoomUtils
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.CartListAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.ui.fragment.pages.PrivacyFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.replaceFragWithArgs
import java.text.DecimalFormat

class CheckOutFragment : BaseFragment(), ViewClickHandler, RoomUtils.roomCallbackListener, BaseAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCheckOutBinding
    private var productName: String = ""
    private var price: String = ""
    private var productId = 0
    private var quantity = 0
    private var cartProductList: ArrayList<CartProductData> = ArrayList()
    private var adapter: CartListAdapter? = null
    private var selectedPos = 0
    private var paymentId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            productId = arguments?.getInt("id")!!
            quantity = arguments?.getInt("quantity")!!
            productName = arguments?.getString("productName")!!
            price = arguments?.getString("price")!!
            paymentId = arguments?.getInt("paymentId")!!
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_check_out, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.clickHandle = this
        baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
    }

    private fun setAdapter() {
        adapter = CartListAdapter(baseActivity!!, cartProductList)
        adapter!!.setOnItemClickListener(this)
        binding.checkOutRV.adapter = adapter
    }

    private fun setData(pos: Int) {
        binding.numItemTV.text = baseActivity!!.getString(R.string.items_colon).plus(cartProductList.size)
        if (cartProductList.size > 0) {
            var price = 0.0
            var quantity = 0
            for (i in 0 until cartProductList.size) {
                price += cartProductList[i].addedQuantity!!.toDouble() * cartProductList[i].price!!.toDouble()
                quantity += cartProductList[i].addedQuantity!!
            }
            binding.numItemPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus(DecimalFormat("##.##").format(price))
            binding.itemPriceValueTV.text = baseActivity!!.getString(R.string.rm_sign).plus(DecimalFormat("##.##").format(price))

            binding.deliveryPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus("24 * ").plus(quantity)
            binding.totalPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus(DecimalFormat("##.##").format(price + (24 * quantity)))
        } else {
            binding.numItemPriceTV.text = baseActivity!!.getString(R.string.zero_rm)
            binding.totalPriceTV.text = baseActivity!!.getString(R.string.zero_rm)
            binding.itemPriceValueTV.text = baseActivity!!.getString(R.string.zero_rm)
            binding.deliveryPriceTV.text = baseActivity!!.getString(R.string.zero_rm)
        }
    }


    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.placeOrderBT -> {
                if (cartProductList.size > 0) {
                    if (binding.termsCB.isChecked) {
                        val bundle = Bundle()
                        bundle.putParcelableArrayList("productList", cartProductList)
                        bundle.putInt("paymentId", paymentId)
                        baseActivity!!.replaceFragWithArgs(PaymentDetailFragment(), R.id.container, bundle)
                    } else {
                        showToastOne(getString(R.string.please_agree_to_terms_conditions))
                    }

                } else {
                    baseActivity!!.showToastOne(getString(R.string.your_cart_is_empty))
                }
            }
            R.id.termsTV -> {
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.terms_conditions))
                bundle.putBoolean("isPayment", true)
                bundle.putInt("typeId", Const.StaticPage.TYPE_TERMS)
                baseActivity!!.replaceFragWithArgs(PrivacyFragment(), args = bundle)
            }
        }
    }

    override fun cartData(data: CartProductData?) {

    }

    override fun onSuccess() {

    }

    private fun onClickCartFunction(type: Int, position: Int, list: ArrayList<CartProductData>, quantity: Int) {
        when (type) {
            Const.UPDATE_QUANTITY -> {
                baseActivity!!.roomUtils!!.addData(list[position], type, this)
            }

            Const.DELETE_PRODUCT -> {
                baseActivity!!.roomUtils!!.deleteProduct(list[position], Const.DELETE_PRODUCT, this)
                baseActivity!!.roomUtils!!.getCartCount(Const.GET_COUNT, this)
                baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
            }
        }

    }

    override fun onDataReceived(vararg objects: Any?) {
        val isSuccess: Boolean = objects[1] as Boolean
        val type = objects[0] as Int
        when (type) {
            Const.UPDATE_QUANTITY -> {
                baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)

            }
            Const.GET_ALL_ITEM -> {
                cartProductList.clear()
                cartProductList.addAll(objects[2] as ArrayList<CartProductData>)

                setAdapter()
                setData(selectedPos)
                if (cartProductList.size == 0) {
                    (baseActivity as MainActivity).onBackPressed()
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
                selectedPos = pos
                onClickCartFunction(Const.UPDATE_QUANTITY, pos, cartProductList, count)
            }

            Const.DELETE_PRODUCT -> {
                onClickCartFunction(Const.DELETE_PRODUCT, pos, cartProductList, count)
            }
        }
        baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
    }

}