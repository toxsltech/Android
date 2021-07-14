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

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.tracolfood.R
import com.tracolfood.databinding.FragmentSignUpBinding
import com.tracolfood.ui.activity.LoginSignUpActivity
import com.tracolfood.ui.activity.MainActivity
import com.tracolfood.ui.base.BaseFragment
import com.tracolfood.ui.fragment.HomeFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.extensions.replaceFragmentWithoutStack


class SignUpFragment : BaseFragment() {
    private lateinit var binding: FragmentSignUpBinding
    private var url = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        if (baseActivity is MainActivity)
            (baseActivity as MainActivity).setToolBar(false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            url = requireArguments().getString("url")!!
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.settings.let {
            it?.javaScriptEnabled = true
            it.loadsImagesAutomatically = true
            it.loadWithOverviewMode = true
        }
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    log(request?.url?.toString() ?: "")
                    view?.loadUrl(request?.url?.toString()!!)
                } else {
                    super.shouldOverrideUrlLoading(view, request)
                }
                return true
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url!!.contains("msg")) {
                    val string = url.substringAfter("msg=", "&").substringBeforeLast("&order_id")
                    if (string.contains("_")) {
                        showToastOne(string.replace("_", " "))
                    } else {
                        showToastOne(string)
                    }
                }
                when {
                    url.contains(Const.CHECK_PROFILE) -> {
                        if (baseActivity is LoginSignUpActivity) {
                            baseActivity!!.replaceFragmentWithoutStack(LoginFragment())
                        } else {
                            baseActivity!!.replaceFragmentWithoutStack(HomeFragment())
                        }
                    }
                    url.contains("status_id=0") -> {
                        baseActivity!!.replaceFragmentWithoutStack(HomeFragment())
                    }
                }
                log("on Page Finished >>>> $url")
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    log("onReceived Error >>> ${error?.description?.toString()}")
                }
            }
        }
        binding.webView.loadUrl(url)
    }


}