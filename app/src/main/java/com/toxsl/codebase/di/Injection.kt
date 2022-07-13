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

package com.toxsl.codebase.di

import androidx.lifecycle.ViewModelProvider
import com.toxsl.codebase.data.ApiClient
import com.toxsl.codebase.data.SongRemoteDataSource
import com.toxsl.codebase.model.SongDataSource
import com.toxsl.codebase.model.SongRepository
import com.toxsl.codebase.viewmodel.ViewModelFactory


object Injection {

    private var SongDataSource: SongDataSource? = null
    private var SongRepository: SongRepository? = null
    private var SongViewModelFactory: ViewModelFactory? = null

    private fun createSongDataSource(): SongDataSource {
        val dataSource = SongRemoteDataSource(ApiClient)
        SongDataSource = dataSource
        return dataSource
    }

    private fun createSongRepository(): SongRepository {
        val repository = SongRepository(provideDataSource())
        SongRepository = repository
        return repository
    }

    private fun createFactory(): ViewModelFactory {
        val factory = ViewModelFactory(providerRepository())
        SongViewModelFactory = factory
        return factory
    }

    private fun provideDataSource() = SongDataSource ?: createSongDataSource()
    private fun providerRepository() = SongRepository ?: createSongRepository()

    fun provideViewModelFactory() = SongViewModelFactory ?: createFactory()

    fun destroy() {
        SongDataSource = null
        SongRepository = null
        SongViewModelFactory = null
    }
}