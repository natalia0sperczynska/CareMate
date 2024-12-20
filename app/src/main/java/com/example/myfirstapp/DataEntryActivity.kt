package com.example.myfirstapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.myfirstapp.firebase.FireStore
import com.example.myfirstapp.firebase.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.util.Calendar

class DataEntryActivity : BaseActivity() {

    private var registerButton: Button? = null
    private var inputName: EditText? = null
    private var inputSurname: EditText? = null
    private var inputEmail: EditText? = null
    private val inputDateOfBirth: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepeatPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_entry)

        registerButton = findViewById(R.id.registerButton)
        inputEmail = findViewById(R.id.email)
        inputName = findViewById(R.id.name)
        inputSurname = findViewById(R.id.surname)
        inputPassword = findViewById(R.id.password)
        inputRepeatPassword = findViewById(R.id.repeatPassword)
        val birthDateButton: Button = findViewById(R.id.birthDateButton)
        val birthDateTextView: TextView = findViewById(R.id.birthDateTextView)

        /**
         * Sets up a listener for the birth date button to display a DatePicker dialog.
         */
        birthDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val dateString = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                birthDateTextView.text = dateString
            }, year, month, day)
            datePickerDialog.show()
        }
        registerButton?.setOnClickListener {
            registerUser()
        }

}
        private fun validateRegisterDetails(): Boolean {
            val passwordPattern = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")

            return when {
                inputEmail.toString().isEmpty() -> {
                    showErrorSnackBar("Please enter an email", true)
                    false
                }

                !android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail?.text.toString()).matches() -> {
                    showErrorSnackBar("Please enter a valid email", true)
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

                !passwordPattern.matches(inputPassword?.text.toString().trim()) -> {
                    showErrorSnackBar("Password must be at least 8 characters, include an uppercase letter, a number, and a special character", true)
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
                val repeatPassword = inputRepeatPassword?.text.toString().trim { it <= ' ' }
                val dateOfBirth = inputDateOfBirth?.text.toString().trim{ it <= ' ' }
                val name = inputName?.text.toString().trim { it <= ' ' }
                val surname = inputSurname?.text.toString().trim { it <= ' ' }

                if (password != repeatPassword) {
                    showErrorSnackBar("Passwords do not match", true)
                    return
                }
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
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
                                email=email,
                                dateOfBirth = dateOfBirth,
                                phoneNumber = "",
                                profilePictureUrl = "" ,
                                address = mapOf(), // Default empty map
                                allergies = listOf(),
                                diseases= listOf(),
                                medications = listOf()
                            )
                            lifecycleScope.launch {
                                try {
                                    val firestoreClass = FireStore()
                                    firestoreClass.registerOrUpdateUser(user)
                                    Toast.makeText(this@DataEntryActivity, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                                    FirebaseAuth.getInstance().signOut()
                                    finish()
                                } catch (e: Exception) {
                                    Toast.makeText(this@DataEntryActivity, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }

                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
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
