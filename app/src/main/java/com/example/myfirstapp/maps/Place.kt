package com.example.myfirstapp.maps

import com.google.android.gms.maps.model.LatLng
/**
 * Data class representing a place with a name, geographical coordinates, and an address.
 *
 * @property name The name of the place.
 * @property latLng The latitude and longitude of the place.
 * @property address The address of the place.
 */
data class Place(
    val name: String,
    val latLng: LatLng,
    val address: String
)