package com.aceinteract.flightscheduler.data.remote.repository

import com.aceinteract.flightscheduler.data.entity.Airport
import com.aceinteract.flightscheduler.data.remote.api.ApiService
import com.aceinteract.flightscheduler.util.DummyFactoryUtil
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.mock.Calls
import java.lang.reflect.Type

@RunWith(MockitoJUnitRunner::class)
class FlightRemoteRepositoryTest {

    @Mock
    lateinit var gson: Gson

    @Mock
    lateinit var apiService: ApiService

    private lateinit var repository: FlightRemoteRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = FlightRemoteRepository(
            "[]",
            "",
            apiService,
            AirlineRemoteRepository("", apiService),
            AirportRemoteRepository("[]", "", apiService, gson)
        )
    }

    @Test
    fun `check that fetching flight schedules from repo completes successfully`() {
        val airline = DummyFactoryUtil.createAirlineResponse("XX", "Random Airline")
        val schedules = DummyFactoryUtil.createFlightSchedules(8, "XYZ", airline.resource.airlineData.airline)
        val airport = DummyFactoryUtil.createAirport("XYZ")
        runBlocking {
            whenever(apiService.getFlightSchedulesAsync(any(), any(), any()))
                .thenReturn(async { Calls.response(schedules).execute() })
            whenever(apiService.getAirlineAsync(any()))
                .thenReturn(async { Calls.response(airline).execute() })
            whenever(gson.fromJson<List<Airport>>(any<String>(), any<Type>())).thenReturn(arrayListOf(airport))
        }
        val testSchedules = runBlocking { repository.getFlightSchedules("XYZ", "ABC", "0000-00-00") }
        Assert.assertNotNull(testSchedules)
        Assert.assertEquals(testSchedules?.size, schedules.scheduleResource.schedules.size)
        Assert.assertEquals(
            testSchedules?.get(0)?.flights?.get(0)?.airline?.airlineID,
            schedules.scheduleResource.schedules[0].flights[0].airline?.airlineID
        )
    }

}