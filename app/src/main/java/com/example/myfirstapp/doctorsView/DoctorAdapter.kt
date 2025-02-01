package com.example.myfirstapp.doctorsView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfirstapp.R
import com.example.myfirstapp.firebase.Doctor

class DoctorAdapter(
    private val doctors: List<Doctor>,
    private val recyclerViewInterface: RecyclerViewInterface?
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(doctorView: View) : RecyclerView.ViewHolder(doctorView) {
        val imageView: ImageView = doctorView.findViewById(R.id.imageview)
        val nameView: TextView = doctorView.findViewById(R.id.name)
        val emailView: TextView = doctorView.findViewById(R.id.email)

        init {
            doctorView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    recyclerViewInterface?.onClickItem(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_view, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.nameView.text = "${doctor.name} ${doctor.surname}"
        holder.emailView.text = doctor.email

        Log.d("DoctorAdapter", "Loading image for ${doctor.name}: ${doctor.profilePicture}")

        if (doctor.profilePicture.isEmpty()) {
            Log.e("DoctorAdapter", "Doctor ${doctor.name} has an empty profilePictureUrl!")
            holder.imageView.setImageResource(R.drawable.profile_pic) // Default photo
        } else {
            Log.d("DoctorAdapter", "Loading image for ${doctor.name}: ${doctor.profilePicture}")
            Glide.with(holder.itemView.context)
                .load(doctor.profilePicture)
                .placeholder(R.drawable.profile_pic)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = doctors.size
}
