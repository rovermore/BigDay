package com.smallworldfs.moneytransferapp.presentation.autentix.model

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.webkit.JavascriptInterface
import javax.inject.Inject

class LocalStorageJavaScriptInterface @Inject constructor(private val localStorageDBHelper: LocalStorage) {

    private lateinit var database: SQLiteDatabase

    @JavascriptInterface
    fun getItem(key: String?): String? {
        var value: String? = null
        key?.let {
            database = localStorageDBHelper.readableDatabase
            val cursor: Cursor = database.query(
                LocalStorage.LOCALSTORAGE_TABLE_NAME,
                null,
                LocalStorage.LOCALSTORAGE_ID + " = ?", arrayOf(it), null, null, null,
            )
            if (cursor.moveToFirst()) {
                value = cursor.getString(1)
            }
            cursor.close()
            database.close()
        }
        return value
    }

    @JavascriptInterface
    fun setItem(key: String?, value: String?) {
        if (key != null && value != null) {
            val oldValue = getItem(key)
            database = localStorageDBHelper.writableDatabase
            val values = ContentValues()
            values.put(LocalStorage.LOCALSTORAGE_ID, key)
            values.put(LocalStorage.LOCALSTORAGE_VALUE, value)
            if (oldValue != null) {
                database.update(
                    LocalStorage.LOCALSTORAGE_TABLE_NAME,
                    values,
                    LocalStorage.LOCALSTORAGE_ID + "='" + key + "'",
                    null,
                )
            } else {
                database.insert(LocalStorage.LOCALSTORAGE_TABLE_NAME, null, values)
            }
            database.close()
        }
    }

    @JavascriptInterface
    fun removeItem(key: String?) {
        key?.let {
            database = localStorageDBHelper.writableDatabase
            database.delete(
                LocalStorage.LOCALSTORAGE_TABLE_NAME,
                LocalStorage.LOCALSTORAGE_ID + "='" + it + "'",
                null,
            )
            database.close()
        }
    }

    @JavascriptInterface
    fun clear() {
        database = localStorageDBHelper.writableDatabase
        database.delete(LocalStorage.LOCALSTORAGE_TABLE_NAME, null, null)
        database.close()
    }
}
