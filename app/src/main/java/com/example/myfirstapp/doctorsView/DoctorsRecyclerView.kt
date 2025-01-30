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

class DoctorsRecyclerView : AppCompatActivity(), RecyclerViewInterface {

    private val db = Firebase.firestore
    private val doctors: MutableList<Doctor> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors)

        val goBackToMainUser: Button = findViewById(R.id.goBackButton12)
        val recyclerView: RecyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("doctors").get()
            .addOnSuccessListener { result ->
                doctors.clear()
                for (document in result) {
                    val doctor = document.toObject(Doctor::class.java)
                    doctors.add(doctor)
                }
                Log.d("DoctorsRecyclerView", "Doctors: $doctors")
                recyclerView.adapter = DoctorAdapter(doctors, this)
            }
            .addOnFailureListener { e ->
                Log.e("DoctorsRecyclerView", "Error fetching documents", e)
            }

        goBackToMainUser.setOnClickListener {
            val intent = Intent(this, MainUser::class.java)
            startActivity(intent)
        }
    }

    override fun onClickItem(position: Int) {
        val doctor = doctors[position]

        val intent = Intent(this, DoctorDetailActivity::class.java).apply {
            putExtra("DOCTOR_NAME", doctor.name)
            putExtra("DOCTOR_SURNAME", doctor.surname)
            putExtra("DOCTOR_EMAIL", doctor.email)
            putExtra("DOCTOR_PHONE", doctor.phoneNumber)
            putExtra("DOCTOR_IMAGE", doctor.profilePictureUrl)
        }

        startActivity(intent)
    }
}
