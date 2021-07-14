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
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tracolfood.R
import com.tracolfood.databinding.FragmentProfileBinding
import com.tracolfood.model.UserDetail
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.adapter.AddressAdapter
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.ViewClickHandler
import com.tracolfood.utils.extensions.loadImageUrl
import com.tracolfood.utils.extensions.replaceFragment
import org.json.JSONObject
import java.io.File

class ProfileFragment : BaseFragment(), BaseActivity.PermCallback, ViewClickHandler {

    private var imageFile: File? = null
    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (baseActivity as MainActivity).setToolBar(isShowToolbar = true, isShow = true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding!!.clickHandle = this
        setHasOptionsMenu(true)
        clickListener()
        setProfileData(baseActivity!!.getProfileData())
    }

    private fun setProfileData(profileData: UserDetail) {
        binding!!.nameTV.text = profileData.fullName
        binding!!.emailValueTV.text = profileData.email
        binding!!.phoneValueTV.text = profileData.contactNo
        Glide.with(baseActivity!!).load(Const.IMAGE_SERVER_URL + profileData.profileFile).placeholder(R.mipmap.ic_userdummy).into(binding!!.profileIV)

    }


    private fun clickListener() {
        binding!!.editIV.setOnClickListener(this)
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            when (responseUrl) {
                Const.Profile.API_PROFILE_IMAGE_REMOVE -> {
                    val userData = Gson().fromJson(jsonObject.getJSONObject("detail").toString(), UserDetail::class.java)
                    baseActivity!!.saveProfileData(userData)
                    userData.profileFile = ""
                }
                Const.Profile.API_UPDATE_PROFILE -> {
                    val userData = Gson().fromJson(jsonObject.getJSONObject("detail").toString(), UserDetail::class.java)
                    baseActivity!!.saveProfileData(userData)
                    showToastOne(getString(R.string.profile_updated_successfully))
                }
            }
        } catch (e: Exception) {
            baseActivity!!.handelException(e)
        }


    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.editIV -> {
                selectImage()
            }

        }
    }


    private fun selectImage() {
        if (baseActivity!!.checkPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), Const.PermissionConst.IMAGE_CODE, this)) {
            chooseImagePicker(Const.PermissionConst.IMAGE_CODE, false)
        }
    }

    override fun onImageSelected(uri: Uri?, requestCode: Int) {
        super.onImageSelected(uri, requestCode)
        if (requestCode == Const.PermissionConst.IMAGE_CODE) {
            binding!!.profileIV.setImageURI(uri)
            imageFile = File(uri?.path!!)
        }
    }

    override fun onImageRemoved(isRemoved: Boolean, requestCode: Int) {
        super.onImageRemoved(isRemoved, requestCode)
        if (requestCode == Const.PermissionConst.IMAGE_CODE && isRemoved) {
            imageRemoveApi()
        }
    }

    private fun imageRemoveApi() {
        val call = api!!.apiProfileImageRemove()
        baseActivity!!.restFullClient!!.sendRequest(call, this)
        imageFile = null
    }

    override fun permGranted(resultCode: Int) {
        if (resultCode == Const.PermissionConst.IMAGE_CODE) {
            selectImage()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                baseActivity!!.replaceFragment(EditProfileFragment())
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun permDenied(resultCode: Int) {
    }

    override fun onHandleClick(view: View) {
        when (view.id) {

        }


    }

}