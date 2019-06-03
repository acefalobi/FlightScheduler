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
class AirportRemoteRepositoryTest {

    @Mock
    lateinit var gson: Gson

    @Mock
    lateinit var apiService: ApiService

    private lateinit var repository: AirportRemoteRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = AirportRemoteRepository("[]", "", apiService, gson)
    }

    @Test
    fun `check that getting nearest airports from repo completes successfully`() {
        val airports = DummyFactoryUtil.createNearestAirportsResponse(5)
        runBlocking {
            whenever(
                apiService
                    .getNearestAirportsAsync(any(), any())
            ).thenReturn(async { Calls.response(airports).execute() })
        }
        val testAirports = runBlocking { repository.getNearestAirports(0.0, 0.0) }
        Assert.assertNotNull(testAirports)
        Assert.assertEquals(testAirports?.size, airports.resource.airportsData.airports.size)
    }

    @Test
    fun `check that getting a single airport via airport code from repo completes successfully`() {
        val airport = DummyFactoryUtil.createAirport("XYZ")

        whenever(gson.fromJson<List<Airport>>(any<String>(), any<Type>())).thenReturn(arrayListOf(airport))

        val testAirport = runBlocking { repository.getAirport("XYZ") }
        Assert.assertNotNull(testAirport)
        Assert.assertEquals(testAirport?.code, airport.code)

    }

    @Test
    fun `check that getting searching airport for a query from repo completes successfully`() {
        val airports = DummyFactoryUtil.createAirports(8)

        whenever(gson.fromJson<List<Airport>>(any<String>(), any<Type>())).thenReturn(airports)

        val testAirports = runBlocking { repository.searchAirports("") }
        Assert.assertNotNull(testAirports)
        Assert.assertEquals(testAirports.size, airports.size)
    }

}