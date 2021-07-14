package com.tracolfood.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentCharityListBinding
import com.tracolfood.model.CharityData
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.CharityAdapter
import com.tracolfood.ui.base.BaseAdapter
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.handleException
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.sin

class CharityListFragment : BaseFragment(), BaseAdapter.OnPageEndListener {
    private lateinit var binding: FragmentCharityListBinding
    private var adapter: CharityAdapter? = null
    private var arrayList: ArrayList<CharityData> = ArrayList()
    private var singleHit = false
    private var pageCount = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (baseActivity as MainActivity).setToolBar(isShowToolbar = true, isShow = false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_charity_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        clearData()
        hitCharityListApi()
    }

    private fun clearData() {
        arrayList.clear()
        adapter = null
        singleHit = false
        pageCount = 0
    }


    private fun hitCharityListApi() {
        if (!singleHit) {
            val call = api!!.apiCharityList(pageCount)
            baseActivity!!.restFullClient!!.sendRequest(call, this)
        }
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            if (responseUrl.contains(Const.API_CHARITY_LIST)) {
                if (responseCode == Const.STATUS_OK) {
                    for (i in 0 until jsonObject.getJSONArray("list").length()) {
                        val charityData = Gson().fromJson(jsonObject.getJSONArray("list").getJSONObject(i).toString(), CharityData::class.java)
                        arrayList.add(charityData)
                    }
                    pageCount++
                    singleHit = jsonObject.getJSONObject("_meta").getInt("pageCount") <= pageCount
                    setAdapter()
                }
            }

        } catch (e: JSONException) {
            handleException(e)
        }
    }

    private fun setAdapter() {
        if (adapter == null) {
            adapter = CharityAdapter(baseActivity!!, arrayList)
            adapter!!.setOnPageEndListener(this)
            binding.charityRV.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onPageEnd(vararg itemData: Any) {
        hitCharityListApi()
    }
}