package com.example.myfirstapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DoctorAdapter(private val doctors: List<Doctor>) :
    RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(doctorView: View) : RecyclerView.ViewHolder(doctorView) {
        val imageView: ImageView = doctorView.findViewById(R.id.imageview)
        val nameView: TextView = doctorView.findViewById(R.id.name)
        val emailView: TextView = doctorView.findViewById(R.id.email)
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

        if (doctor.profilePictureUrl.isEmpty()) {
            holder.imageView.setImageResource(R.drawable.profile_pic) //defauklt photo
        } else {
            Glide.with(holder.itemView.context)
                .load(doctor.profilePictureUrl)
                .placeholder(R.drawable.profile_pic)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = doctors.size
}
