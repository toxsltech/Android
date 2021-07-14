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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentOrderDetailBinding
import com.tracolfood.model.OrderData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.ItemListAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.ui.fragment.authentication.SignUpFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.handleException
import com.tracolfood.utils.extensions.replaceFragWithArgs
import com.tracolfood.utils.extensions.visibleView
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat


class OrderDetailFragment : BaseFragment(), ViewClickHandler {
    private lateinit var binding: FragmentOrderDetailBinding
    private var orderId: Int = 0
    private var paymentUrl = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            orderId = requireArguments().getInt("orderId")
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true, "", false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

    }

    private fun initUI() {
        binding.clickHandler = this
        binding.itemsRV.layoutManager = LinearLayoutManager(baseActivity!!)
        hitOrderDetailApi()

    }

    private fun setData(orderData: OrderData) {
        this.paymentUrl = orderData.paymentUrl!!
        if (orderData.paymentStatus != null && orderData.paymentStatus > 0) {
            binding.payMethodTV.text = baseActivity!!.getString(R.string.patment_status).plus(" ").plus(baseActivity!!.getString(R.string.paid))
            binding.paymentBT.visibleView(false)
        } else {
            binding.payMethodTV.text = baseActivity!!.getString(R.string.patment_status).plus(" ").plus(baseActivity!!.getString(R.string.un_paid))
            binding.paymentBT.visibleView(true)
        }
        var price = 0.0
        var quantity = 0
        for (i in 0 until orderData.items!!.size) {
            price += orderData.items[i].amount!!.toDouble() * orderData.items[i].quantity!!.toDouble()
            quantity += orderData.items[i].quantity!!.toInt()
        }
        binding.numItemPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus(price)
        binding.deliveryPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus("24 * ").plus(quantity)
        binding.totalPriceTV.text = baseActivity!!.getString(R.string.rm_sign).plus(DecimalFormat("##.##").format(price + (24 * quantity)))
        binding.itemPriceValueTV.text = baseActivity!!.getString(R.string.rm_sign).plus(price)

        val adapter = ItemListAdapter(baseActivity!!, orderData.items)
        binding.itemsRV.adapter = adapter
    }


    private fun hitOrderDetailApi() {
        val call = api!!.apiOrderDetail(orderId)
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.API_ORDER_DETAIL_API)) {
                if (responseCode == Const.STATUS_OK) {
                    val orderDetail = Gson().fromJson(jsonObject.getJSONObject("Detail").toString(), OrderData::class.java)
                    setData(orderDetail!!)
                }
            }
        } catch (e: JSONException) {
            handleException(e)
        }

    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.paymentBT -> {
                if (!paymentUrl.isBlank()) {
                    val bundle = Bundle()
                    bundle.putString("url", paymentUrl)
                    baseActivity!!.replaceFragWithArgs(SignUpFragment(), args = bundle)
                }
            }
        }

    }

}