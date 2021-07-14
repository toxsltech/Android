/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.snackBar

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView is a provided dependency, so in order to avoid burdening developers with a
 * potentially unnecessary dependency, we move the RecyclerView-related code here and only call it
 * if we confirm that they've provided it themselves.
 */
internal object RecyclerUtil {
    fun setScrollListener(snackbar: Snackbar, view: View) {
        val recyclerView = view as RecyclerView
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                snackbar.dismiss()
            }
        })
    }
}
