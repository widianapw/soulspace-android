package com.soulspace.app.di

import android.content.Context
import com.soulspace.app.common.AuthInterceptor
import com.soulspace.app.common.Constants
import com.soulspace.app.common.TokenManager
import com.soulspace.app.data.remote.SoulSpaceApi
import com.soulspace.app.data.repository.SoulSpaceRepositoryImpl
import com.soulspace.app.domain.repository.SoulSpaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()
    }

    @Provides
    @Singleton
    fun provideSoulSpaceApi(okHttpClient: OkHttpClient): SoulSpaceApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
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
