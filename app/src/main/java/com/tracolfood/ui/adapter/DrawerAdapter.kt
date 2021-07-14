/*
 * @copyright : ToXSL Technologies Pvt. Ltd. < www.toxsl.com >
 * @author     : Shiv Charan Panjeta < shiv@toxsl.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of ToXSL Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.ui.adapter


import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tracolfood.R
import com.tracolfood.model.DrawerData
import com.tracolfood.ui.base.BaseActivity
import java.util.*


class DrawerAdapter(internal var activity: BaseActivity, resource: Int, val objects: ArrayList<DrawerData>) : ArrayAdapter<DrawerData>(activity, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = newView(parent)
        }
        bindView(position, convertView)
        return convertView
    }

    private fun newView(parent: ViewGroup): View {
        return activity.layoutInflater.inflate(R.layout.adapter_drawer, parent, false)
    }

    private fun bindView(position: Int, convertView: View) {
        val nameTV = convertView.findViewById<View>(R.id.nameTV) as TextView
        val data = getItem(position)
        if (data != null) {
            nameTV.text = data.name
        }
    }
}
