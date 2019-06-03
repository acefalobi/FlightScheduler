package com.aceinteract.flightscheduler.ui.airport

import android.app.Application
import android.widget.Toast
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aceinteract.flightscheduler.App
import com.aceinteract.flightscheduler.data.entity.Airport
import com.aceinteract.flightscheduler.data.entity.AirportRemote
import com.aceinteract.flightscheduler.data.remote.repository.AirportRemoteRepository
import com.aceinteract.flightscheduler.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * View Model class for the Nearest Airports view.
 */
class NearestAirportsViewModel(application: Application) : BaseViewModel(application) {

    private val airportRemoteRepository: AirportRemoteRepository by application.inject()

    /**
     * Observable string for searching through airports.
     */
    val queryString = ObservableField<String>("")

    /**
     * Observable list of airports that match the search result.
     */
    val searchAirports = ObservableArrayList<Airport>()

    /**
     * Live data object of list of nearest airports fetched from repository and bound to view.
     */
    val nearestAirports = MutableLiveData<List<AirportRemote>>()

    init {
        isLoading.set(true)
        queryString.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                searchAirports(queryString.get().toString())
            }
        })
    }

    /**
     * Searches and loads airports from query asynchronously
     */
    fun searchAirports(query: String) {
        viewModelScope.launch {
            val airports = airportRemoteRepository.searchAirports(query)
            if (airports.isNotEmpty()) {
                searchAirports.run {
                    clear()
                    addAll(airports)
                }
            }
        }
    }

    /**
     * Loads nearest airports asynchronously
     */
    fun loadAirports(latitude: Double, longitude: Double) {
        isLoading.set(true)
        viewModelScope.launch {
            val airports = airportRemoteRepository.getNearestAirports(latitude, longitude)
            launch(Dispatchers.Main) {
                if (airports != null) {
                    nearestAirports.postValue(airports)
                } else {
                    Toast.makeText(getApplication<App>(), "Problem fetching airports", Toast.LENGTH_SHORT).show()
                }
                isLoading.set(false)
            }
        }
    }

}
