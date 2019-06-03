package com.aceinteract.flightscheduler.data.remote.api

import com.aceinteract.flightscheduler.data.entity.FlightSchedule
import com.aceinteract.flightscheduler.data.adapter.FlightScheduleTypeAdapter
import com.aceinteract.flightscheduler.data.adapter.CalendarTypeAdapter
import com.aceinteract.flightscheduler.data.remote.api.response.AccessTokenResponse
import com.aceinteract.flightscheduler.data.remote.api.response.AirlineResponse
import com.aceinteract.flightscheduler.data.remote.api.response.FlightSchedulesResponse
import com.aceinteract.flightscheduler.data.remote.api.response.NearestAirportsResponse
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

/**
 * API interface containing all endpoints.
 */
interface ApiService {

    /**
     * [POST] Api Service function that gets a new access token.
     * @param clientId Lufthansa application client ID.
     * @param clientSecret Lufthansa application client secret.
     * @param grantType Type of token to grant request.
     * @return Kotlin Coroutines response object.
     */
    @POST(ApiEndpoints.POST.ACCESS_TOKEN)
    @FormUrlEncoded
    fun getAccessTokenAsync(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String
    ): Deferred<Response<AccessTokenResponse>>

    /**
     * [GET] Api Service function that gets nearest airports to coordinates.
     * @param latitude Latitude of coordinates.
     * @param longitude Longitude of coordinates.
     * @return Kotlin Coroutines response object.
     */
    @GET(ApiEndpoints.GET.NEAREST_AIRPORTS + "{latitude},{longitude}")
    fun getNearestAirportsAsync(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): Deferred<Response<NearestAirportsResponse>>

    /**
     * [GET] Api Service function that gets available flight schedules between airports.
     * @param origin Airport code of origin airport.
     * @param destination Airport code of destination airport.
     * @param fromDate Local departure date.
     * @return Kotlin Coroutines response object.
     */
    @GET(ApiEndpoints.GET.FLIGHT_SCHEDULES + "{origin}/{destination}/{fromDateTime}")
    fun getFlightSchedulesAsync(
        @Path("origin") origin: String,
        @Path("destination") destination: String,
        @Path("fromDateTime") fromDate: String
    ): Deferred<Response<FlightSchedulesResponse>>

    /**
     * [GET] Api Service function that gets a single airline.
     * @param airlineCode 2-character code of the airline/carrier.
     * @return Kotlin Coroutines response object.
     */
    @GET(ApiEndpoints.GET.AIRLINES + "{airlineCode}")
    fun getAirlineAsync(@Path("airlineCode") airlineCode: String): Deferred<Response<AirlineResponse>>

    companion object {

        /**
         * Creates retrofit instance of api service.
         */
        operator fun invoke(accessToken: String): ApiService {

            val authInterceptor = Interceptor {

                if (it.request().method().equals("get", true)) {
                    val request = it.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .build()

                    it.proceed(request)
                } else {
                    it.proceed(it.request())
                }

            }

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)

            val builder = Retrofit.Builder()
                .baseUrl(ApiEndpoints.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .registerTypeAdapter(Calendar::class.java,
                                CalendarTypeAdapter()
                            )
                            .registerTypeAdapter(FlightSchedule::class.java,
                                FlightScheduleTypeAdapter()
                            )
                            .create()
                    )
                )

            val retrofit = builder.client(httpClient.build()).build()

            /**
             * Api Retrofit Service reference to make retrofit calls
             */
            return retrofit.create(ApiService::class.java)

        }

    }

}