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

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tracolfood.model.CartProductData

@Database(entities = [CartProductData::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao?

    companion object {
        private const val DB_NAME = "tracol_db"
        private var dbInstance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if (dbInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (dbInstance == null) {
                        dbInstance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d("AppDatabase", "populating with data...")
                            }

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                                Log.d("Open", "Database Opening")
                            }
                        }).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return dbInstance
        }
    }
}