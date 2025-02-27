package com.example.myfirstapp.maps
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myfirstapp.BuildConfig
import com.example.myfirstapp.R
import com.example.myfirstapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.launch
/**
 * Activity for displaying a map and user location. It includes features like showing nearby places
 * (e.g., hospitals) and handling location permissions.
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var isFirstLocationUpdate = true

    /**
     * Called when the activity is created. Initializes the map, location services, and places client.
     *
     * @param savedInstanceState The saved state of the activity, if available.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializePlacesClient()
        initializeLocationClient()
        initializeMapFragment()
    }

    /**
     * Initializes the Places client using the API key.
     */
    private fun initializePlacesClient() {
        val apiKey = BuildConfig.MAPS_API_KEY
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            Toast.makeText(this, "No API key", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)
        placesClient = Places.createClient(this)
    }
    /**
     * Initializes the Fused Location Provider Client.
     */
    private fun initializeLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }
    /**
     * Initializes the MapFragment and sets it up to load the map asynchronously.
     */
    private fun initializeMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    /**
     * Called when the map is ready for interaction. It checks location permissions and retrieves the current location.
     *
     * @param googleMap The GoogleMap instance.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show()
    }
    /**
     * Requests and retrieves the current location of the user.
     */
    private fun getCurrentLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location: Location? = locationResult.lastLocation
                location?.let {
                    handleNewLocation(location)
                } ?: run {
                    Toast.makeText(this@MapsActivity, "Unable to fetch location", Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }
    /**
     * Handles the new location by displaying it on the map and searching for nearby places.
     *
     * @param location The current location of the user.
     */
    private fun handleNewLocation(location: Location) {
        Toast.makeText(this, "Location: ${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
        val currentLocation = LatLng(location.latitude, location.longitude)
        if (isFirstLocationUpdate) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
            isFirstLocationUpdate = false
        }
        searchNearbyPlaces(currentLocation)
    }
    /**
     * Searches for nearby places around the current location and displays them on the map.
     *
     * @param currentLocation The current location of the user.
     */
    private fun searchNearbyPlaces(currentLocation: LatLng) {
        val placeFields = listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION)
        val circle = CircularBounds.newInstance(currentLocation, 10000.0)
        val includedTypes = listOf("hospital")

        val searchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields)
            .setIncludedTypes(includedTypes)
            .setMaxResultCount(10)
            .build()

        placesClient.searchNearby(searchNearbyRequest)
            .addOnSuccessListener { response ->
                val places = response.places
                Log.d("Places API Response", places.toString())
                if (places.isEmpty()) {
                    Log.d("MapsActivity", "No places found.")
                    Toast.makeText(this, "No places found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                Toast.makeText(this, "Places found: ${places.size}", Toast.LENGTH_SHORT).show()
                for (place in places) {
                    val placeName = place.displayName
                    val placeLatLng = place.location
                    val marker = mMap.addMarker(MarkerOptions().position(placeLatLng!!).title(placeName))
                }
                if (places.isNotEmpty()) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(places[0].location!!, 15f))
                }

                mMap.setOnInfoWindowClickListener { marker ->
                    val place = places.find { it.location == marker.position }
                }

            }
            .addOnFailureListener { exception ->
                Log.e("MapsActivity", "Error searching nearby places", exception)
                Toast.makeText(this, "Error searching nearby places", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Handles the result of location permission request.
     *
     * @param requestCode The request code passed to requestPermissions.
     * @param permissions The requested permissions.
     * @param grantResults The results of the requested permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission denied. Can't show location.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /**
     * Cleans up resources and removes location updates when the activity is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}