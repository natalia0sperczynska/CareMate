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
/**
 * Activity for handling user login.
 * Allows the user to log in using their email and password.
 * Provides a link to the registration activity if the user doesn't have an account.
 */
class LoginActivity : BaseActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
     * Function validates the login details entered by the user.
     *
     * @return True if the email and password are valid, false otherwise.
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
                true
            }
        }
    }
    /**
     * Attempts to log in the user using Firebase Authentication.
     * If successful, the user is redirected to the main user activity.
     */
    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            val email = inputEmail?.text.toString().trim { it <= ' ' }
            val password = inputPassword?.text.toString().trim { it <= ' ' }

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
     * Navigates to the main user activity after successful login.
     */
    open fun goToMainUser() {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.orEmpty()

        val intent = Intent(this, MainUser::class.java).apply {
            putExtra("uID", email)
        }
        startActivity(intent)
    }
}
