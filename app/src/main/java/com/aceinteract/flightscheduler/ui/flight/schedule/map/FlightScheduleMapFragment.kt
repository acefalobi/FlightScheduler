package com.aceinteract.flightscheduler.ui.flight.schedule.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.aceinteract.flightscheduler.R
import com.aceinteract.flightscheduler.data.entity.FlightSchedule
import com.aceinteract.flightscheduler.databinding.FlightScheduleMapFragmentBinding
import com.aceinteract.flightscheduler.ui.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator

/**
 * Fragment that handles and holds views for the flight schedule map.
 */
class FlightScheduleMapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private lateinit var binding: FlightScheduleMapFragmentBinding

    /**
     * Setup ui and data binding.
     *
     * Check and request for permissions.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FlightScheduleMapFragmentBinding.inflate(inflater, container, false).apply {
            schedule = arguments?.getParcelable(FLIGHT_SCHEDULE)!!
        }

        setupUI()

        return binding.root
    }

    /**
     * Setup Google Maps.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener { return@setOnMarkerClickListener true }

        val boundsBuilder = LatLngBounds.Builder()
        val polyline = PolylineOptions()
            .width(5f)
            .color(ContextCompat.getColor(context!!, R.color.color_accent))
            .geodesic(true)
        binding.schedule?.flights?.forEachIndexed { index, flight ->

            if (index > 0) {
                val markerOptions = makeMarker(flight.arrival)
                boundsBuilder.include(markerOptions.position)
                polyline.add(markerOptions.position)
                map.addMarker(markerOptions)
            } else {
                var newMarker = map.addMarker(makeMarker(flight.departure))
                boundsBuilder.include(newMarker.position)
                polyline.add(newMarker.position)

                newMarker = map.addMarker(makeMarker(flight.arrival))
                boundsBuilder.include(newMarker.position)
                polyline.add(newMarker.position)
            }

        }

        map.addPolyline(polyline)

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 130))
    }

    private fun makeMarker(stop: FlightSchedule.Flight.Stop): MarkerOptions {
        val iconGenerator = IconGenerator(context)

        iconGenerator.setBackground(null)

        val markerView = View.inflate(activity, R.layout.stop_marker, null)
        iconGenerator.setContentView(markerView)

        markerView.findViewById<TextView>(R.id.text_stop_time).text = stop.scheduledTime.timeInString
        markerView.findViewById<TextView>(R.id.text_stop_airport).text = stop.airportCode

        return MarkerOptions().apply {
            position(LatLng(stop.position!!.lat, stop.position!!.long))
            icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(stop.airportCode)))
        }
    }

    private fun setupUI() {

        appCompatActivity.run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.run {
                title = "Flight Schedule"
                setDisplayHomeAsUpEnabled(true)
            }
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    companion object {
        /**
         * Key for storing and retrieving the the flight schedule.
         */
        const val FLIGHT_SCHEDULE = "flight_schedule"
    }

}
