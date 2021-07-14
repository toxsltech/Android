/*
 * copyright : Ozvid Technologies Pvt. Ltd. < www.ozvid.com >
 * @author     : Shiv Charan Panjeta < shiv@ozvid.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of Ozvid Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.tracolfood.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tracolfood.R
import java.util.*

internal class SampleRecyclerViewAdapter(context: Context) : RecyclerView.Adapter<SampleRecyclerViewAdapter.ViewHolder?>() {
    private val context: Context
    private var mItems: MutableList<String>? = null
    fun reloadItems() {
        try {
            mItems = ArrayList()
            mItems!!.add("Row 1")
            mItems!!.add("Row 2")
            mItems!!.add("Row 3")
            mItems!!.add("Row 4")
            mItems!!.add("Row 5")
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vh: ViewHolder? = null
        try {
            val view = LayoutInflater.from(context).inflate(R.layout.adapter_address, parent, false)
            vh = ViewHolder(view)
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
        return vh!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder!!.text1.text = mItems!![position]
            holder.text2.text = "Swipe left or right to see what happens"
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
    }

    override fun getItemCount(): Int {
        return mItems!!.size
    }

    fun addItem(item: String, position: Int) {
        try {
            mItems!!.add(position, item)
            notifyItemInserted(position)
        } catch (e: Exception) {
            Log.e("MainActivity", e.message!!)
        }
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text1: TextView
        var text2: TextView

        init {
            text1 = itemView.findViewById<View>(R.id.addressValueTV) as TextView
            text2 = itemView.findViewById<View>(R.id.addressValueTV) as TextView
        }
    }

    fun removeItem(position: Int): String? {
        var item: String? = null
        try {
            item = mItems!![position]
            mItems!!.removeAt(position)
            notifyItemRemoved(position)
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
        return item
    }

    companion object {
        private const val TAG = "ADAPTER"
    }

    init {
        reloadItems()
        this.context = context
    }


}