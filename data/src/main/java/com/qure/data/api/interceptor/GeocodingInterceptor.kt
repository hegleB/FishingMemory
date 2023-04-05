package com.qure.data.api.interceptor

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class GeocodingInterceptor @Inject constructor(
    private val buildPropertyRepository: BuildPropertyRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.apply {
            addHeader("X-NCP-APIGW-API-KEY-ID", buildPropertyRepository.get(BuildProperty.NAVER_MAP_API_CLIENT_ID))
            addHeader("X-NCP-APIGW-API-KEY", buildPropertyRepository.get(BuildProperty.NAVER_MAP_API_CLIENT_SECRET))
        }
        return chain.proceed(request.build())
    }

}