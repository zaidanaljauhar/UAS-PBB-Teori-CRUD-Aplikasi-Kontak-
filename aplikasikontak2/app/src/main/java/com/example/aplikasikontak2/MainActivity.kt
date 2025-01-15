package com.example.aplikasikontak2

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var myDb: DatabaseHelper
    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var listViewContacts: ListView

    private var selectedContactId: String? = null // Menyimpan ID kontak yang dipilih

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myDb = DatabaseHelper(this)

        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        listViewContacts = findViewById(R.id.listViewContacts)

        val buttonAddContact = findViewById<Button>(R.id.buttonAddContact)
        val buttonEditContact = findViewById<Button>(R.id.buttonEditContact)
        val buttonDeleteContact = findViewById<Button>(R.id.buttonDeleteContact)

        buttonAddContact.setOnClickListener {
            val isInserted = myDb.insertData(editTextName.text.toString(), editTextPhone.text.toString())
            if (isInserted) {
                loadContacts()
            }
            clearInputFields()
        }

        buttonEditContact.setOnClickListener {
            selectedContactId?.let {
                val isUpdated = myDb.updateData(it, editTextName.text.toString(), editTextPhone.text.toString())
                if (isUpdated) {
                    loadContacts()
                    clearInputFields()
                    selectedContactId = null // Reset ID setelah update
                }
            } ?: run {
                Toast.makeText(this, "Pilih kontak untuk diedit", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDeleteContact.setOnClickListener {
            selectedContactId?.let {
                myDb.deleteData(it)
                loadContacts()
                clearInputFields()
                selectedContactId = null // Reset ID setelah delete
            } ?: run {
                Toast.makeText(this, "Pilih kontak untuk dihapus", Toast.LENGTH_SHORT).show()
            }
        }

        listViewContacts.setOnItemClickListener { _, _, position, _ ->
            val cursor: Cursor = myDb.getAllData()
            cursor.moveToPosition(position)
            selectedContactId = cursor.getString(0) // Ambil ID kontak yang dipilih
            editTextName.setText(cursor.getString(1)) // Set nama ke EditText
            editTextPhone.setText(cursor.getString(2)) // Set telepon ke EditText
            cursor.close()
        }

        loadContacts()
    }

    private fun loadContacts() {
        val cursor: Cursor = myDb.getAllData()

        val contactsList = ArrayList<String>()

        while (cursor.moveToNext()) {
            contactsList.add("${cursor.getString(1)} - ${cursor.getString(2)}")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList)

        listViewContacts.adapter = adapter

        cursor.close()
    }

    private fun clearInputFields() {
        editTextName.text.clear()
        editTextPhone.text.clear()
    }
}


