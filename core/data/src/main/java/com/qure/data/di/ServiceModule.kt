package com.qure.data.di

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.AuthService
import com.qure.data.api.FishingSpotService
import com.qure.data.api.MemoService
import com.qure.data.api.NaverMapService
import com.qure.data.api.StorageService
import com.qure.data.api.WeatherService
import com.qure.data.api.interceptor.AuthInterceptor
import com.qure.data.api.interceptor.MapInterceptor
import com.qure.data.api.interceptor.WeatherInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Auth

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Weather

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Map

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Storage

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FishingSpot

    @Provides
    @Singleton
    fun providesAuthService(
        @Auth retrofit: Retrofit,
    ): AuthService = retrofit.create()

    @Provides
    @Singleton
    fun providesWeatherService(
        @Weather retrofit: Retrofit,
    ): WeatherService = retrofit.create()

    @Provides
    @Singleton
    fun providesMapService(
        @Map retrofit: Retrofit,
    ): NaverMapService = retrofit.create()

    @Provides
    @Singleton
    fun providesMemoService(
        @Auth retrofit: Retrofit,
    ): MemoService = retrofit.create()

    @Provides
    @Singleton
    fun providesStorageService(
        @Storage retrofit: Retrofit,
    ): StorageService = retrofit.create()

    @Provides
    @Singleton
    fun providesFishingSpotService(
        @FishingSpot retrofit: Retrofit,
    ): FishingSpotService = retrofit.create()

    @Singleton
    @Provides
    @Auth
    fun providesAuthRetrofit(
        @Auth okHttpClient: OkHttpClient,
        buildPropertyRepository: BuildPropertyRepository,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_URL))
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    @Storage
    fun providesStorageRetrofit(
        buildPropertyRepository: BuildPropertyRepository,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(buildPropertyRepository.get(BuildProperty.FIREBASE_STORAGE_URL))
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    @Weather
    fun providesWeatherRetrofit(
        @Weather okHttpClient: OkHttpClient,
        buildPropertyRepository: BuildPropertyRepository,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(buildPropertyRepository.get(BuildProperty.WEATHER_DATABASE_URL))
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    @Map
    fun providesMapRetrofit(
        @Map okHttpClient: OkHttpClient,
        buildPropertyRepository: BuildPropertyRepository,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(buildPropertyRepository.get(BuildProperty.NAVER_MAP_BASE_URL))
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    @FishingSpot
    fun providesFishingSpotRetrofit(
        @Map okHttpClient: OkHttpClient,
        buildPropertyRepository: BuildPropertyRepository,
        converterFactory: Converter.Factory,
        ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_URL))
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @Auth
    fun providesAuthHttpClient(
        buildPropertyRepository: BuildPropertyRepository,
    ): OkHttpClient {
        val client =
            OkHttpClient.Builder()
                .readTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
        val clientWithAuthInterceptor =
            client
                .addInterceptor(
                    interceptor =
                        AuthInterceptor(
                            buildPropertyRepository = buildPropertyRepository,
                        ),
                )
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    },
                )
        return clientWithAuthInterceptor.build()
    }

    @Provides
    @Singleton
    @Weather
    fun providesWeatherHttpClient(
        buildPropertyRepository: BuildPropertyRepository,
    ): OkHttpClient {
        val client =
            OkHttpClient.Builder()
                .readTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
        val clientWithAuthInterceptor =
            client
                .addInterceptor(
                    interceptor =
                        WeatherInterceptor(
                            buildPropertyRepository = buildPropertyRepository,
                        ),
                )
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    },
                )
        return clientWithAuthInterceptor.build()
    }

    @Provides
    @Singleton
    @Map
    fun providesMapHttpClient(
        buildPropertyRepository: BuildPropertyRepository,
    ): OkHttpClient {
        val client =
            OkHttpClient.Builder()
                .readTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
        val clientWithAuthInterceptor =
            client
                .addInterceptor(
                    interceptor =
                        MapInterceptor(
                            buildPropertyRepository = buildPropertyRepository,
                        ),
                )
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    },
                )
        return clientWithAuthInterceptor.build()
    }

    @Provides
    @Singleton
    fun providerConverterFactory(
        json: Json,
    ): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    companion object {
        private val TIME_OUT_COUNT: Long = 10
    }
}
