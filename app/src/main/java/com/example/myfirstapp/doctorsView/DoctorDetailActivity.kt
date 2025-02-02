package com.example.myfirstapp.doctorsView

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myfirstapp.R
/**
 * Activity displaying details of a selected doctor.
 */
class DoctorDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_detail)

        val goBack: Button = findViewById(R.id.buttonGoBack)
        goBack.setOnClickListener { finish() }

        val doctorName = intent.getStringExtra("DOCTOR_NAME") ?: "Unknown"
        val doctorSurname = intent.getStringExtra("DOCTOR_SURNAME") ?: ""
        val doctorEmail = intent.getStringExtra("DOCTOR_EMAIL") ?: "No Email"
        val doctorPhone = intent.getStringExtra("DOCTOR_PHONE") ?: "No Phone"
        val doctorImage = intent.getStringExtra("DOCTOR_IMAGE") ?: ""

        val nameView: TextView = findViewById(R.id.doctorName)
        val emailView: TextView = findViewById(R.id.doctorEmail)
        val phoneView: TextView = findViewById(R.id.doctorPhone)
        val imageView: ImageView = findViewById(R.id.doctorImage)

        nameView.text = "$doctorName $doctorSurname"
        emailView.text = doctorEmail
        phoneView.text = doctorPhone

        if (doctorImage.isNotEmpty()) {
            Glide.with(this)
                .load(doctorImage)
                .placeholder(R.drawable.profile_pic)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.profile_pic)
        }
    }

}
