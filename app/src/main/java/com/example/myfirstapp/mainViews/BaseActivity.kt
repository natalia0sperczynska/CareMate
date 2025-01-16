package com.example.myfirstapp.mainViews

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myfirstapp.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(
            ContextCompat.getColor(
                this@BaseActivity,
                if (errorMessage) R.color.colorSnackBarError else R.color.colorSnackBarSuccess
            )
        )
        snackbar.show()
    }
}