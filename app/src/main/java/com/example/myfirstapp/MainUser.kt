package com.example.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myfirstapp.firebase.FireStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainUser : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance() // Firebase authentication instance
    private val firestoreClass = FireStore() // Firestore interaction helper class

   // @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main_user)

       val userId=auth.currentUser?.uid


       findViewById<TextView>(R.id.message).text = "Welcome!"
       if (userId != null) {
           loadUserData(userId)
       }
       val logout: Button = findViewById(R.id.logoutButton)
       logout.setOnClickListener {
           FirebaseAuth.getInstance().signOut() // Sign out the user
           val intent = Intent(this, MainActivity::class.java)
           startActivity(intent)
           finish() // Prevent returning to this activity when back is pressed
       }
       val updateData: Button = findViewById(R.id.updatedataButton)

      updateData.setOnClickListener {
           val intent = Intent(this, UpdateDataActivity::class.java)
           startActivity(intent)
       }

   }


    private fun loadUserData(userId: String) {
        lifecycleScope.launch {
            try {
                // Fetch user data from Firestore
                val data = firestoreClass.loadUserData(userId)
                if (data != null) {
                    Toast.makeText(this@MainUser, "\"Welcome back!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainUser, "No user data found.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainUser,
                    "Failed to load user data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}