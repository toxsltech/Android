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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.FragmentDoctorBinding
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment


class DoctorFragment : BaseFragment() {
    private lateinit var binding: FragmentDoctorBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true, isShow = false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        when {
            requireArguments().getString("title")!! == getString(R.string.logistics) -> {
                binding.mainLL.background = ContextCompat.getDrawable(baseActivity!!, R.drawable.logistic_bg)
                binding.imageIV.setImageResource(R.drawable.logistics)
            }
            requireArguments().getString("title")!! == getString(R.string.travel) -> {
                binding.mainLL.background = ContextCompat.getDrawable(baseActivity!!, R.color.White)
                binding.imageIV.setImageResource(R.drawable.travel)
            }
            requireArguments().getString("title")!! == getString(R.string.takafull) -> {
                binding.mainLL.background = ContextCompat.getDrawable(baseActivity!!, R.color.takafull)
                binding.imageIV.setImageResource(R.drawable.takafull)
            }
            requireArguments().getString("title")!! == getString(R.string.pay) -> {
                binding.mainLL.background = ContextCompat.getDrawable(baseActivity!!, R.color.pay)
                binding.imageIV.setImageResource(R.drawable.pay)
            }
        }
    }
}