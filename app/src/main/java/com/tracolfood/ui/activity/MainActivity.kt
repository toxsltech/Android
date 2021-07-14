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

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.tracolfood.R
import com.tracolfood.databinding.ActivityMainBinding
import com.tracolfood.model.DrawerData
import com.tracolfood.ui.adapter.DrawerAdapter
import com.tracolfood.ui.base.BaseActivity
import com.tracolfood.ui.fragment.*
import com.tracolfood.ui.fragment.authentication.ChangePasswordFragment
import com.tracolfood.ui.fragment.authentication.EditProfileFragment
import com.tracolfood.ui.fragment.authentication.ProfileFragment
import com.tracolfood.ui.fragment.authentication.SignUpFragment
import com.tracolfood.ui.fragment.pages.AboutUsFragment
import com.tracolfood.ui.fragment.pages.PrivacyFragment
import com.tracolfood.ui.fragment.query.ContactUsFragment
import com.tracolfood.utils.Const
import com.tracolfood.utils.GoogleApisHandle
import com.tracolfood.utils.extensions.replaceFragment
import com.tracolfood.utils.extensions.replaceFragmentWithoutStack
import com.tracolfood.utils.extensions.showAlertDialog
import com.tracolfood.utils.extensions.visibleView
import org.json.JSONObject
import java.util.*


class MainActivity : BaseActivity(), BaseActivity.PermCallback, BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var toggle: ActionBarDrawerToggle? = null
    private val drawerItems = ArrayList<DrawerData>()
    private var drawerAdapter: DrawerAdapter? = null
    private var fromMain: Boolean = false
    private var lat: String = ""
    private var lang: String = ""
    private var count: Int = 0
    private var gpsAlert: AlertDialog? = null


    private var seekerDrawerListener: AdapterView.OnItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        when (position) {
            0 -> {
                setBottomSelection()
                binding.bottomBNV.visibility = View.VISIBLE
                replaceFragmentWithoutStack(HomeFragment())
            }
            1 -> {
                binding.bottomBNV.visibility = View.GONE
                replaceFragmentWithoutStack(MyOrderFragment())
            }

            2 -> {
                val bundle = Bundle()
                bundle.putBoolean("fromMain", true)
                binding.bottomBNV.visibility = View.GONE
                replaceFragmentWithoutStack(AddressListFragment(), args = bundle)
            }
            3 -> {
                val bundle = Bundle()
                bundle.putBoolean("fromMain", true)
                replaceFragmentWithoutStack(CharityListFragment(), args = bundle)
            }
            4 -> {
                replaceFragmentWithoutStack(VideosFragment())
            }
            5 -> {
                setBottomSelection(R.id.wish)
                replaceFragmentWithoutStack(WishListFragment())
            }
            6 -> {
                replaceFragmentWithoutStack(SettingFragment())
            }
            7 -> {
                replaceFragmentWithoutStack(AboutUsFragment())
            }
            8 -> {
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.terms_conditions))
                bundle.putInt("typeId", Const.StaticPage.TYPE_TERMS)
                replaceFragmentWithoutStack(PrivacyFragment(), args = bundle)
            }
            9 -> {
                val bundle = Bundle()
                bundle.putString("title", getString(R.string.privacy_policy))
                bundle.putInt("typeId", Const.StaticPage.TYPE_PRIVACY)
                replaceFragmentWithoutStack(PrivacyFragment(), args = bundle)
            }
            10 -> {
                binding.bottomBNV.visibility = View.GONE
                replaceFragmentWithoutStack(ContactUsFragment())
            }
        }
        binding.drawer.closeDrawers()
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Const.DISPLAY_MESSAGE_ACTION -> {
                    val bundle = intent.getBundleExtra("detail")
                    bundle?.let {
                        openNotificationDialog(bundle)
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.searchET.setOnClickListener {
            replaceFragment(SearchFragment())
        }
        initToolbar()
        initDrawer()
        init()
    }


    fun setBottomSelection(id: Int = R.id.home) {
        binding.bottomBNV.selectedItemId = id
    }

    fun setSearchOpen(isOpen: Boolean = false) {
        if (isOpen) {
            binding.searchET.visibility = View.VISIBLE
        } else {
            binding.searchET.visibility = View.GONE
        }

    }

    private fun init() {
        googleApisHandle = let { GoogleApisHandle.getInstance(it) }
        binding.logoutTV.setOnClickListener(this)
        binding.profileIV.setOnClickListener {
            replaceFragmentWithoutStack(ProfileFragment())
            binding.bottomBNV.selectedItemId = R.id.profile
            binding.drawer.closeDrawers()
        }
        binding.bottomBNV.setOnNavigationItemSelectedListener(this)
        val bottomNavigationViewBackground = binding.bottomBNV.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
                bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                        .setAllCorners(CornerFamily.ROUNDED, 100f)
                        .build()

        goToHomeFragment()


        toggle = object : ActionBarDrawerToggle(this, binding.drawer, null,
                R.string.app_name, R.string.app_name) {

            @SuppressLint("RestrictedApi")
            override fun onDrawerOpened(drawerView: View) {
                invalidateOptionsMenu()
            }

            @SuppressLint("RestrictedApi")
            override fun onDrawerClosed(drawerView: View) {
                invalidateOptionsMenu()
            }
        }
        binding.drawer.addDrawerListener(toggle!!)
        updateDrawer()

    }

    fun updateDrawer() {
        binding.nameTV.text = getProfileData().fullName
        Glide.with(this).load(Const.IMAGE_SERVER_URL + getProfileData().profileFile).placeholder(R.mipmap.ic_userdummy).into(binding.profileIV)
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle!!.syncState()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle!!.onOptionsItemSelected(item)) {
            return true
        }
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


    private fun initDrawer() {
        val names = resources.getStringArray(R.array.drawer_items)
        for (i in names.indices) {
            val drawerData = DrawerData()
            drawerData.name = names[i]
            drawerItems.add(drawerData)
        }
        drawerAdapter = DrawerAdapter(this, 0, drawerItems)
        binding.drawerLV.adapter = drawerAdapter
        binding.drawerLV.onItemClickListener = seekerDrawerListener
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setSearchOpen(false)
        if (fragment is ChangePasswordFragment ||
                fragment is AddBankDetailFragment ||
                fragment is SubCategoryListFragment || fragment is DoctorFragment || fragment is DoctorFragment || fragment is AddAddressFragment
                || fragment is SubCategoryDetailFragment || fragment is EditProfileFragment || fragment is SignUpFragment || fragment is CheckOutFragment || fragment is PaymentDetailFragment || fragment is OrderDetailFragment || fragment is CharityFragment) {
            toggle!!.isDrawerIndicatorEnabled = false
            supportActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_backblack)
            binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.White))
            binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            toggle!!.isDrawerIndicatorEnabled = true
            binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            if (fragment is PaymentFragment) {
                supportActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_menudrop)
                binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            } else {
                supportActionBar!!.setHomeAsUpIndicator(R.mipmap.ic_drawer)
                binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.White))
            }

        }
        if (fragment is CharityFragment) {
            binding.centerLogoIV.visibility = View.VISIBLE
        } else {
            binding.centerLogoIV.visibility = View.GONE
        }
        return super.onCreateOptionsMenu(menu)
    }


    fun setToolBar(isShowToolbar: Boolean = true, text: String = "", isShow: Boolean = false) {
        if (isShowToolbar) {
            binding.toolbar.visibility = View.VISIBLE
            if (text.isNotEmpty()) {
                binding.titleTV.text = text
                binding.titleTV.setTextColor(ContextCompat.getColor(this, R.color.White))
            }
        } else {
            binding.toolbar.visibility = View.GONE
        }
        binding.bottomBNV.visibleView(isShow)
    }

    override fun permGranted(resultCode: Int) {
        getAndSetCurrentLocation()
    }

    override fun permDenied(resultCode: Int) {

    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (!(fragment is ChangePasswordFragment ||
                        fragment is AddBankDetailFragment ||
                        fragment is SubCategoryListFragment
                        || fragment is SubCategoryDetailFragment
                        || fragment is EditProfileFragment
                        || fragment is DoctorFragment
                        || fragment is AddAddressFragment
                        || fragment is OrderDetailFragment
                        || fragment is CharityFragment
                        || fragment is PaymentDetailFragment
                        || fragment is PaymentDetailFragment
                        || fragment is ViewVideoFragment
                        || fragment is CheckOutFragment
                        || fragment is SignUpFragment
                        )) {
            backAction()
        } else {
            when {
                supportFragmentManager.backStackEntryCount > 0 -> {
                    supportFragmentManager.popBackStack()
                }
                else -> {

                }

            }
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> goToHomeFragment()
            R.id.wish -> replaceFragmentWithoutStack(WishListFragment())
            R.id.cart -> replaceFragmentWithoutStack(CartFragment())
            R.id.notification -> replaceFragmentWithoutStack(NotificationFragment())
            R.id.profile -> replaceFragmentWithoutStack(ProfileFragment())
            else -> return false
        }
        return true
    }


    override fun onPause() {
        super.onPause()
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        } catch (e: Exception) {
            handelException(e)
        }
    }

    override fun onResume() {
        super.onResume()
        val intent = IntentFilter()
        intent.addAction(Const.DISPLAY_MESSAGE_ACTION)
        try {

            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intent)
        } catch (e: Exception) {
            handelException(e)
        }
        getAndSetCurrentLocation()

    }


    private fun checkAction(bundle: Bundle, broadcast: Boolean) {

        if (restFullClient?.getLoginStatus() != null) {
            val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.container)
            val controller = bundle.get("controller") as String
            val action = bundle.get("action") as String
            val detailObject = if (bundle.containsKey("detail")) {
                JSONObject(bundle.get("detail") as String)
            } else {
                JSONObject()
            }
            when (controller) {
                "front" -> {
                    when (action) {
                        "step-two" -> {
                        }
                        else -> {
                        }
                    }
                }

                else -> {
                }

            }
        } else {
            gotoLoginSignUpActivity()
        }
        clearNotification()

    }

    private fun clearNotification() {
        val nMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        nMgr?.cancelAll()
    }

    private fun openNotificationDialog(bundle: Bundle) {
        showAlertDialog(message = (bundle.get("message") as String), title = getString(R.string.app_name), postiveBtnText = getString(android.R.string.ok), negativeBtnText = getString(R.string.dismiss).toUpperCase(Locale.getDefault()), handleClick = { positiveClick ->
            if (positiveClick) {
                checkAction(bundle, false)
            } else {
                clearNotification()
            }
        })
    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.homeTV -> {
                goToHomeFragment()
            }
            R.id.logoutTV -> {
                this.showAlertDialog(getString(R.string.are_you_sure_want_to_logout), postiveBtnText = "Yes", handleClick = {
                    if (it) {
                        hitLogoutAPI()
                    }
                })

            }

        }
        binding.drawer.closeDrawers()
    }

    private fun hitLogoutAPI() {
        val call = api!!.apiLogout()
        restFullClient?.sendRequest(call, this)
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            when (responseUrl) {
                Const.Login.API_LOGOUT -> {
                    roomUtils!!.deleteAll(Const.DELETE_ALL, this)
                    restFullClient?.setLoginStatus(null)
                    store?.saveProfileData(null)
                    gotoLoginSignUpActivity()

                }
            }
        } catch (e: Exception) {
            handelException(e)
        }
    }


    private fun goToHomeFragment() {
        replaceFragmentWithoutStack(HomeFragment())
    }

    private fun getAndSetCurrentLocation() {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (checkPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), Const.PermissionConst.PERMISSION_LOCATION_CODE, this)) {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val currentLocation = googleApisHandle!!.getLastKnownLocation(this)
                if (currentLocation != null) {
                    lat = currentLocation.latitude.toString()
                    lang = currentLocation.longitude.toString()
                } else {
                    if (count < 5) {
                        count++
                        getAndSetCurrentLocation()
                    } else {
                        showToastOne(getString(R.string.not_able_to_get_latlong))
                    }
                }
            } else {
                buildAlertMessageNoGps(this)
            }
        }
    }


    private fun buildAlertMessageNoGps(activity: Activity?) {
        if (activity != null && !activity.isFinishing) {
            if (gpsAlert != null && gpsAlert!!.isShowing) {
                gpsAlert!!.dismiss()
            }
            val alert = AlertDialog.Builder(activity)
            alert.setMessage(getString(R.string.enable_gps))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), fun(_: Any, _: Any) {
                        startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Const.PermissionConst.SOURE)
                    })
                    .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            gpsAlert = alert.create()
            gpsAlert!!.show()
        }
    }


}
