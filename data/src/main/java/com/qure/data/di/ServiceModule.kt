package com.qure.data.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.Auth
import com.qure.data.api.AuthService
import com.qure.data.api.deserializer.LocalDateDeserializer
import com.qure.data.api.deserializer.LocalDateTimeDeserializer
import com.qure.data.api.deserializer.LocalTimeDeserializer
import com.qure.data.api.exception.ResultCallAdapterFactory
import com.qure.data.api.interceptor.AuthInterceptor
import com.qure.data.api.serializer.LocalDateSerializer
import com.qure.data.api.serializer.LocalDateTimeSerializer
import com.qure.data.api.serializer.LocalTimeSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun providesAuthService(
        @Auth retrofit: Retrofit,
    ): AuthService = retrofit.create()

    @Singleton
    @Provides
    @Auth
    fun providesAuthRetrofit(
        okHttpClient: OkHttpClient,
        buildPropertyRepository: BuildPropertyRepository,
        resultCallAdapterFactory: ResultCallAdapterFactory
    ): Retrofit {
        val gsonWithAdapter: Gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeDeserializer())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
            .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeSerializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_URL))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonWithAdapter))
            .addCallAdapterFactory(resultCallAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun providesResultCallAdapterFactory(): ResultCallAdapterFactory = ResultCallAdapterFactory()

    @Provides
    @Singleton
    fun providesHttpClient(
        @ApplicationContext context: Context,
        gson: Gson,
        buildPropertyRepository: BuildPropertyRepository
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
            .readTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
        val clientWithAuthInterceptor = client
            .addInterceptor(
                interceptor = AuthInterceptor(
                    buildPropertyRepository = buildPropertyRepository,
                ),
            )
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
        return clientWithAuthInterceptor.build()
    }

    @Provides
    @Singleton
    fun providesConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    companion object {
        private val TIME_OUT_COUNT : Long = 10
    }
}