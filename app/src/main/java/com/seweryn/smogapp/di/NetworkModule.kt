package com.seweryn.smogapp.di

import com.google.gson.GsonBuilder
import com.seweryn.smogapp.data.Constants
import com.seweryn.smogapp.data.model.City
import com.seweryn.smogapp.data.remote.SmogApi
import com.seweryn.smogapp.tools.network.CityDeserializer
import com.seweryn.smogapp.tools.network.NetworkConnectionInterceptor
import com.seweryn.smogapp.utils.network.ConnectionManager
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(connectionManager: ConnectionManager): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(connectionManager))
            .build()

    @Provides
    @Singleton
    fun provideSmogApiClient(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return buildRetrofitClient(
            gsonConverterFactory,
            okHttpClient,
            Constants.BASE_URL
        )
    }

    @Provides
    @Singleton
    fun provideSmogApiInterface(retrofit: Retrofit): SmogApi {
        return retrofit.create(SmogApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder().apply {
            registerTypeAdapter(City::class.java, CityDeserializer())
        }.create()

        return GsonConverterFactory.create(gson)
    }

    private fun buildRetrofitClient(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}