package com.example.myfirstapp.mainViews

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myfirstapp.MapsActivity
import com.example.myfirstapp.bmi.BMIActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.SetNotificationActivity
import com.example.myfirstapp.doctorsView.DoctorsRecyclerView
import com.example.myfirstapp.firebase.FireStore
import com.example.myfirstapp.updateData.UpdateDataActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainUser : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance() // Firebase authentication instance
    private val firestoreClass = FireStore() // Firestore interaction helper class


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)

       val userId=auth.currentUser?.uid

        val welcomeText: TextView = findViewById(R.id.nameAndSurname)

       findViewById<TextView>(R.id.message).text = "Welcome!"

       if (userId != null) {
           loadUserData(userId,welcomeText)
       }
       //val logout1: Button = findViewById(R.id.logoutButton)
        val logout: LinearLayout = findViewById(R.id.logout)
       logout.setOnClickListener {
           FirebaseAuth.getInstance().signOut() // Sign out the user
           val intent = Intent(this, MainActivity::class.java)
           startActivity(intent)
           finish() // Prevent returning to this activity when back is pressed
       }
      // val updateData: Button = findViewById(R.id.updatedataButton)
        val updateData: LinearLayout = findViewById(R.id.updateData)


      updateData.setOnClickListener {
           val intent = Intent(this, UpdateDataActivity::class.java)
           startActivity(intent)
       }

        val maps: LinearLayout = findViewById(R.id.maps)


        maps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        val doctors: LinearLayout = findViewById(R.id.doctors)


        doctors.setOnClickListener {
            val intent = Intent(this, DoctorsRecyclerView::class.java)
            startActivity(intent)
        }


        val calculateBMI: LinearLayout = findViewById(R.id.BMI)


        calculateBMI.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }
        val notification: LinearLayout = findViewById(R.id.Notifications)


        notification.setOnClickListener {
            val intent = Intent(this, SetNotificationActivity::class.java)
            startActivity(intent)
        }

//        val notificationsButton: LinearLayout = findViewById(R.id.imageView12).parent
//        notificationsButton.setOnClickListener {
//            val intent = Intent(this, NotificationsActivity::class.java)
//            startActivity(intent)
//        }
//
//        val calendarButton: LinearLayout = findViewById(R.id.imageView13).parent
//        calendarButton.setOnClickListener {
//            val intent = Intent(this, CalendarActivity::class.java)
//            startActivity(intent)
//        }
//
//        val appointmentButton: LinearLayout = findViewById(R.id.imageView14).parent
//        appointmentButton.setOnClickListener {
//            val intent = Intent(this, SetAppointmentActivity::class.java)
//            startActivity(intent)
//        }

   }


    private fun loadUserData(userId: String,welcomeText: TextView) {
        lifecycleScope.launch {
            try {
                // Fetch user data from Firestore
                val data = firestoreClass.loadUserData(userId)
                if (data != null) {
                    val userName=data.getValue("name")
                    welcomeText.text = "Good to see you again $userName!"
                    //Toast.makeText(this@MainUser, "Welcome back!", Toast.LENGTH_SHORT).show()
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