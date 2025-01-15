package com.example.myfirstapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoctorsRecyclerView : AppCompatActivity() {

    private val db = Firebase.firestore
    private val doctors: MutableList<Doctor> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors)

        val recyclerView: RecyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("doctors").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val doctor = document.toObject(Doctor::class.java)
                    doctors.add(doctor)
                }
                Log.d("DoctorsRecyclerView", "Doctors: $doctors")
                recyclerView.adapter = DoctorAdapter(doctors)
            }
            .addOnFailureListener { e ->
                Log.e("DoctorsRecyclerView", "Error fetching documents", e)
            }
    }
}