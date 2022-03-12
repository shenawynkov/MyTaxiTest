package com.shenawynkov.mytaxitest.di


import com.shenawynkov.data.remote.ApiService
import com.shenawynkov.data.repo.TaxiRepoImpl
import com.shenawynkov.domain.repo.TaxiRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Provides
    @Singleton
    fun provideHomeRepository(apiService: ApiService): TaxiRepo {
        return TaxiRepoImpl(apiService)
    }

}