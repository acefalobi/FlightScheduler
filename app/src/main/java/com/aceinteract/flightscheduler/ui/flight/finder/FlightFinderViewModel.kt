package com.aceinteract.flightscheduler.ui.flight.finder

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.aceinteract.flightscheduler.data.entity.Airport
import com.aceinteract.flightscheduler.data.remote.repository.AirportRemoteRepository
import com.aceinteract.flightscheduler.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * View Model class for the Flight Finder views.
 */
class FlightFinderViewModel(application: Application) : BaseViewModel(application) {

    private val airportRemoteRepository: AirportRemoteRepository by application.inject()

    /**
     * Origin airport to find schedules from
     */
    var originAirport: Airport? = null
        private set(value) {
            field = value
        }

    /**
     * Destination airport to find flights from
     */
    var destinationAirport: Airport? = null
        private set(value) {
            field = value
        }

    /**
     * Observable enum field for the current search mode.
     */
    var searchMode =
        AirportSearchMode.ORIGIN

    /**
     * Observable list of airports that match the search result.
     */
    val searchAirports = ObservableArrayList<Airport>()

    /**
     * Observable string for searching through airports.
     */
    val originQuery = ObservableField<String>("")

    /**
     * Observable string for searching through airports.
     */
    val destinationQuery = ObservableField<String>("")

    /**
     * Observable string for storing the departure date
     */
    val departureDate = ObservableField<String>("")

    init {
        searchAirports("")
        isLoading.set(true)

        originQuery.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                searchAirports(originQuery.get().toString())
            }
        })

        destinationQuery.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                searchAirports(destinationQuery.get().toString())
            }
        })
    }

    /**
     * Searches and loads in origin airport from code asynchronously
     */
    fun loadOriginAirport(code: String) {
        viewModelScope.launch {
            val airport = airportRemoteRepository.getAirport(code)
            airport?.let {
                searchMode = AirportSearchMode.ORIGIN
                setAirport(it)
            }
        }
    }

    /**
     * Searches and loads airports from query asynchronously
     */
    fun searchAirports(query: String) {
        viewModelScope.launch {
            var airports = airportRemoteRepository.searchAirports(query)
            if (airports.isNotEmpty()) {
                airports = when (searchMode) {
                    AirportSearchMode.ORIGIN -> {
                        airports.filter { it.code != destinationAirport?.code }
                    }
                    AirportSearchMode.DESTINATION -> {
                        airports.filter { it.code != originAirport?.code }
                    }
                }
                searchAirports.run {
                    clear()
                    addAll(airports)
                }
            }
        }
    }

    /**
     * Sets the values of the destination or origin airport fields based on the current search mode
     */
    fun setAirport(airport: Airport) {
        when (searchMode) {
            AirportSearchMode.ORIGIN -> {
                originQuery.set(airport.name)
                originAirport = airport
            }
            AirportSearchMode.DESTINATION -> {
                destinationQuery.set(airport.name)
                destinationAirport = airport
            }
        }
    }

    /**
     * Enum class for different airport search modes
     */
    enum class AirportSearchMode {
        /**
         * Enum type for origin airport
         */
        ORIGIN,

        /**
         * Enum type for destination airport
         */
        DESTINATION
    }

}
