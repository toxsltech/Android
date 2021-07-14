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
import com.tracolfood.databinding.FragmentAddBankDetailBinding
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.ViewClickHandler

class AddBankDetailFragment : BaseFragment(), ViewClickHandler {
    private lateinit var binding: FragmentAddBankDetailBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_bank_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandle = this
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.saveBT -> {
                baseActivity!!.onBackPressed()
            }
            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }
        }
    }

}