package com.aceinteract.flightscheduler.data.remote.repository

import com.aceinteract.flightscheduler.data.remote.api.ApiService
import com.aceinteract.flightscheduler.util.DummyFactoryUtil
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

@RunWith(MockitoJUnitRunner::class)
class AirlineRemoteRepositoryTest {

    @Mock
    lateinit var apiService: ApiService

    private lateinit var repository: AirlineRemoteRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = AirlineRemoteRepository("", apiService)
    }

    @Test
    fun `check that getting airline from repo completes successfully`() {
        val airline = DummyFactoryUtil.createAirlineResponse("XX", "Random Airline")
        runBlocking {
            whenever(
                apiService
                    .getAirlineAsync(any())
            ).thenReturn(async { Calls.response(airline).execute() })
        }
        val testAirline = runBlocking { repository.getAirline("XX") }
        Assert.assertNotNull(testAirline)
        Assert.assertEquals(testAirline?.airlineID, airline.resource.airlineData.airline.airlineID)
    }

}