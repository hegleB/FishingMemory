package com.qure.data.api.interceptor

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val buildPropertyRepository: BuildPropertyRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        request.url(
            originalHttpUrl.newBuilder()
                .addQueryParameter("key", "${buildPropertyRepository.get(BuildProperty.FIREBASE_API_KEY)}")
                .build()
        )
        return chain.proceed(request.build())
    }
}