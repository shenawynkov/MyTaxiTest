package com.shenawynkov.mytaxitest.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.shenawynkov.domain.model.Poi
import com.shenawynkov.mytaxitest.R
import com.shenawynkov.mytaxitest.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var vehicles: List<Poi>
    val boundsBuilder = LatLngBounds.builder()
    val markers = ArrayList<Marker>()
    var selectedPoi: Poi? = null

    companion object {
        val POIS = "POIS"
        val POI = "POI"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //init data
        vehicles = intent.extras?.getParcelableArrayList<Poi>(POIS) ?: ArrayList()
        selectedPoi = intent.extras?.getParcelable(POI)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        vehicles.forEach { poi ->
            val latLng = LatLng(poi.coordinate.latitude, poi.coordinate.longitude)
            //add to map bounds
            boundsBuilder.include(latLng)

            //draw the marker on the map then add it's reference in our marker list
            addMarker(latLng, poi.fleetType)?.let { marker ->
                markers.add(
                    marker
                )
            }
        }

        selectedPoi?.let {
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        it.coordinate.latitude,
                        it.coordinate.longitude
                    ), 16f
                )
            )

        }
        //set bounds with all the map points
        if (selectedPoi == null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50))


    }

    private fun addMarker(latLng: LatLng, title: String): Marker? {

        return mMap.addMarker(
            MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                .position(latLng).title(title)
        )
    }
}