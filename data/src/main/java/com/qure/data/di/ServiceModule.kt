package com.qure.data.di

import android.content.Context
import com.google.gson.Gson
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.AuthService
import com.qure.data.api.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun providesAuthService(
        retrofit: Retrofit,
    ): AuthService = retrofit.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        buildPropertyRepository: BuildPropertyRepository
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(buildPropertyRepository.get(BuildProperty.FIREBASE_AUTH_URL))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

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
                    context = context,
                    client = client.build(),
                    gson = gson,
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