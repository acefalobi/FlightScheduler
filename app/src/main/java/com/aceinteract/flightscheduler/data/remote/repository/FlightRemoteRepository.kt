package com.aceinteract.flightscheduler.data.remote.repository

import com.aceinteract.flightscheduler.data.entity.FlightSchedule
import com.aceinteract.flightscheduler.data.entity.Position
import com.aceinteract.flightscheduler.data.remote.api.ApiService

/**
 * Source class for getting flight data from remote data sets.
 */
class FlightRemoteRepository(
    private val jsonSource: String,
    accessToken: String,
    override val apiService: ApiService = ApiService(accessToken),
    private val airlineRemoteRepository: AirlineRemoteRepository = AirlineRemoteRepository(accessToken, apiService),
    private val airportRemoteRepository: AirportRemoteRepository = AirportRemoteRepository(jsonSource, accessToken)
) : BaseRemoteRepository(accessToken) {

    /**
     * Gets flight schedules from origin to destination from API.
     */
    suspend fun getFlightSchedules(origin: String, destination: String, fromDate: String): List<FlightSchedule>? {

        val response = safeApiCall { apiService.getFlightSchedulesAsync(origin, destination, fromDate).await() }

        val schedules = response?.scheduleResource?.schedules

        schedules?.forEach { schedule ->
            schedule.flights[0].airline = airlineRemoteRepository.getAirline(schedule.flights[0].carrier.airlineID)
            schedule.flights.forEach {
                it.arrival.position = Position.Coordinate(
                    airportRemoteRepository.getAirport(it.arrival.airportCode)?.latitude!!.toDouble(),
                    airportRemoteRepository.getAirport(it.arrival.airportCode)?.longitude!!.toDouble()
                )
                it.departure.position = Position.Coordinate(
                    airportRemoteRepository.getAirport(it.departure.airportCode)?.latitude!!.toDouble(),
                    airportRemoteRepository.getAirport(it.departure.airportCode)?.longitude!!.toDouble()
                )
            }
        }

        return response?.scheduleResource?.schedules
    }

}