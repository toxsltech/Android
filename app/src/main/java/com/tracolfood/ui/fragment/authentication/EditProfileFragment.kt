/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.fragment.authentication

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.toxsl.restfulClient.api.Api2MultipartByte
import com.tracolfood.R
import com.tracolfood.databinding.FragmentEditProfileBinding
import com.tracolfood.model.UserDetail
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.checkString
import com.tracolfood.utils.extensions.loadImageUrl
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException

class EditProfileFragment : BaseFragment(), ViewClickHandler, BaseActivity.PermCallback {

    private var binding: FragmentEditProfileBinding? = null
    private var profileFile: File? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).setToolBar(isShowToolbar = false, isShow = false)
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding!!.clickHandle = this
        setData(baseActivity!!.getProfileData())
    }

    private fun setData(profileData: UserDetail) {
        binding!!.emailET.setText(profileData.email)
        binding!!.phoneET.setText(profileData.contactNo)
        binding!!.firstnameET.setText(profileData.fullName.substringBefore(" "))
        if (profileData.fullName.contains(" ")) {
            binding!!.lastNameET.setText(profileData.fullName.substring(profileData.fullName.lastIndexOf(" ") + 1))
        } else {
            binding!!.lastNameET.setText("")
        }
        binding!!.profileIV.loadImageUrl(profileData.profileFile, R.mipmap.ic_userdummy)
    }


    private fun hitUpdateProfileApi() {
        val params = Api2MultipartByte()
        params.put("User[first_name]", binding!!.firstnameET.checkString())
        params.put("User[last_name]", binding!!.lastNameET.checkString())
        params.put("User[city]", "")
        params.put("User[country]", "")
        params.put("User[contact_no]", binding!!.phoneET.checkString())
        try {
            params.put("User[profile_file]", profileFile)
        } catch (e: FileNotFoundException) {
            baseActivity!!.handelException(e)
        }
        val call = api!!.apiUpdateProfile(params.getRequestBody())
        baseActivity!!.restFullClient!!.sendRequest(call, this)
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            when (responseUrl) {

                Const.Profile.API_UPDATE_PROFILE -> {
                    when (responseCode) {
                        Const.STATUS_OK -> {
                            val data = Gson().fromJson(jsonObject.getJSONObject("detail").toString(), UserDetail::class.java)
                            baseActivity!!.saveProfileData(data)
                            (baseActivity as MainActivity).updateDrawer()
                            baseActivity!!.onBackPressed()
                            showToastOne(jsonObject.getString("message"))
                        }
                        else -> {
                            showToastOne(jsonObject.getString("message"))
                        }
                    }
                }
            }

        } catch (e: Exception) {
            baseActivity!!.handelException(e)
        }
    }


    override fun onHandleClick(view: View) {
        when (view.id) {
            R.id.backIV -> baseActivity!!.onBackPressed()
            R.id.saveBT -> hitUpdateProfileApi()
            R.id.camIV -> {
                if (baseActivity!!.checkPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), Const.PermissionConst.IMAGE_CODE, this)) {
                    chooseImagePicker(Const.PermissionConst.IMAGE_CODE, false)
                }
            }
        }
    }


    override fun permGranted(resultCode: Int) {
        chooseImagePicker(Const.PermissionConst.IMAGE_CODE, false)
    }


    override fun onImageSelected(uri: Uri?, requestCode: Int) {
        super.onImageSelected(uri, requestCode)
        Glide.with(baseActivity!!).load(uri).into(binding!!.profileIV)
        profileFile = File(uri!!.path!!)
    }

    override fun permDenied(resultCode: Int) {
    }

    override fun onSyncFailure(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        super.onSyncFailure(errorCode, t, response, call, callBack)


    }


}
