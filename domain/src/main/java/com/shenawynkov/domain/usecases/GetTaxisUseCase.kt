package com.shenawynkov.domain.usecases

import com.shenawynkov.domain.model.Poi
import com.shenawynkov.domain.model.TaxiParams
import com.shenawynkov.domain.common.Resource
import com.shenawynkov.domain.repo.TaxiRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaxisUseCase @Inject constructor(private val repo: TaxiRepo) {
    operator fun invoke(taxiParams: TaxiParams) : Flow<Resource<List<Poi>>> = repo.getTaxis(taxiParams)
}