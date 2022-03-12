package com.shenawynkov.data.repo

import com.shenawynkov.data.remote.ApiService
import com.shenawynkov.domain.common.Resource
import com.shenawynkov.domain.model.Poi
import com.shenawynkov.domain.model.TaxiParams
import com.shenawynkov.domain.repo.TaxiRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class TaxiRepoImpl(private val apiService: ApiService) : TaxiRepo {
    override fun getTaxis(taxiParams: TaxiParams): Flow<Resource<List<Poi>>> {
        return flow {

            try {
                val result = apiService.getTaxis(
                    firstLat = taxiParams.p1Lat,
                    firstLon = taxiParams.p1Lon,
                    secondLat = taxiParams.p2Lat,
                    secondLon = taxiParams.p2Lon
                )
                //emit loading until receiving data
                emit(Resource.Loading<List<Poi>>())
                //emit  data
                emit(Resource.Success(result.poiList))

            } catch (e: HttpException) {
                emit(
                    Resource.Error<List<Poi>>(
                        e.localizedMessage ?: "An unexpected error occurred"
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error<List<Poi>>(
                         "Couldn't reach server. Check your internet connection."

                    )
                )
            }


        }
    }
}