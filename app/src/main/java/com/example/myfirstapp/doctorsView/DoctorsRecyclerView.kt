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
/**
 * Activity displaying a list of doctors in a RecyclerView.
 */
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
                    Log.d("DoctorsRecyclerView", "Fetched doctor: ${doctor.name}, Image URL: ${doctor.profilePicture}")
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
    /**
     * Function handles the click event on a doctor item, opening the DoctorDetailActivity.
     * @param position The position of the clicked item in the list.
     */
    override fun onClickItem(position: Int) {
        val doctor = doctors[position]

        val intent = Intent(this, DoctorDetailActivity::class.java).apply {
            putExtra("DOCTOR_NAME", doctor.name)
            putExtra("DOCTOR_SURNAME", doctor.surname)
            putExtra("DOCTOR_EMAIL", doctor.email)
            putExtra("DOCTOR_PHONE", doctor.phoneNumber)
            putExtra("DOCTOR_IMAGE", doctor.profilePicture)
        }

        startActivity(intent)
    }
}
