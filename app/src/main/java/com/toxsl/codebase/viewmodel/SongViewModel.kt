/*
 *
 *  * @copyright : ToXSL Technologies Pvt. Ltd. < www.toxsl.com >
 *  * @author     : Shiv Charan Panjeta < shiv@toxsl.com >
 *  *
 *  * All Rights Reserved.
 *  * Proprietary and confidential :  All information contained herein is, and remains
 *  * the property of ToXSL Technologies Pvt. Ltd. and its partners.
 *  * Unauthorized copying of this file, via any medium is strictly prohibited.
 *
 */

package com.toxsl.codebase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.toxsl.codebase.data.OperationCallback
import com.toxsl.codebase.model.Song
import com.toxsl.codebase.model.SongRepository


class SongViewModel(private val repository: SongRepository) : ViewModel() {

    private val _songs = MutableLiveData<List<Song>>().apply { value = emptyList() }
    val songs: LiveData<List<Song>> = _songs

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList


    fun loadSongs() {
        _isViewLoading.value = true
        repository.fetchSongs(object : OperationCallback<Song> {
            override fun onError(error: String?) {
                _isViewLoading.value = false
                _onMessageError.value = error!!
            }

            override fun onSuccess(data: List<Song>?) {
                _isViewLoading.value = false
                if (data.isNullOrEmpty()) {
                    _isEmptyList.value = true

                } else {
                    _songs.value = data
                }
            }
        })
    }

}