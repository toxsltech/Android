/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.tracolfood.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar

class SlideButton(context: Context?, attrs: AttributeSet?) : AppCompatSeekBar(context!!, attrs) {
    private var slideThumb: Drawable? = null
    private var listener: SlideButtonListener? = null
    override fun setThumb(thumb: Drawable) {
        super.setThumb(thumb)
        this.slideThumb = thumb
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (slideThumb!!.bounds.contains(event.x.toInt(), event.y.toInt())) {
                super.onTouchEvent(event)
            } else return false
        } else if (event.action == MotionEvent.ACTION_UP) {
            if (progress > 70) {
                handleSlide()
            }
            progress = 0
        } else super.onTouchEvent(event)
        return true
    }

    private fun handleSlide() {
        listener?.handleSlide()
    }

    fun setSlideButtonListener(listener: SlideButtonListener?) {
        this.listener = listener
    }
}

interface SlideButtonListener {
    fun handleSlide()
}