package com.aceinteract.flightscheduler.data.remote.repository

import com.aceinteract.flightscheduler.data.remote.api.ApiService

/**
 * Source class for getting authentication data from remote data sets
 */
class AuthRemoteRepository(override val apiService: ApiService = ApiService("")) : BaseRemoteRepository("") {

    /**
     * Gets and returns Access Token from API
     */
    suspend fun getAccessToken(clientId: String, clientSecret: String): String? {

        val accessTokenResponse =
            safeApiCall { apiService.getAccessTokenAsync(clientId, clientSecret, "client_credentials").await() }

        return accessTokenResponse?.accessToken

    }

}