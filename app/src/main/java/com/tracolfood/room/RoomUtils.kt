/*
 * @copyright : ToXSL Technologies Pvt. Ltd. < www.toxsl.com >
 * @author     : Shiv Charan Panjeta < shiv@toxsl.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of ToXSL Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
package com.tracolfood.room

import com.tracolfood.model.CartProductData
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class RoomUtils {
    var callbackListener: roomCallbackListener? = null
    private var database: AppDatabase? = null
    fun setDatabase(database: AppDatabase?) {
        this.database = database
    }

    fun addData(data: CartProductData?, type: Int, callbackListener: roomCallbackListener?) {
        this.callbackListener = callbackListener
        Completable.fromAction { database!!.cartDao()!!.insertSingleRecord(data) }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {
                        this@RoomUtils.callbackListener!!.onDataReceived(type, true)
                    }

                    override fun onError(e: Throwable) {
                        this@RoomUtils.callbackListener!!.onDataReceived(type, false, e.message)
                    }
                })
    }

    fun getAll(type: Int, listener: roomCallbackListener?) {
        callbackListener = listener
        database!!.cartDao()!!.getAll()!!.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(object : SingleObserver<List<CartProductData?>?> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onSuccess(guestData: List<CartProductData?>) {
                        callbackListener!!.onDataReceived(type, true, guestData)
                    }

                    override fun onError(e: Throwable) {
                        callbackListener!!.onDataReceived(type, false, e.message)
                    }
                })
    }

    fun deleteProduct(data: CartProductData, type: Int, listener: roomCallbackListener?) {
        callbackListener = listener
        Completable.fromAction { database!!.cartDao()!!.delete(data.productId, data.sellerId) }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {
                        callbackListener!!.onDataReceived(type, true)
                    }

                    override fun onError(e: Throwable) {
                        callbackListener!!.onDataReceived(type, false, e.message)
                    }
                })
    }

    fun deleteAll(type: Int, listener: roomCallbackListener?) {
        callbackListener = listener
        Completable.fromAction { database!!.cartDao()!!.deleteAll() }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {
                        callbackListener!!.onDataReceived(type, true)
                    }

                    override fun onError(e: Throwable) {
                        callbackListener!!.onDataReceived(type, false, e.message)
                    }
                })
    }

    fun getCartCount(type: Int, listener: roomCallbackListener?) {
        callbackListener = listener
        database!!.cartDao()!!.getCount()!!.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(object : SingleObserver<Int?> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onSuccess(result: Int) {
                        callbackListener!!.onDataReceived(type, true, result)
                    }

                    override fun onError(e: Throwable) {
                        callbackListener!!.onDataReceived(type, false, e.message)
                    }
                })
    }

    interface roomCallbackListener {
        //        public void cartList(List<GuestData> cartList);
        fun cartData(data: CartProductData?)
        fun onSuccess()
        fun onDataReceived(vararg objects: Any?)
    }

    companion object {
        var instance: RoomUtils? = null
            get() {
                if (field == null) field = RoomUtils()
                return field
            }
            private set
    }
}