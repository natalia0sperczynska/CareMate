package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseauthdemo.firebase.FirestoreClass
import com.example.firebaseauthdemo.firebase.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DataEntryActivity : AppCompatActivity() {

    private var registerButton: Button? = null
    private var inputName: EditText? = null
    private var inputSurname: EditText? = null
    private var inputEmail: EditText? = null
    private var inputDateOfBirth: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepeatPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_entry)

        registerButton = findViewById(R.id.registerButton)
        inputEmail = findViewById(R.id.inputEmail)
        inputName = findViewById(R.id.inputName)
        inputSurname = findViewById(R.id.inputSurname)
        inputDateOfBirth = findViewById(R.id.inputDateOfBirth)
        inputPassword = findViewById(R.id.inputPassword)
        inputRepeatPassword = findViewById(R.id.inputRepeatPassword)


        registerButton?.setOnClickListener {
            registerUser()
        }

        private fun validateRegisterDetails(): Boolean {
            return when {
                inputEmail?.text.toString().trim { it <= ' ' }.isEmpty() -> {
                    showErrorSnackBar("Please enter an email", true)
                    false
                }

                inputName?.text.toString().trim { it <= ' ' }.isEmpty() -> {
                    showErrorSnackBar("Please enter a name", true)
                    false
                }

                inputSurname?.text.toString().trim { it <= ' ' }.isEmpty() -> {
                    showErrorSnackBar("Please enter a surname", true)
                    false
                }

                inputDateOfBirth?.text.toString().trim { it <= ' ' }.isEmpty() -> {
                    showErrorSnackBar("Please enter a date of birth", true)
                    false

                }

                inputPassword?.text.toString().trim { it <= ' ' }.isEmpty() -> {
                    showErrorSnackBar("Please enter a password", true)
                    false
                }

                inputRepeatPassword?.text.toString().trim { it <= ' ' }.isEmpty() -> {
                    showErrorSnackBar("Please enter a repeat password", true)
                    false
                }

                else -> true
            }
        }

        fun goToLogin(view: View) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        private fun registerUser() {
            if (validateRegisterDetails()) {
                val email = inputEmail?.text.toString().trim { it <= ' ' }
                val password = inputPassword?.text.toString().trim { it <= ' ' }
                val name = inputName?.text.toString().trim { it <= ' ' }
                val surname = inputSurname?.text.toString().trim { it <= ' ' }

                FirebaseAuth.getInstance().createUserWithEmailAndPassowr(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar(
                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )

                            val user = User(
                                id = firebaseUser.uid,
                                name = name,
                                surname = surname,
                                registeredUser = true,
                                email = email
                            )
                            FirestoreClass().registerUserFS(this@DataEntryActivity, user)

                            FirestoreAuth.getInstance().signOut()
                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }

        fun userRegistrationSuccess() {
            Toast.makeText(
                this@DataEntryActivity,
                "You are registered successfully.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
