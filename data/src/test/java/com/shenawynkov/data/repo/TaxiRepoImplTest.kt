package com.shenawynkov.data.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.shenawynkov.data.model.TaxisResponse
import com.shenawynkov.data.remote.ApiService
import com.shenawynkov.domain.model.Coordinate
import com.shenawynkov.domain.model.Poi
import com.shenawynkov.domain.model.TaxiParams
import com.shenawynkov.domain.common.Resource

import io.mockk.coEvery
import io.mockk.mockk

import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class TaxiRepoImplTest {
    private lateinit var SUT: TaxiRepoImpl
    private val apiService: ApiService = mockk()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    private val testScope = TestCoroutineScope(testDispatcher)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        SUT = TaxiRepoImpl(apiService)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTaxis_Success_DataReturned() = testScope.runBlockingTest {
        val poi = Poi(
            Coordinate(1.0, 1.0), "car", 0.0, 1
        )
        val response = TaxisResponse(arrayListOf(poi))

        coEvery {
            apiService.getTaxis(any(), any(), any(), any())
        } returns response

        //Act
        val result = SUT.getTaxis(TaxiParams(1.0, 1.0, 2.0, 2.0))

        //Assert
        result.test {
            val loading = awaitItem()
            val data = awaitItem()
            Truth.assertThat(loading).isInstanceOf(Resource.Loading<List<Poi>>().javaClass)
            Truth.assertThat(data.data).isEqualTo(arrayListOf(poi))
            awaitComplete()

        }


    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTaxis_NetworkError_ErrorMessageReturned() = testScope.runBlockingTest {

        //arrange
        val error="Couldn't reach server. Check your internet connection."
        coEvery {
            apiService.getTaxis(any(), any(), any(), any())
        } throws IOException(error)

        //Act
        val result = SUT.getTaxis(TaxiParams(1.0, 1.0, 2.0, 2.0))

        //Assert
        result.test {
            val data = awaitItem()

            Truth.assertThat(data.message).isEqualTo(error)
            awaitComplete()

        }


    }
    @ExperimentalCoroutinesApi
    @Test
    fun getTaxis_Error_ErrorMessageReturned() = testScope.runBlockingTest {

        //arrange
        val error="HTTP 500 Response.error()"
        coEvery {
            apiService.getTaxis(any(), any(), any(), any())
        } throws HttpException(Response.error<Any>(500,
            error.toResponseBody("text/plain".toMediaTypeOrNull())
        ))

        //Act
        val result = SUT.getTaxis(TaxiParams(1.0, 1.0, 2.0, 2.0))

        //Assert
        result.test {
            val data = awaitItem()

            Truth.assertThat(data.message).isEqualTo(error)
            awaitComplete()

        }


    }
}