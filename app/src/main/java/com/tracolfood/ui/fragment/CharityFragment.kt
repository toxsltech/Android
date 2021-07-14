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

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.toxsl.restfulClient.api.Api3Params
import com.tracolfood.R
import com.tracolfood.databinding.DialogEnterAmountBinding
import com.tracolfood.databinding.FragmentCharityBinding
import com.tracolfood.model.CharityData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.ui.fragment.authentication.SignUpFragment
import com.tracolfood.ui.fragment.pages.PrivacyFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.checkString
import com.tracolfood.utils.extensions.handleException
import com.tracolfood.utils.extensions.isBlank
import com.tracolfood.utils.extensions.replaceFragWithArgs
import org.json.JSONException
import org.json.JSONObject


class CharityFragment : BaseFragment(), ViewClickHandler {
    private lateinit var binding: FragmentCharityBinding
    private var listDialog: AlertDialog? = null
    private var url = ""
    private var charityData: CharityData? = null
    private var isDialogOpen = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            charityData = requireArguments().getParcelable("charityData")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_charity, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.clickHandle = this
        setData()
        if (arguments != null) {
            (baseActivity as MainActivity).setToolBar(isShowToolbar = true, isShow = false)
            binding.topRL.visibility = View.GONE
        } else {
            (baseActivity as MainActivity).setToolBar(isShowToolbar = false, isShow = false)
            binding.topRL.visibility = View.VISIBLE
        }
    }

    private fun setData() {
        binding.urgentTV.text = charityData!!.title
        binding.commentTV.text = Html.fromHtml(charityData!!.description)
        url = charityData!!.url!!
        binding.infoTV.text = baseActivity!!.getString(R.string.rm_sign).plus(charityData!!.raisedAmount).plus(getString(R.string.raised_of)).plus(baseActivity!!.getString(R.string.rm_sign)).plus(charityData!!.goalAmount)
        Glide.with(baseActivity!!).load(Const.IMAGE_SERVER_URL + charityData!!.imageFile).error(R.mipmap.ic_youtube).into(binding.imageIV)
        if (isDialogOpen) {
            openEnterAmount()
        }


    }

    private fun shareIntent() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, baseActivity!!.getString(R.string.app_name))
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Const.IMAGE_SERVER_URL + url)
        startActivity(Intent.createChooser(sharingIntent, baseActivity!!.getString(R.string.share_using)))
    }

    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.backIV -> {
                baseActivity!!.supportFragmentManager.popBackStack()
            }
            R.id.shareBT -> {
                shareIntent()
            }
            R.id.donateBT -> {
                openEnterAmount()
            }
        }
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.Charity.API_CHARITY_DETAIL)) {
                if (responseCode == Const.STATUS_OK) {
                    val detail = jsonObject.getJSONObject("detail")

                }
            } else if (responseUrl.contains(Const.Charity.API_CHARITY_AMOUNT)) {
                if (responseCode == Const.STATUS_OK) {
                    if (listDialog != null)
                        listDialog!!.dismiss()
                    val bundle = Bundle()
                    bundle.putString("url", jsonObject.getJSONObject("detail").getString("payment_url"))
                    baseActivity!!.replaceFragWithArgs(SignUpFragment(), args = bundle)
                }
            }

        } catch (e: JSONException) {
            handleException(e)
        }
    }


    private fun openEnterAmount() {
        val builder = AlertDialog.Builder(baseActivity!!)
        val dialogCountryBinding = DataBindingUtil.inflate<DialogEnterAmountBinding>(LayoutInflater.from(baseActivity), R.layout.dialog_enter_amount, null, false)
        builder.setView(dialogCountryBinding.root)
        listDialog = builder.create()
        listDialog!!.setCancelable(true)
        listDialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        listDialog!!.show()
        dialogCountryBinding!!.termsTV.setOnClickListener {
            listDialog!!.dismiss()
            isDialogOpen = true
            val bundle = Bundle()
            bundle.putString("title", getString(R.string.terms_conditions))
            bundle.putBoolean("isPayment", true)
            bundle.putInt("typeId", Const.StaticPage.TYPE_TERMS)
            baseActivity!!.replaceFragWithArgs(PrivacyFragment(), args = bundle)

        }
        dialogCountryBinding.donateBT.setOnClickListener {
            when {
                !dialogCountryBinding.termsCB.isChecked -> baseActivity!!.showToastOne(baseActivity!!.getString(R.string.please_agree_to_terms_conditions))
                dialogCountryBinding.enterAmountET.isBlank() -> baseActivity!!.showToastOne(baseActivity!!.getString(R.string.please_enter_amount))
                dialogCountryBinding.enterAmountET.equals("0") ->baseActivity!!.showToastOne("")
                else -> hitDonationPriceApi(dialogCountryBinding.enterAmountET.checkString())
            }
        }
        dialogCountryBinding.crossIV.setOnClickListener {
            listDialog!!.dismiss()
        }
    }

    private fun hitDonationPriceApi(amount: String) {
        val params = Api3Params()
        params.put("CharityDetail[amount]", amount)
        params.put("CharityDetail[charity_id]", charityData!!.id!!)
        val call = api!!.apiCharityAmount(params.getServerHashMap())
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }


}