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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.FragmentPaymentDetailBinding
import com.tracolfood.model.CartProductData
import com.tracolfood.room.RoomUtils
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.CheckoutAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.ui.fragment.pages.PrivacyFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.replaceFragWithArgs
import java.text.DecimalFormat


class PaymentDetailFragment : BaseFragment(), ViewClickHandler, RoomUtils.roomCallbackListener {
    private lateinit var binding: FragmentPaymentDetailBinding
    private var cartProductList: ArrayList<CartProductData> = ArrayList()
    private var adapter: CheckoutAdapter? = null
    private var productId = 0
    private var paymentId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            productId = arguments?.getInt("id")!!
            paymentId = arguments?.getInt("paymentId")!!
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.clickHandle = this
        baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
        setAdapter()
    }

    private fun setAdapter() {
        adapter = CheckoutAdapter(baseActivity!!, cartProductList)
        binding.itemsRV.adapter = adapter
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.placeOrderBT -> {
                if (cartProductList.size > 0) {
                    if (binding.termsCB.isChecked){
                        val bundle = Bundle()
                        bundle.putString("totalPrice", binding.totalPriceTV.text.toString().replace("RM ", ""))
                        bundle.putParcelableArrayList("cardProductList", cartProductList)
                        baseActivity!!.replaceFragWithArgs(AddressListFragment(), args = bundle)
                    } else{
                      baseActivity!!.showToastOne(baseActivity!!.getString(R.string.please_agree_to_terms_conditions))
                    }
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


    @SuppressLint("SetTextI18n")
    private fun setData() {
        if (cartProductList.size > 0) {
            binding.numItemTV.text = "${baseActivity!!.getString(R.string.items_colon)} ${cartProductList.size}"
        }
        var price = 0.0
        var quantity = 0
        for (i in 0 until cartProductList.size) {
            price += cartProductList[i].price!!.toDouble() * cartProductList[i].addedQuantity!!
            quantity += cartProductList[i].addedQuantity!!
        }
        binding.numItemPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus(DecimalFormat("##.##").format(price))
        binding.deliveryPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus("24 * ").plus(quantity)
        binding.totalPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus(DecimalFormat("##.##").format(price + (24 * quantity)))
        binding.itemPriceValueTV.text = baseActivity!!.getString(R.string.rm_sign).plus(DecimalFormat("##.##").format(price))
    }

    override fun onDataReceived(vararg objects: Any?) {
        val isSuccess: Boolean = objects[1] as Boolean
        val type = objects[0] as Int
        when (type) {
            Const.GET_ALL_ITEM -> {
                cartProductList.clear()
                cartProductList.addAll(objects[2] as ArrayList<CartProductData>)
                setAdapter()
                setData()
            }
        }
    }


}