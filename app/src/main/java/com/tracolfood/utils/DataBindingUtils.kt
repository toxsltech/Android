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

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.api.load
import com.tracolfood.R
import com.tracolfood.utils.extensions.loadCircleImageUrl
import com.tracolfood.utils.extensions.setGender


@BindingAdapter(value = ["circleImageUrl", "default"], requireAll = false)
fun setCircleImageResource(imageView: ImageView, url: String?, defaultImage: Int = R.mipmap.ic_img1) {
    imageView.loadCircleImageUrl(url, defaultImage)

}

@BindingAdapter(value = ["imageUrl", "default"], requireAll = false)
fun setImageResource(imageView: ImageView, url: String?, defaultImage: Int = R.mipmap.ic_img1) {
    if (!url.isNullOrEmpty()) {

        imageView.load(url) {
            error(defaultImage)
        }
    } else {
        imageView.load(defaultImage)
    }

}


@BindingAdapter(value = ["setDate", "sourceFormat", "outputFormat"], requireAll = false)
fun setDate(textView: TextView, date: String?, sourceFormat: String? = "yyyy-MM-dd HH:mm:ss", outputFormat: String? = "dd MMM,yyyy | hh:mm a") {
    textView.text = DateUtils.changeDateFormat(date, sourceFormat
            ?: "yyyy-MM-dd HH:mm:ss", outputFormat ?: "dd MMM,yyyy | hh:mm a")


}

@BindingAdapter("gender")
fun setGender(textView: TextView, gender: Int) {

    textView.setGender(gender)
}