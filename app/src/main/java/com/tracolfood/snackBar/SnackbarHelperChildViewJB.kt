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

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
internal class SnackbarHelperChildViewJB(context: Context) : View(context) {
    init {
        isSaveEnabled = false
        setWillNotDraw(true)
        visibility = View.GONE
    }

    override fun onWindowSystemUiVisibilityChanged(visible: Int) {
        super.onWindowSystemUiVisibilityChanged(visible)

        val parent = parent
        if (parent is Snackbar) {
            parent.dispatchOnWindowSystemUiVisibilityChangedCompat(visible)
        }
    }
}
