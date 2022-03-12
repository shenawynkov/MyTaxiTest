package com.shenawynkov.domain.repo

import com.shenawynkov.domain.common.Resource
import com.shenawynkov.domain.model.Poi
import com.shenawynkov.domain.model.TaxiParams
import kotlinx.coroutines.flow.Flow

interface TaxiRepo {
    fun getTaxis(taxiParams: TaxiParams):Flow<Resource<List<Poi>>>
}