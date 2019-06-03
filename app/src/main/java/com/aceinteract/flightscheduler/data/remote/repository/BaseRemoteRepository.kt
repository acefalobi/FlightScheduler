package com.aceinteract.flightscheduler.data.remote.repository

import com.aceinteract.flightscheduler.data.remote.api.ApiService
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * Base Repository class for fetching data
 */
abstract class BaseRemoteRepository(accessToken: String) {

    /**
     * Api Service reference for making API calls
     */
    protected open val apiService = ApiService(accessToken)


    /**
     * Allows making of safe api calls without error exceptions
     */
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): T? {
        return when (val result: Result<T> = safeApiResult(call)) {
            is Result.Success ->
                result.data
            is Result.Error -> {
                Timber.d("Exception - ${result.exception}")
                null
            }
        }
    }

    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): Result<T> {
        val response = call.invoke()
        try {
            if (response.isSuccessful) return Result.Success(
                response.body()!!
            )
        } catch (e: Exception) {
            return Result.Error(e)
        }

        return Result.Error(IOException("Error Occurred during getting safe Api result."))

    }

    /**
     * Result outcomes.
     */
    sealed class Result<out T : Any> {

        /**
         * Success outcome.
         */
        data class Success<out T : Any>(
            /**
             * Data from successful request
             */
            val data: T
        ) : Result<T>()

        /**
         * Success outcome.
         */
        data class Error(
            /**
             * Exception thrown from error
             */
            val exception: Exception
        ) : Result<Nothing>()

    }

}