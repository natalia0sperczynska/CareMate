package com.example.myfirstapp.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myfirstapp.R
import com.example.myfirstapp.mainViews.BaseActivity
import com.example.myfirstapp.mainViews.MainUser
import com.example.myfirstapp.register.DataEntryActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize input fields and login button
        inputEmail = findViewById(R.id.editTextTextEmailAddressLogin)
        inputPassword = findViewById(R.id.editTextTextPasswordLogin)
        loginButton = findViewById(R.id.loginButton)

        loginButton?.setOnClickListener {
            logInRegisteredUser()
        }

        val goToRegisterActivityButton = findViewById<Button>(R.id.dontHaveAccountButton)
        goToRegisterActivityButton.setOnClickListener {
            val intent = Intent(this, DataEntryActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Validates the login details entered by the user.
     * @return True if the details are valid, otherwise False.
     */
    private fun validateLoginDetails(): Boolean {
        val email = inputEmail?.text.toString().trim { it <= ' ' }
        val password = inputPassword?.text.toString().trim { it <= ' ' }

        return when {
            email.isEmpty() -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            password.isEmpty() -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                // Optionally show a success message
                true
            }
        }
    }

    /**
     * Logs in a registered user using Firebase Authentication.
     */
    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            val email = inputEmail?.text.toString().trim { it <= ' ' }
            val password = inputPassword?.text.toString().trim { it <= ' ' }

            // Sign in with FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showErrorSnackBar("You are logged in successfully.", false)
                        goToMainUser()
                    } else {
                        showErrorSnackBar(task.exception?.message.toString(), true)
                    }
                }
        }
    }

    /**
     * Navigates to the main user activity after successful login and passes the user's UID to the main activity.
     */
    open fun goToMainUser() {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.orEmpty() // User email

        val intent = Intent(this, MainUser::class.java).apply {
            putExtra("uID", email)
        }
        startActivity(intent)
    }
}
