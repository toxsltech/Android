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
import com.toxsl.restfulClient.api.Api3Params
import com.tracolfood.R
import com.tracolfood.databinding.FragmentAddAddressBinding
import com.tracolfood.model.AddAddress
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.checkString
import com.tracolfood.utils.extensions.handleException
import com.tracolfood.utils.extensions.isBlank
import org.json.JSONException
import org.json.JSONObject


class AddAddressFragment : BaseFragment(), ViewClickHandler {
    private lateinit var binding: FragmentAddAddressBinding
    private var addressData: AddAddress? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            addressData = requireArguments().getParcelable("addressData")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.clickHandle = this
        if (addressData != null) {
            setAddressData(addressData!!)
        }

    }

    private fun setAddressData(addressData: AddAddress) {
        binding.locationET.setText(addressData.primaryAddress!!)
        binding.landMarkET.setText(addressData.secondaryAddress!!)
        binding.zipCodeET.setText(addressData.zipcode!!)
        binding.cityET.setText(addressData.city)
        binding.phoneET.setText(addressData.contactNo)
    }


    private fun hitAddAddressApi() {
        val params = Api3Params()
        params.put("Address[first_name]", baseActivity!!.getProfileData().firstName)
        params.put("Address[last_name]", baseActivity!!.getProfileData().firstName)
        params.put("Address[primary_address]", binding.locationET.checkString())
        params.put("Address[secondary_address]", binding.landMarkET.checkString())
        params.put("Address[zipcode]", binding.zipCodeET.checkString())
        params.put("Address[city]", binding.cityET.checkString())
        params.put("Address[contact_no]", binding.phoneET.checkString())
        if (addressData != null) {
            val call = api!!.apiUpdateAddress(addressData!!.id!!, params.getServerHashMap())
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        } else {
            val call = api!!.apiAddAddress(params.getServerHashMap())
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.API_ADD_ADDRESS) || responseUrl.contains(Const.API_UPDATE_ADDRESS)) {
                if (responseCode == Const.STATUS_OK) {
                    if (jsonObject.has("message")) {
                        baseActivity!!.showToastOne(jsonObject.getString("message"))
                    }
                    baseActivity!!.onBackPressed()
                }
            }
        } catch (e: JSONException) {
            handleException(e)
        }
    }


    private fun isValid(): Boolean {
        when {
            binding.locationET.isBlank() -> baseActivity!!.showToastOne(getString(R.string.please_enter_address))
            binding.cityET.isBlank() -> baseActivity!!.showToastOne(getString(R.string.please_enter_city))
            binding.zipCodeET.isBlank() -> baseActivity!!.showToastOne(getString(R.string.please_enter_zipcode))
            binding.phoneET.isBlank() -> baseActivity!!.showToastOne(getString(R.string.plz_enter_phone_number))
            binding.phoneET.length() < 6 -> baseActivity!!.showToastOne(getString(R.string.plz_enter_valid_mobile_no))
            else -> {
                return true
            }
        }
        return false
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.addBT -> {
                if (isValid()) {
                    hitAddAddressApi()
                }
            }
        }
    }

}