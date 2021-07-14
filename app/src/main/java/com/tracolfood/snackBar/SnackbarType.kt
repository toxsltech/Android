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

enum class SnackbarType(val minHeight: Int, val maxHeight: Int, val maxLines: Int) {

    /**
     * Snackbar with a single line
     */
    SINGLE_LINE(48, 48, 1),

    /**
     * Snackbar with two lines
     */
    MULTI_LINE(48, 80, 2)
}
