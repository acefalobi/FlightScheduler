package com.aceinteract.flightscheduler.ui.flight.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.aceinteract.flightscheduler.R
import com.aceinteract.flightscheduler.databinding.FlightScheduleFragmentBinding
import com.aceinteract.flightscheduler.ui.base.BaseFragment
import com.aceinteract.flightscheduler.ui.flight.schedule.map.FlightScheduleMapFragment
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment that handles and holds views for flight schedules.
 */
class FlightScheduleFragment : BaseFragment() {

    private lateinit var binding: FlightScheduleFragmentBinding

    /**
     * Setup data binding and view model.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FlightScheduleFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@FlightScheduleFragment)[FlightScheduleViewModel::class.java]
        }

        Snackbar.make(binding.root, "Loading flight schedules. This might take a while...", Snackbar.LENGTH_LONG).show()

        setupUI()

        return binding.root
    }

    /**
     * Use view model setup UI
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {

            binding.viewModel?.run {

                originAirport.set(arguments?.getParcelable(ORIGIN_AIRPORT)!!)
                destinationAirport.set(arguments?.getParcelable(DESTINATION_AIRPORT)!!)

                val departureDate = arguments?.getString(DEPARTURE_DATE)!!

                this.departureDate.set(departureDate)

                loadSchedules(originAirport.get()!!.code, destinationAirport.get()!!.code, departureDate)

            }

        } catch (e: Exception) {
            throw IllegalArgumentException("Necessary arguments went passed into the fragment.")
        }
    }

    private fun setupUI() {

        appCompatActivity.run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.run {
                title = "Flight Schedules"
                setDisplayHomeAsUpEnabled(true)
            }
        }

        binding.recyclerSchedules.adapter = FlightScheduleListAdapter {

            findNavController().navigate(
                R.id.action_flightScheduleFragment_to_flightScheduleMapFragment,
                Bundle().apply { putParcelable(FlightScheduleMapFragment.FLIGHT_SCHEDULE, it) })
        }

    }

    companion object {

        /**
         * Key for storing and retrieving the origin airport.
         */
        const val ORIGIN_AIRPORT = "origin_airport"

        /**
         * Key for storing and retrieving the origin airport.
         */
        const val DESTINATION_AIRPORT = "destination_airport"

        /**
         * Key for storing and retrieving the departure date in a bundle.
         */
        const val DEPARTURE_DATE = "departure_date"

    }

}
