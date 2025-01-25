package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirstapp.firebase.FireStore
import com.example.myfirstapp.mainViews.MainAdminActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ManageDoctorsActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val firestoreClass = FireStore()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_doctors)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nameInput: EditText = findViewById(R.id.name)
        val surnameInput: EditText = findViewById(R.id.surname)
        val emailInput: EditText = findViewById(R.id.email)
        val phoneInput: EditText = findViewById(R.id.phoneNumber)
        val profilePicInput: EditText = findViewById(R.id.ProfilePic)
        val add: Button = findViewById(R.id.addButton)
        val goBack: Button = findViewById(R.id.goBackButton)

        add.setOnClickListener {
            val name = nameInput.text.toString()
            val surname = surnameInput.text.toString()
            val email = emailInput.text.toString()
            val phone = phoneInput.text.toString()
            val profilePic = profilePicInput.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && profilePic.isNotEmpty()) {
                val doctor = hashMapOf(
                    "name" to name,
                    "surname" to surname,
                    "email" to email,
                    "phone" to phone,
                )
                db.collection("doctors")
                    .add(doctor)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Doctor added successfully!", Toast.LENGTH_SHORT)
                            .show()
//                        clearFields(
//                            nameInput,
//                            surnameInput,
//                            emailInput,
//                            phoneInput,
//                            profilePicInput
//                        )
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


        }
        goBack.setOnClickListener {
            val intent = Intent(this, MainAdminActivity::class.java)
            startActivity(intent)
        }
        fun clearFields(vararg fields: EditText) {
            for (field in fields) {
                field.text.clear()
            }
        }

    }
}