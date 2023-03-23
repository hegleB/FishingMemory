package com.qure.data.api.interceptor

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.domain.entity.weather.Body
import com.qure.domain.entity.weather.Item
import com.qure.domain.entity.weather.Items
import com.qure.domain.entity.weather.Weather
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import javax.inject.Inject

class WeatherInterceptor @Inject constructor(
    private val gson: Gson,
    private val buildPropertyRepository: BuildPropertyRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalHttpUrl = chain.request().url
        val request = chain.request().newBuilder().url(
            originalHttpUrl.newBuilder()
                .addQueryParameter(
                    "serviceKey",
                    "${buildPropertyRepository.get(BuildProperty.WEATHER_API_KEY)}"
                )
                .addQueryParameter("numOfRows", "1000")
                .addQueryParameter("pageNo", "1")
                .addQueryParameter("dataType", "JSON",)
                .build()
        )
        return chain.proceed(request.build())
    }
}