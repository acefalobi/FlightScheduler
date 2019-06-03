package com.aceinteract.flightscheduler.ui.flight.finder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aceinteract.flightscheduler.R
import com.aceinteract.flightscheduler.databinding.FlightFinderFragmentBinding
import com.aceinteract.flightscheduler.ui.airport.AirportListAdapter
import com.aceinteract.flightscheduler.ui.base.BaseFragment
import com.aceinteract.flightscheduler.ui.flight.schedule.FlightScheduleFragment

/**
 * Fragment that handles and holds views for the flight finder.
 */
class FlightFinderFragment : BaseFragment() {

    private lateinit var binding: FlightFinderFragmentBinding

    /**
     * Setup data binding and view model.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FlightFinderFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProviders.of(this@FlightFinderFragment)[FlightFinderViewModel::class.java]
        }

        return binding.root
    }

    /**
     * Use view model setup UI
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUI()

        arguments?.getString(ORIGIN_AIRPORT_CODE)?.let {
            binding.viewModel?.loadOriginAirport(it)
        }
    }

    private fun setupUI() {

        appCompatActivity.run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.run {
                title = "Flight Finder"
                setDisplayHomeAsUpEnabled(true)
            }
        }

        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )

        binding.run {
            recyclerAirportsSearch.adapter = AirportListAdapter {
                viewModel?.setAirport(it)
            }
            recyclerAirportsSearch.addItemDecoration(dividerItemDecoration)

            editSearchOrigin.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    viewModel?.searchMode = FlightFinderViewModel.AirportSearchMode.ORIGIN
                    viewModel?.originQuery?.notifyChange()
                }
            }

            editSearchDestination.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    viewModel?.searchMode = FlightFinderViewModel.AirportSearchMode.DESTINATION
                    viewModel?.destinationQuery?.notifyChange()
                }
            }

            buttonFindFlight.setOnClickListener {

                editSearchOrigin.error = null
                editSearchDestination.error = null
                editSearchDepartureDate.error = null

                if (viewModel?.originAirport == null) {
                    editSearchOrigin.error = "Select a valid airport!"
                    return@setOnClickListener
                }

                if (viewModel?.destinationAirport == null) {
                    editSearchDestination.error = "Select a valid airport!"
                    return@setOnClickListener
                }

                if (viewModel?.departureDate?.get().toString().isEmpty()) {
                    editSearchDepartureDate.error = "Input a valid departure date!"
                    return@setOnClickListener
                }

                findNavController().navigate(
                    R.id.action_flightFinderFragment_to_flightScheduleFragment,
                    Bundle().apply {
                        putParcelable(FlightScheduleFragment.ORIGIN_AIRPORT, viewModel?.originAirport!!)
                        putParcelable(FlightScheduleFragment.DESTINATION_AIRPORT, viewModel?.destinationAirport!!)
                        putString(FlightScheduleFragment.DEPARTURE_DATE, viewModel?.departureDate?.get()!!)
                    })

            }
        }

    }

    companion object {

        /**
         * Key for storing and retrieving the origin airport code in a bundle.
         */
        const val ORIGIN_AIRPORT_CODE = "origin_airport_code"

    }

}
