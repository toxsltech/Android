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
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.FragmentPaymentBinding
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.replaceFragWithArgs


class PaymentFragment : BaseFragment(), ViewClickHandler {
    private var productName: String = ""
    private var price: String = ""
    private var productId = 0
    private var quantity = 0
    private lateinit var binding: FragmentPaymentBinding
    private var isMain = false
    private var paymentId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isMain = arguments?.getBoolean("isMain")!!
            productId = arguments?.getInt("id")!!
            quantity = arguments?.getInt("quantity")!!
            if (requireArguments().containsKey("price")) {
                price = arguments?.getString("price")!!
            }

            if (requireArguments().containsKey("productName")) {
                productName = arguments?.getString("productName")!!
            }

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(false, isShow = false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandle = this
        if (isMain) {
            (baseActivity as MainActivity).setToolBar(true, text = "Payment Method")
            binding.toolbarRL.visibility = View.GONE
            binding.continueBT.visibility = View.GONE

        } else {
            (baseActivity as MainActivity).setToolBar(false)
            binding.toolbarRL.visibility = View.VISIBLE
            binding.continueBT.visibility = View.VISIBLE
        }
    }


    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.backIV -> {
                baseActivity!!.supportFragmentManager.popBackStack()
            }
            R.id.cardCB -> {
                paymentId = 1
                setSelection(binding.cardCB, binding.visaCB, binding.bankCB)

            }
            R.id.visaCB -> {
                paymentId = 2
                setSelection(binding.visaCB, binding.bankCB, binding.cardCB)

            }
            R.id.bankCB -> {
                paymentId = 3
                setSelection(binding.bankCB, binding.cardCB, binding.visaCB)

            }
            R.id.continueBT -> {
                if (!binding.cardCB.isChecked && !binding.bankCB.isChecked && !binding.visaCB.isChecked) {
                    showToast("Please Select Payment Method")
                } else {
                    val bundle = Bundle()
                    bundle.putInt("id", productId)
                    bundle.putInt("quantity", quantity)
                    bundle.putString("productName", productName)
                    bundle.putString("price", price)
                    bundle.putInt("paymentId", paymentId)
                    baseActivity!!.replaceFragWithArgs(CheckOutFragment(), R.id.container, bundle)
                }
            }
        }
    }


    private fun setSelection(selection: CheckBox, unselectionOne: CheckBox, unSelectionTwo: CheckBox) {
        selection.isChecked = true
        unselectionOne.isChecked = false
        unSelectionTwo.isChecked = false
    }


}