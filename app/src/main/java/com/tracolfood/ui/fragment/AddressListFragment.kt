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
import android.view.*
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.toxsl.restfulClient.api.Api3Params
import com.tracolfood.R
import com.tracolfood.databinding.FragmentAddressListBinding
import com.tracolfood.model.AddAddress
import com.tracolfood.model.CartProductData
import com.tracolfood.room.RoomUtils
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.AddressAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.ui.fragment.authentication.SignUpFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class AddressListFragment : BaseFragment(), BaseAdapter.OnPageEndListener, BaseAdapter.OnItemClickListener, RoomUtils.roomCallbackListener, ViewClickHandler {
    private lateinit var binding: FragmentAddressListBinding
    private var fromMain = false
    private var addressList: ArrayList<AddAddress> = ArrayList()
    private var cardProductList: ArrayList<CartProductData> = ArrayList()
    private var adapter: AddressAdapter? = null
    private var pageCount = 0
    private var singleHit = false
    private var addressId = 0
    private var selectedPos = 0
    private var totalPrice = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (requireArguments().containsKey("cardProductList")) {
                cardProductList = requireArguments().getParcelableArrayList<CartProductData>("cardProductList")!!
                totalPrice = requireArguments().getString("totalPrice")!!
            }
            if (requireArguments().containsKey("fromMain")) {
                fromMain = requireArguments().getBoolean("fromMain")
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(fromMain)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_list, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        setHasOptionsMenu(true)
        binding.clickHandle = this
        binding.selectBT.visibleView(!fromMain)
        binding.toolbarRL.visibleView(!fromMain)
        clearData()
        hitAddressListApi()
    }

    private fun clearData() {
        addressList.clear()
        pageCount = 0
        singleHit = false
        adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_black, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                baseActivity!!.replaceFragment(AddAddressFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hitAddressListApi() {
        if (!singleHit) {
            val call = api!!.apiAddressList(pageCount)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.API_ADDRESS_LIST)) {
                if (responseCode == Const.STATUS_OK) {
                    for (i in 0 until jsonObject.getJSONArray("list").length()) {
                        val addressList = Gson().fromJson(jsonObject.getJSONArray("list").getJSONObject(i).toString(), AddAddress::class.java)
                        this.addressList.add(addressList)
                    }
                    pageCount++
                    singleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount

                    setAdapter()
                    binding.noDataTV.visibleView(!(this.addressList.size > 0))
                }
            } else if (responseUrl.contains(Const.Orders.API_PLACE_ORDER)) {
                if (responseCode == Const.STATUS_OK) {
                    if (jsonObject.has("message")) {
                        baseActivity!!.showToastOne(jsonObject.getString("message"))
                    }
                    baseActivity!!.roomUtils!!.deleteAll(Const.DELETE_ALL, this)
                    val bundle = Bundle()
                    bundle.putString("url", jsonObject.getJSONObject("Order").getString("payment_url"))
                    baseActivity!!.replaceFragWithArgs(SignUpFragment(), args = bundle)
                }
            } else if (responseUrl.contains(Const.API_SET_DEFAULT_ADDRESS)) {
                if (responseCode == Const.STATUS_OK) {
                    if (jsonObject.has("message")) {
                        baseActivity!!.showToastOne(jsonObject.getString("message"))
                    }
                    addressList[selectedPos].isSelected = true
                }
            } else if (responseUrl.contains(Const.API_DELETE_ADDRESS)) {
                if (responseCode == Const.STATUS_OK) {
                    addressList.removeAt(selectedPos)
                    setAdapter()
                }
            }
        } catch (e: JSONException) {
            handleException(e)
        }
    }

    private fun setAdapter() {
        if (adapter == null) {
            adapter = AddressAdapter(baseActivity!!, addressList)
            adapter!!.setOnPageEndListener(this)
            adapter!!.setOnItemClickListener(this)
            binding.addressRV.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onPageEnd(vararg itemData: Any) {
        hitAddressListApi()
    }


    private fun jsonArray(): JSONArray {
        val jsonArray = JSONArray()
        if (cardProductList.size > 0) {
            for (i in 0 until cardProductList.size) {
                val jsonObject = JSONObject()
                jsonObject.put("item_id", cardProductList[i].productId)
                jsonObject.put("amount", cardProductList[i].price)
                jsonObject.put("quantity", cardProductList[i].addedQuantity)
                jsonObject.put("type_id", cardProductList[i].itemType)
                jsonArray.put(jsonObject)
            }
        }
        return jsonArray
    }

    private fun hitPlaceOrderApi(jsonArray: JSONArray) {
        val params = Api3Params()
        params.put("Order[payment_type]", 1)
        params.put("Order[address_id]", addressId)
        params.put("Order[total_amount]", totalPrice)
        params.put("itemJson", jsonArray)
        val call = api!!.apiPlaceOrder(params.getServerHashMap())
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }

    override fun onItemClick(vararg itemData: Any) {
        when (itemData[0] as Int) {
            Const.TYPE_SELECT -> {
                this.selectedPos = itemData[1] as Int
                hitDefaultAddressApi(itemData[1] as Int)
            }
            Const.TYPE_DELETE -> {
                this.selectedPos = itemData[1] as Int
                baseActivity!!.showAlertDialog(baseActivity!!.getString(R.string.are_you_sure_want_to_delete_your_address), postiveBtnText = "Yes", handleClick = {
                    if (it) {
                        deleteAddressApi(addressList[selectedPos].id!!)
                    }
                })
            }
            Const.TYPE_EDIT -> {
                val bundle = Bundle()
                bundle.putParcelable("addressData", addressList[selectedPos])
                baseActivity!!.replaceFragWithArgs(AddAddressFragment(), args = bundle)
            }
        }
    }


    private fun hitDefaultAddressApi(pos: Int) {
        val call = api!!.apiSetDefaultAddress(addressList[pos].id!!)
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }

    override fun cartData(data: CartProductData?) {

    }

    override fun onSuccess() {
    }

    override fun onDataReceived(vararg objects: Any?) {
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.selectBT -> {
                var isSelected = false
                if (cardProductList.size > 0)
                    for (i in 0 until addressList.size) {
                        isSelected = addressList[i].isDefault!! > 0
                        this.addressId = addressList[i].id!!
                        if (isSelected)
                            break
                    }
                if (isSelected) {
                    hitPlaceOrderApi(jsonArray())
                } else {
                    baseActivity!!.showToastOne(getString(R.string.please_add_atleast_one_address))
                }
            }
            R.id.backIV -> {
                baseActivity!!.supportFragmentManager.popBackStack()
            }
            R.id.addIV -> {
                baseActivity!!.replaceFragment(AddAddressFragment())
            }
        }
    }


    private fun deleteAddressApi(addressId: Int) {
        val call = api!!.apiDeleteAddress(addressId)
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }


}