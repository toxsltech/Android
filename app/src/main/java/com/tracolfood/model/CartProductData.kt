/*
 * @copyright : ToXSL Technologies Pvt. Ltd. < www.toxsl.com >
 * @author     : Shiv Charan Panjeta < shiv@toxsl.com >
 *
 * All Rights Reserved.
 * Proprietary and confidential :  All information contained herein is, and remains
 * the property of ToXSL Technologies Pvt. Ltd. and its partners.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */

package com.tracolfood.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "product_data")
class CartProductData :Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "productId")
    var productId: Int? = null

    @ColumnInfo(name = "sellerId")
    var sellerId: Int? = null

    @ColumnInfo(name = "quantity")
    var quantity: Int? = null

    @ColumnInfo(name = "addedQuantity")
    var addedQuantity: Int? = null

    @ColumnInfo(name = "productName")
    var productName: String? = null

    @ColumnInfo(name = "selectColorId")
    var selectColorId: Int? = null

    @ColumnInfo(name = "colorSelect")
    var colorSelect: String? = null

    @ColumnInfo(name = "sizeSelect")
    var sizeSelect: String? = null

    @ColumnInfo(name = "productCategory")
    var productCategory: String? = null

    @ColumnInfo(name = "selectSizeId")
    var selectSizeId: Int? = null

    @ColumnInfo(name = "conditionType")
    var conditionType: String? = null

    @ColumnInfo(name = "deliveryType")
    var deliveryType: Int? = null

    @ColumnInfo(name = "itemType")
    var itemType: Int? = null

    @ColumnInfo(name = "productAmt")
    var productAmt: String? = null

    @ColumnInfo(name = "productAcutalAmt")
    var productAcutalAmt: String? = null

    @ColumnInfo(name = "productDiscount")
    var productDiscount: String? = null

    @ColumnInfo(name = "productBrand")
    var productBrand: String? = null

    @ColumnInfo(name = "productRating")
    var productRating: String? = null

    @ColumnInfo(name = "productReview")
    var productReview: String? = null

    @ColumnInfo(name = "productDes")
    var productDes: String? = null

    @ColumnInfo(name = "imageFile")
    var imageFile: String? = null

    @ColumnInfo(name = "createdOn")
    var createdOn: String? = null

    @ColumnInfo(name = "price")
    var price: String? = null


}