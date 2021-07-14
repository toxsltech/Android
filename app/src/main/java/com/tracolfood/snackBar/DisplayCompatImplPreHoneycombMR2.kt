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

import android.graphics.Point
import android.view.Display

internal class DisplayCompatImplPreHoneycombMR2 : DisplayCompat.Impl() {
    override fun getSize(display: Display, outSize: Point) {
        outSize.x = display.width
        outSize.y = display.height
    }

    override fun getRealSize(display: Display, outSize: Point) {
        outSize.x = display.width
        outSize.y = display.height
    }
}

