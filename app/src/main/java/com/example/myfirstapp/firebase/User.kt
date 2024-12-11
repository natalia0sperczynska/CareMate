package com.example.myfirstapp.firebase

//data class
data class User (
    val id: String = "",
    val name: String? = null,
    val surname: String?=null,
    val email: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val profilePictureUrl: String = ""
){}