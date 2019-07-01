package com.aceinteract.flightscheduler.data.remote.repository

import com.aceinteract.flightscheduler.data.entity.Airline
import com.aceinteract.flightscheduler.data.remote.api.ApiService

/**
 * Source class for getting airport data from remote data sets.
 */
class AirlineRemoteRepository(
    accessToken: String,
    override val apiService: ApiService = ApiService(accessToken)
) : BaseRemoteRepository(accessToken) {

    /**
     * Gets and returns airline details from API.
     */
    suspend fun getAirline(airlineCode: String): Airline? {

        val response = safeApiCall { apiService.getAirlineAsync(airlineCode).await() }

        return response?.resource?.airlineData?.airline

    }
}