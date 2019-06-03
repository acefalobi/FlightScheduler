package com.aceinteract.flightscheduler.ui.flight.schedule

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.aceinteract.flightscheduler.data.entity.Airport
import com.aceinteract.flightscheduler.data.entity.FlightSchedule
import com.aceinteract.flightscheduler.data.remote.repository.FlightRemoteRepository
import com.aceinteract.flightscheduler.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * View Model class for the Flight Schedule views.
 */
class FlightScheduleViewModel(application: Application) : BaseViewModel(application) {

    private val flightRemoteRepository: FlightRemoteRepository by application.inject()

    /**
     * Observable string for storing the origin airport.
     */
    val originAirport = ObservableField<Airport>()

    /**
     * Observable string for storing the destination airport.
     */
    val destinationAirport = ObservableField<Airport>()

    /**
     * Observable string for storing the departure date.
     */
    val departureDate = ObservableField<String>("")

    /**
     * Observable list of flights schedules for origin and destination.
     */
    val flightSchedules = ObservableArrayList<FlightSchedule>()

    init {
        isLoading.set(true)
    }

    /**
     * Loads schedules asynchronously.
     */
    fun loadSchedules(origin: String, destination: String, fromDate: String) {
        viewModelScope.launch {
            val schedules = flightRemoteRepository.getFlightSchedules(origin, destination, fromDate)
            launch(Dispatchers.Main) {
                isLoading.set(false)
                if (schedules != null) {
                    if (schedules.isNotEmpty()) {
                        flightSchedules.run {
                            clear()
                            addAll(schedules)
                        }
                    }
                } else
                    Toast.makeText(
                        getApplication(),
                        "Problem loading schedules from server.",
                        Toast.LENGTH_SHORT
                    ).show()

            }
        }
    }

}
