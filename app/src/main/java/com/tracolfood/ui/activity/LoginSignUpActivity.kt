/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.tracolfood.R
import com.tracolfood.databinding.ActivityLoginBinding
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.fragment.authentication.*
import com.tracolfood.ui.fragment.pages.PrivacyFragment
import com.tracolfood.utils.extensions.replaceFragment
import com.tracolfood.utils.extensions.visibleView


class LoginSignUpActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        initToolbar()
        gotoLoginFragment()
    }

    private fun gotoLoginFragment() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replaceFragment(LoginFragment())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    fun setToolBar(title: String = "", isShowToolbar: Boolean = true) {
        binding.toolbar.visibleView(isShowToolbar)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        supportActionBar!!.setHomeButtonEnabled(true)
        if (fragment is EditProfileFragment) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//            supportActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_white_bck)

            binding.toolbar.background = getDrawable(R.drawable.action_bar_gredient)
        } else if (fragment is PrivacyFragment) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//            supportActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_bck_arw)
            binding.toolbar.background = getDrawable(R.color.White)
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            setToolBar(isShowToolbar = false)

        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        when {
            fragment is LoginFragment -> {
                backAction()
            }
            supportFragmentManager.backStackEntryCount > 0 -> {
                supportFragmentManager.popBackStack()
            }
            else -> {
                gotoLoginFragment()
            }
        }
    }

}
