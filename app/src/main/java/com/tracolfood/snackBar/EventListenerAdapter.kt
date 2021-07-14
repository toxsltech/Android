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

/**
 * This adapter class provides empty implementations of the methods from "".
 * If you are only interested in a subset of the interface methods you can extend this class an override only the methods you need.
 */
abstract class EventListenerAdapter : EventListener {

    /**
     * {@inheritDoc}
     */
    override fun onShow(snackbar: Snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    override fun onShowByReplace(snackbar: Snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    override fun onShown(snackbar: Snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    override fun onDismiss(snackbar: Snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    override fun onDismissByReplace(snackbar: Snackbar) {

    }

    /**
     * {@inheritDoc}
     */
    override fun onDismissed(snackbar: Snackbar) {

    }
}
