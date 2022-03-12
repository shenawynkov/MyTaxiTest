package com.shenawynkov.mytaxitest.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shenawynkov.domain.common.Resource
import com.shenawynkov.domain.model.Poi
import com.shenawynkov.domain.model.TaxiParams
import com.shenawynkov.domain.usecases.GetTaxisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TaxisViewModel @Inject constructor(val getTaxisUseCase: GetTaxisUseCase) : ViewModel() {
    //default param
    private val hamburg = TaxiParams(
        p1Lat = 53.694865,
        p1Lon = 9.757589,
        p2Lat = 53.394655,
        p2Lon = 10.099891
    )

    private val _pois = MutableStateFlow<List<Poi>>(ArrayList())
    val pois: StateFlow<List<Poi>> = _pois
    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message


    fun fetchPois(parms: TaxiParams = hamburg) {

        getTaxisUseCase(parms).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _loading.value = false
                    result.data?.let { pois ->
                        _pois.value = pois
                    }

                }
                is Resource.Error -> {
                    _loading.value = false
                    _message.value = result.message

                }
                is Resource.Loading -> {
                    _loading.value = true
                }

            }
        }.launchIn(viewModelScope)
    }
}