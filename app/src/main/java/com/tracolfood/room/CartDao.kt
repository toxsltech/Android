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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.tracolfood.model.CartProductData
import io.reactivex.Single

@Dao
interface CartDao {

    @Query("SELECT * FROM product_data")
    fun getAll(): Single<List<CartProductData?>?>?

    @Query("SELECT COUNT(productId) FROM product_data")
    fun getDataCount(): Int

    @Insert(onConflict = REPLACE)
    fun insertAll(users: List<CartProductData?>?)

    @Insert(onConflict = REPLACE)
    fun insertSingleRecord(users: CartProductData?)

    @Update
    fun update(data: CartProductData?)

    @Query("DELETE  FROM product_data where productId =  :productId AND sellerId = :sellerId AND selectColorId = :selectColorId AND selectSizeId = :selectSizeId")
    fun deleteBoth(productId: Int?, sellerId: Int?, selectColorId: Int?, selectSizeId: Int?)

    @Query("DELETE  FROM product_data where productId =  :productId AND sellerId = :sellerId AND  selectSizeId = :selectSizeId")
    fun deleteSize(productId: Int?, sellerId: Int?, selectSizeId: Int?)

    @Query("DELETE  FROM product_data where productId =  :productId AND sellerId = :sellerId AND selectColorId = :selectColorId")
    fun deleteColor(productId: Int?, sellerId: Int?, selectColorId: Int?)

    @Query("DELETE  FROM product_data where productId =  :productId AND sellerId = :sellerId ")
    fun delete(productId: Int?, sellerId: Int?)

    @Query("UPDATE product_data  SET addedQuantity =:addedQuantity where productId = :productId AND  sellerId = :sellerId")
    fun updateQuantity(productId: Int?, sellerId: Int?, addedQuantity: Int?)

    @Query("UPDATE product_data  SET addedQuantity =:addedQuantity where productId = :productId AND  sellerId = :sellerId AND selectSizeId = :selectSizeId")
    fun updateSize(productId: Int?, sellerId: Int?, addedQuantity: Int?, selectSizeId: Int?)

    @Query("UPDATE product_data  SET addedQuantity =:addedQuantity where productId = :productId AND  sellerId = :sellerId AND selectColorId = :selectColorId")
    fun updateColor(productId: Int?, sellerId: Int?, addedQuantity: Int?, selectColorId: Int?)

    @Query("UPDATE product_data  SET addedQuantity =:addedQuantity where productId = :productId AND  sellerId = :sellerId")
    fun updateQ(productId: Int?, sellerId: Int?, addedQuantity: Int?)


    @Query("DELETE FROM product_data")
    fun deleteAll()

    @Query("SELECT COUNT(productId) FROM product_data")
     fun getCount(): Single<Int?>?

}