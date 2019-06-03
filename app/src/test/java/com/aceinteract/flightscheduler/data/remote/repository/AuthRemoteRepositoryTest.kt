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
class AuthRemoteRepositoryTest {

    @Mock
    lateinit var apiService: ApiService

    private lateinit var repository: AuthRemoteRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = AuthRemoteRepository(apiService)
    }

    @Test
    fun `check that getting access token from repo completes successfully`() {
        val accessToken = DummyFactoryUtil.createAccessToken("123ABC")
        runBlocking {
            whenever(apiService.getAccessTokenAsync(any(), any(), any()))
                .thenReturn(async { Calls.response(accessToken).execute() })
        }
        val testAccessToken = runBlocking { repository.getAccessToken("", "") }
        Assert.assertNotNull(testAccessToken)
        Assert.assertEquals(testAccessToken, accessToken.accessToken)
    }

}