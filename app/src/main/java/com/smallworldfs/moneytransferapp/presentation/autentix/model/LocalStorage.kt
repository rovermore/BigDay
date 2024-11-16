package com.smallworldfs.moneytransferapp.presentation.autentix.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import javax.inject.Inject

class LocalStorage @Inject constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val LOCALSTORAGE_TABLE_NAME = "autentix_storage_table"
        const val LOCALSTORAGE_ID = "_id"
        const val LOCALSTORAGE_VALUE = "value"
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "autentix_storage.db"
        private const val DICTIONARY_TABLE_CREATE = (
            "CREATE TABLE " +
                LOCALSTORAGE_TABLE_NAME +
                " (" + LOCALSTORAGE_ID + " TEXT PRIMARY KEY, " +
                LOCALSTORAGE_VALUE + " TEXT NOT NULL);"
            )
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DICTIONARY_TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $LOCALSTORAGE_TABLE_NAME")
        onCreate(db)
    }
}
