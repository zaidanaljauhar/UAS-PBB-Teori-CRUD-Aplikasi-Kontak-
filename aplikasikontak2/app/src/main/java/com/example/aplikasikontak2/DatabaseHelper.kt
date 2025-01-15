package com.example.aplikasikontak2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "kontak.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "kontak"
        const val COL_ID = "ID"
        const val COL_NAME = "NAMA"
        const val COL_PHONE = "TELEPON"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT, $COL_PHONE TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(name: String, phone: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_PHONE, phone)
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L // return true if data inserted successfully
    }

    fun getAllData(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun updateData(id: String, name: String, phone: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_PHONE, phone)
        db.update(TABLE_NAME, contentValues, "$COL_ID = ?", arrayOf(id))
        return true
    }

    fun deleteData(id: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id))
    }
}