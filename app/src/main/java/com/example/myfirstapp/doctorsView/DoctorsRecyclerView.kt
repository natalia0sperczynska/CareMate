package com.example.myfirstapp.doctorsView

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.mainViews.MainUser
import com.example.myfirstapp.R
import com.example.myfirstapp.firebase.Doctor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoctorsRecyclerView : AppCompatActivity() {

    private val db = Firebase.firestore
    private val doctors: MutableList<Doctor> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors)

        val goBackToMainUser: Button = findViewById(R.id.goBackButton)

        val recyclerView: RecyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("doctors").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val doctor = document.toObject(Doctor::class.java)
                    doctors.add(doctor)
                }
                Log.d("DoctorsRecyclerView", "Doctors: $doctors")
                recyclerView.adapter = DoctorAdapter(doctors) { selectedDoctor ->
                    val intent = Intent(this, DoctorDetailActivity::class.java).apply {
                        putExtra("DOCTOR_NAME", selectedDoctor.name)
                        putExtra("DOCTOR_SURNAME", selectedDoctor.surname)
                        putExtra("DOCTOR_EMAIL", selectedDoctor.email)
                        putExtra("DOCTOR_IMAGE", selectedDoctor.profilePictureUrl)
                    }
                    startActivity(intent)
                }
            }
            .addOnFailureListener { e ->
                Log.e("DoctorsRecyclerView", "Error fetching documents", e)
            }

        goBackToMainUser.setOnClickListener {
            val intent = Intent(this, MainUser::class.java)
            startActivity(intent)
        }
    }
}