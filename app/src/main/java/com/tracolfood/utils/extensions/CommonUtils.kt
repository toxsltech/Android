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

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.tracolfood.BuildConfig
import com.tracolfood.R
import com.tracolfood.utils.Const
import com.tracolfood.utils.Const.Drawable.END
import com.tracolfood.utils.Const.Drawable.START
import java.lang.Exception


fun View.visibleView(visible: Boolean) {
    this.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}


//change status bar color
fun AppCompatActivity.setStatusBarColor(color: Int, showLight: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = this.window

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color


    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = this.window.decorView
        view.let {
            if (showLight) {
                it.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            } else {
                it.systemUiVisibility = it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() //set status text  light

            }
        }
    }
}


fun ImageView.loadCircleImageUrl(url: String? = "", defaultImage: Int) {
    if (!url.isNullOrEmpty()) {

        load(url) {
            error(defaultImage)
            diskCachePolicy(CachePolicy.DISABLED)
            memoryCachePolicy(CachePolicy.DISABLED)
            transformations(CircleCropTransformation())
        }
    } else {
        load(defaultImage)
    }
}

fun ImageView.loadImageUrl(url: String? = "", defaultImage: Int) {
    if (!url.isNullOrEmpty()) {
        val imageUrl = Const.IMAGE_SERVER_URL + url

        load(imageUrl) {
            error(defaultImage)
            placeholder(defaultImage)
            diskCachePolicy(CachePolicy.DISABLED)
            memoryCachePolicy(CachePolicy.DISABLED)

        }
    } else {
        load(defaultImage)
    }
}


fun TextView.showSelected(isSelected: Boolean) {
    if (isSelected) {
        setTextColor(ContextCompat.getColor(this.context, R.color.White))
        /*setBackgroundResource(R.drawable.button_selector)*/
    } else {
        setTextColor(ContextCompat.getColor(this.context, R.color.text_color))
        setBackgroundResource(R.drawable.category_bg)

    }
}


fun TextView.setColor(lightGrey: Int) {
    this.setTextColor(ContextCompat.getColor(this.context!!, lightGrey))
}

fun TextView.setSpanString(spanText: String, start: Int, end: Int = spanText.length, color: Int = R.color.Black, onSpanClick: () -> Unit) {
    val ss = SpannableString(spanText)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            onSpanClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@setSpanString.context, color)
        }
    }
    ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    text = ss
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ContextCompat.getColor(this@setSpanString.context, R.color.transparent)

}

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}


@SuppressLint("ClickableViewAccessibility")
fun EditText.onDrawableClick(drawableType: Int, onClick: () -> Unit) {
    this.setOnTouchListener(View.OnTouchListener { _, event ->

        if (event.action == MotionEvent.ACTION_UP) {
            when (drawableType) {
                END -> {
                    if (event.rawX >= this.right - this.compoundDrawables.get(END).bounds.width()) { // your action here
                        onClick()
                        return@OnTouchListener true
                    }
                }
                START -> {
                    if (event.rawX <= (this.compoundDrawables[START].bounds.width())) {
                        onClick()

                        return@OnTouchListener true

                    }

                }
            }

        }
        false
    })
}


fun TextView.setHtmlData(source: String?) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(source)
    }
}


fun handleException(e: Exception) {
    if (BuildConfig.DEBUG) {
        e.printStackTrace()
    }
}


