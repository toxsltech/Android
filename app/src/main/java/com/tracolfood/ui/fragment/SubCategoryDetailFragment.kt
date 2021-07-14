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
import android.text.Html
import android.view.*
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.gson.Gson
import com.toxsl.restfulClient.api.Api3Params
import com.tracolfood.R
import com.tracolfood.databinding.FragmentSubCategoryDetailBinding
import com.tracolfood.model.CartProductData
import com.tracolfood.model.ProductData
import com.tracolfood.room.RoomUtils
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.handleException
import com.tracolfood.utils.extensions.replaceFragment
import org.json.JSONException
import org.json.JSONObject


class SubCategoryDetailFragment : BaseFragment(), ViewClickHandler, RoomUtils.roomCallbackListener {
    private var productData: ProductData? = null
    private lateinit var binding: FragmentSubCategoryDetailBinding
    private var productId = 0
    private var productType = 0
    private var cartData: ArrayList<CartProductData>? = ArrayList()
    private var selectedPos = 0
    private var selectedQuantity = 0


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(true, "", false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_category_detail, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            productId = requireArguments().getInt("id")
            productType = requireArguments().getInt("type")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.clickHandle = this
        binding.favIV.setOnClickListener {
            if (productType == Const.TYPE_PRODUCT) {
                hitAddToWishListApi(productId)
            } else {
                hitPackageFavoriteApi(productId)
            }
        }
        binding.cartIV.setOnClickListener {
            (baseActivity as MainActivity).setBottomSelection(R.id.cart)
            baseActivity!!.replaceFragment(CartFragment())
        }


        if (productType == Const.TYPE_PRODUCT) {
            hitProductDetailApi()
        } else {
            hitPackageDetailApi()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onHandleClick(view: View) {

        when (view.id) {
            R.id.buyNowBT -> {
                onClickCartFunction(Const.ADD_PRODUCT, selectedPos, cartData!!)
            }
            R.id.plusTV -> {
                cartData!![selectedPos].addedQuantity = selectedQuantity + 1
                onClickCartFunction(Const.UPDATE_QUANTITY, selectedPos, cartData!!)
            }
            R.id.minusTV -> {
                quantityDecreaseHandling()
            }
            R.id.cartIV -> {

            }
        }
    }

    private fun quantityDecreaseHandling() {
        if (cartData!![selectedPos].addedQuantity!! > 1) {
            cartData!![selectedPos].addedQuantity = selectedQuantity - 1
            onClickCartFunction(Const.UPDATE_QUANTITY, selectedPos, cartData!!)
        } else {
            baseActivity!!.roomUtils!!.deleteProduct(cartData!![selectedPos], Const.DELETE_PRODUCT, this)
        }
    }


    override fun cartData(data: CartProductData?) {
    }


    private fun hitAddToWishListApi(productId: Int) {
        val params = Api3Params()
        params.put("Item[id]", productId)
        val call = api!!.apiHitAddToWishList(params.getServerHashMap())
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }

    private fun hitPackageFavoriteApi(productId: Int) {
        val params = Api3Params()
        params.put("Item[id]", productId)

        val call = api!!.apiFavoritePackage(params.getServerHashMap())
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }


    override fun onSuccess() {
    }

    override fun onDataReceived(vararg objects: Any?) {
        if (objects.isNotEmpty()) {
            val type: Int = objects[0] as Int
            val isSuccess: Boolean = objects[1] as Boolean
            when (type) {
                Const.UPDATE_QUANTITY -> {
                    baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
                }
                Const.GET_ALL_ITEM -> {
                    cartData!!.clear()
                    if (objects[2] != null && (objects[2] as? ArrayList<CartProductData>?)?.size!! > 0) {
                        cartData = objects[2] as? ArrayList<CartProductData>
                    }
                    setData(productData!!)
                }
                Const.ADD_PRODUCT -> {
                    getCartData()
                    showToastOne(baseActivity!!.getString(R.string.product_added_to_cart_successfully))
                }
                Const.DELETE_PRODUCT -> {
                    if (isSuccess) {
                        binding.plusCV.visibility = View.GONE
                        binding.countTV.visibility = View.GONE
                        binding.minusCV.visibility = View.GONE
                        binding.buyNowBT.visibility = View.VISIBLE
                        baseActivity!!.showToastOne(baseActivity!!.getString(R.string.product_removed_from_cart))
                    }
                }
            }
        }
    }

    private fun getCartData() {
        if (baseActivity!!.roomUtils != null) {
            baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
        }
    }


    private fun hitProductDetailApi() {
        val call = api!!.apiProductDetail(productId)
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }

    private fun hitPackageDetailApi() {
        val call = api!!.apiPackageDetail(productId)
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.Fruits.API_PRODUCT_DETAIL)) {
                if (responseCode == Const.STATUS_OK) {
                    productData = Gson().fromJson(jsonObject.getJSONObject("details").toString(), ProductData::class.java)
                    if (productData!!.isFavorite!!) {
                        binding.favIV.setImageResource(R.mipmap.ic_fav)
                    } else {
                        binding.favIV.setImageResource(R.mipmap.ic_like)
                    }
                    baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
                }
            } else if (responseUrl.contains(Const.API_PACKAGE_DETAIL)) {
                if (responseCode == Const.STATUS_OK) {
                    productData = Gson().fromJson(jsonObject.getJSONObject("details").toString(), ProductData::class.java)
                    if (productData!!.isFavorite!!) {
                        binding.favIV.setImageResource(R.mipmap.ic_fav)
                    } else {
                        binding.favIV.setImageResource(R.mipmap.ic_like)
                    }
                    baseActivity!!.roomUtils!!.getAll(Const.GET_ALL_ITEM, this)
                }
            } else if (responseUrl.contains(Const.Fruits.API_ADD_WISH_LIST)) {
                if (responseCode == Const.STATUS_OK) {
                    if (jsonObject.has("message")) {
                        if (jsonObject.getString("message").contains("Remove")) {
                            binding.favIV.setImageResource(R.mipmap.ic_like)
                        } else {
                            binding.favIV.setImageResource(R.mipmap.ic_fav)
                        }
                        showToastOne(jsonObject.getString("message"))
                    }
                }
            }

        } catch (e: JSONException) {
            handleException(e)
        }
    }

    private fun setData(data: ProductData) {
        binding.itemNameTV.text = data.title
        binding.priceTV.text = "RM ${data.amount}"
        binding.descTV.text = Html.fromHtml(data.description)
        binding.subCategoryTV.text = data.title
        Glide.with(baseActivity!!).load(Const.IMAGE_SERVER_URL + data.imageFile).error(R.mipmap.ic_youtube).into(binding.imageIV)
        binding.imageIV.shapeAppearanceModel = binding.imageIV.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 30f)
                .build()
        for (i in 0 until cartData!!.size) {
            if (checkProductAdded(i, data)) return
        }
    }

    private fun checkProductAdded(i: Int, data: ProductData): Boolean {
        if (cartData!![i].productId == data.id) {
            this.selectedPos = i
            binding.plusCV.visibility = View.VISIBLE
            binding.countTV.visibility = View.VISIBLE
            binding.minusCV.visibility = View.VISIBLE
            binding.buyNowBT.visibility = View.GONE
            selectedQuantity = cartData!![i].addedQuantity!!
            binding.countTV.text = cartData!![i].addedQuantity.toString()
            return true
        }
        return false
    }


    private fun onClickCartFunction(type: Int, position: Int, list: ArrayList<CartProductData>) {
        when (type) {
            Const.UPDATE_QUANTITY -> {
                baseActivity!!.roomUtils!!.addData(list[position], type, this)
            }
            Const.DELETE_PRODUCT -> {
                baseActivity!!.roomUtils!!.deleteProduct(list[position], type, this)
            }
            Const.ADD_PRODUCT -> {
                val data = CartProductData()
                data.productId = productData!!.id!!
                data.sellerId = baseActivity!!.getProfileData().id
                data.itemType = productType
                data.addedQuantity = 1
                data.productName = productData!!.title
                data.price = productData!!.amount
                data.imageFile = productData!!.imageFile
                when (type) {
                    Const.ADD_PRODUCT -> {
                        baseActivity!!.roomUtils!!.getCartCount(Const.GET_COUNT, this)
                        baseActivity!!.roomUtils!!.addData(data, Const.ADD_PRODUCT, this)
                        baseActivity!!.store?.setInt("productId", productData!!.id!!)
                    }
                }
            }
        }
    }


}