package com.qure.fishingmemory

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class Application : Application() {
    @Inject
    lateinit var buildPropertyRepository: BuildPropertyRepository

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKakaoSdk()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(
            context = this,
            appKey = buildPropertyRepository.get(BuildProperty.KAKAO_API_KEY),
        )
    }
    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}