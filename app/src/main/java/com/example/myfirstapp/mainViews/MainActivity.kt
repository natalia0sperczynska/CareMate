package com.example.myfirstapp.mainViews

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirstapp.register.DataEntryActivity
import com.example.myfirstapp.login.LoginActivity
import com.example.myfirstapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val goToRegisterActivityButton = findViewById<Button>(R.id.registerButton)
        val goToLoginActivityButton: Button = findViewById(R.id.loginButton)


        goToLoginActivityButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        goToRegisterActivityButton.setOnClickListener {
            val intent = Intent(this, DataEntryActivity::class.java)
            startActivity(intent)
        }

    }
//    rules_version = '2';
//
//    service cloud.firestore {
//        match /databases/{database}/documents {
//            match /{document=**} {
//                allow read, write: if false;
//            }
//        }
//    }
}
