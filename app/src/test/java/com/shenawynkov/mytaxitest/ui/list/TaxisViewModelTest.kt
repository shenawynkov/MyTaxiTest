package com.shenawynkov.mytaxitest.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shenawynkov.domain.common.Resource
import com.shenawynkov.domain.model.Coordinate
import com.shenawynkov.domain.model.Poi
import com.shenawynkov.domain.model.TaxiParams
import com.shenawynkov.domain.usecases.GetTaxisUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaxisViewModelTest {
    private lateinit var SUT: TaxisViewModel
    private val getTaxisUseCase: GetTaxisUseCase = mockk()

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

        SUT = TaxisViewModel(getTaxisUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTaxis_Success_DataReturned() = testScope.runBlockingTest {
        //Arrange
        val poi = Poi(
            Coordinate(1.0, 1.0), "car", 0.0, 1
        )
        val pois = arrayListOf(poi)
        val flow = flow<Resource<List<Poi>>> {
            emit(Resource.Loading())
            emit(Resource.Success(pois))

        }
        coEvery {
            getTaxisUseCase.invoke(any())
        } returns flow

        //Act
        SUT.fetchPois(TaxiParams(1.0, 1.0, 2.0, 2.0))
        //Assert
        assertEquals(pois, SUT.pois.value)
        assertEquals(null, SUT.message.value)
        assertEquals(false, SUT.loading.value)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTaxis_Error_ErrorMessagePassed() = testScope.runBlockingTest {
        //Arrange
        val error = "error"
        val flow = flow<Resource<List<Poi>>> {
            emit(Resource.Loading())
            emit(Resource.Error(error))

        }
        coEvery {
            getTaxisUseCase.invoke(any())
        } returns flow
        //Act
        SUT.fetchPois(TaxiParams(1.0, 1.0, 2.0, 2.0))
        //Assert
        assertEquals(error, SUT.message.value)
        assertEquals(false, SUT.loading.value)
    }


}