package com.serafine.uklkasir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.User

class RegisterActivity : AppCompatActivity() {
    lateinit var editName: EditText
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var pilihRole: Spinner
    lateinit var buttonSave: Button

    lateinit var db: CafeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
        setDataSpinner()
        buttonSave.setOnClickListener{
            if(editName.text.toString().isNotEmpty() && editEmail.text.toString().isNotEmpty() && editPassword.text.toString().isNotEmpty()){
                db.cafeDao().insertUser(User(null, editName.text.toString(), editEmail.text.toString(), editPassword.text.toString(), pilihRole.selectedItem.toString()))
                Toast.makeText(applicationContext, "Register successfull", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    fun init(){
        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        pilihRole = findViewById(R.id.spinnerRole)
        buttonSave = findViewById(R.id.buttonSave)

        db = CafeDatabase.getInstance(applicationContext)
    }

    private fun setDataSpinner(){
        val adapter = ArrayAdapter.createFromResource(applicationContext, R.array.roles, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pilihRole.adapter = adapter
    }
}