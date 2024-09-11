package com.soulspace.app.di

import com.soulspace.app.common.Constants
import com.soulspace.app.data.remote.SoulSpaceApi
import com.soulspace.app.data.repository.SoulSpaceRepositoryImpl
import com.soulspace.app.domain.repository.SoulSpaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSoulSpaceApi(): SoulSpaceApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SoulSpaceApi::class.java)
    }


    @Provides
    @Singleton
    fun provideSoulSpaceRepository(soulSpaceApi: SoulSpaceApi): SoulSpaceRepository {
        return SoulSpaceRepositoryImpl(soulSpaceApi)
    }
}