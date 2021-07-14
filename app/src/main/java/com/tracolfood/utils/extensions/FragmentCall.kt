/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.utils.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.tracolfood.R

fun AppCompatActivity.replaceFragment(fragment: Fragment, args: Bundle? = null) {
    args?.let { fragment.arguments = args }
    supportFragmentManager.inTransaction {
        replace(R.id.container, fragment).addToBackStack(fragment.javaClass.name)
    }
}

fun AppCompatActivity.replaceFragmentWithoutStack(fragment: Fragment, frameId: Int = R.id.container, args: Bundle? = null) {
    args?.let { fragment.arguments = args }
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
    }
}


fun AppCompatActivity.replaceFragmentWithTag(fragment: Fragment, frameId: Int = R.id.container, args: Bundle? = null) {
    args?.let { fragment.arguments = args }

    val fragmentPopped = supportFragmentManager.popBackStackImmediate(fragment.javaClass.name, 0)
    if (!fragmentPopped) {
        supportFragmentManager.beginTransaction()
                .replace(frameId, fragment).addToBackStack(fragment.javaClass.name).commitAllowingStateLoss()
    } else {
        val currentFrag = supportFragmentManager.findFragmentById(frameId)
        //update that fragment ui
    }

}

fun androidx.fragment.app.FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int = R.id.container, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        add(frameId, fragment)
        backStackTag?.let {
            addToBackStack(fragment.javaClass.name)
        }!!
    }
}

fun AppCompatActivity.replaceFragWithArgs(fragment: Fragment, frameId: Int = R.id.container, args: Bundle) {
    fragment.arguments = args
    supportFragmentManager.inTransaction {
        replace(frameId, fragment).addToBackStack(fragment.javaClass.name)
    }
}