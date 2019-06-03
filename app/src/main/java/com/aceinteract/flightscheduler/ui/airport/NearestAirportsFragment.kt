package com.aceinteract.flightscheduler.ui.airport

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aceinteract.flightscheduler.R
import com.aceinteract.flightscheduler.data.entity.AirportRemote
import com.aceinteract.flightscheduler.databinding.NearestAirportsFragmentBinding
import com.aceinteract.flightscheduler.ui.base.BaseFragment
import com.aceinteract.flightscheduler.ui.flight.finder.FlightFinderFragment
import com.aceinteract.flightscheduler.util.LocationUtil
import com.aceinteract.flightscheduler.util.LocationUtil.Companion.REQUEST_CHECK_SETTINGS
import com.aceinteract.flightscheduler.util.StorageUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import org.koin.android.ext.android.inject


/**
 * Fragment that handles and holds views for the nearest airports.
 */
class NearestAirportsFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private lateinit var binding: NearestAirportsFragmentBinding

    private val storageUtil: StorageUtil by inject()

    private val locationUtil: LocationUtil by lazy { LocationUtil(context!!) }

    /**
     * Setup ui, data binding and view model.
     *
     * Check and request for permissions.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NearestAirportsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@NearestAirportsFragment)[NearestAirportsViewModel::class.java]
        }

        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            start()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_LOCATION
            )
        }

        setupUI()

        return binding.root
    }

    /**
     * Use view model and carry out operations on view model.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeAirports()
    }

    /**
     * Check if permission requests were granted.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_LOCATION -> if (grantResults.size == 2) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    start()
                } else {
                    Toast.makeText(context, "Can't use app without permissions.", Toast.LENGTH_LONG).show()
                    activity?.finish()
                }
            } else {
                Toast.makeText(context, "Can't use app without permissions.", Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
    }

    /**
     * Setup Google Maps.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener {
            findNavController().navigate(R.id.action_nearestAirportsFragment_to_flightFinderFragment, Bundle().apply {
                putString(FlightFinderFragment.ORIGIN_AIRPORT_CODE, it.tag.toString())
            })
            return@setOnMarkerClickListener true
        }
    }

    /**
     * Check if gps request was granted.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) start()
    }

    private fun start() {
        locationUtil.checkLocationSettings {
            it.addOnSuccessListener { location ->
                if (location == null && storageUtil.lastCoordinate.latitude == 0.0) {
                    start()
                } else if (location == null) {
                    val latLng = storageUtil.lastCoordinate
                    binding.viewModel?.loadAirports(latLng.latitude, latLng.longitude)
                } else {
                    storageUtil.lastCoordinate = LatLng(location.latitude, location.longitude)
                    binding.viewModel?.loadAirports(location.latitude, location.longitude)
                }
            }
        }
    }

    private fun setupUI() {

        appCompatActivity.setSupportActionBar(binding.toolbar)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )
        binding.recyclerAirportsSearch.adapter = AirportListAdapter {
            findNavController().navigate(R.id.action_nearestAirportsFragment_to_flightFinderFragment, Bundle().apply {
                putString(FlightFinderFragment.ORIGIN_AIRPORT_CODE, it.code)
            })
        }
        binding.recyclerAirportsSearch.addItemDecoration(dividerItemDecoration)
    }

    private fun makeMarker(airport: AirportRemote): MarkerOptions {
        val iconGenerator = IconGenerator(context)

        iconGenerator.setBackground(null)

        val markerView = View.inflate(activity, R.layout.stop_marker, null)
        iconGenerator.setContentView(markerView)

        markerView.findViewById<TextView>(R.id.text_stop_time).text = airport.airportCode
        markerView.findViewById<TextView>(R.id.text_stop_airport).text = airport.distance.toString()

        return MarkerOptions().apply {
            position(LatLng(airport.position.coordinate.lat, airport.position.coordinate.long))
            icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(airport.airportCode)))
        }
    }

    private fun observeAirports() {
        binding.viewModel?.nearestAirports?.observe(this, Observer { airports ->
            val boundsBuilder = LatLngBounds.Builder()
            airports.forEach { airportResponse ->
                val markerOptions = makeMarker(airportResponse)
                boundsBuilder.include(markerOptions.position)
                map.addMarker(markerOptions).tag = airportResponse.airportCode
            }
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 128))
        })
    }

    companion object {
        private const val PERMISSION_LOCATION = 420
    }

}
