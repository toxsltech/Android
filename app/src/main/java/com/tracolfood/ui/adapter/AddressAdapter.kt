/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.AdapterAddressBinding
import com.tracolfood.model.AddAddress
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseViewHolder
import com.tracolfood.ui.fragment.AddAddressFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.replaceFragWithArgs

class AddressAdapter(val baseActivity: BaseActivity, val addressList: ArrayList<AddAddress>) : BaseAdapter(), BaseAdapter.OnPageEndListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<AdapterAddressBinding>(LayoutInflater.from(parent.context), R.layout.adapter_address, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = holder.binding as AdapterAddressBinding
        binding.addressValueTV.text = baseActivity.getString(R.string.address).plus(" : ").plus(addressList[position].primaryAddress)
        binding.cardCB.isChecked = addressList[position].isDefault!! > 0
        binding.phoneTV.text = baseActivity.getString(R.string.phone_number_colon).plus(" ").plus(addressList[position].contactNo)
        binding.zipCodeTV.text = baseActivity.getString(R.string.zip_code_colon).plus(" ").plus(addressList[position].zipcode)
        if (position + 1 == addressList.size) {
            onPageEnd()
        }


        binding.editIV.setOnClickListener {

            onItemClick(Const.TYPE_EDIT, position)

        }
        binding.deleteIV.setOnClickListener {
            onItemClick(Const.TYPE_DELETE, position)
        }

        binding.cardCB.setOnClickListener {
            for (i in 0 until addressList.size) {
                addressList[i].isDefault = 0
            }
            addressList[position].isDefault = 1
            notifyDataSetChanged()
            onItemClick(Const.TYPE_SELECT, position)
        }

    }


}
