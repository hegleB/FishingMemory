package com.qure.fishingmemory

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import dagger.hilt.android.HiltAndroidApp
import io.branch.referral.Branch
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
        initNaverMapSdk()
        Branch.expectDelayedSessionInitialization(true)
        Branch.getAutoInstance(this)
    }


    private fun initNaverMapSdk() {
        NaverMapSdk.getInstance(this).setClient(
            NaverMapSdk.NaverCloudPlatformClient(buildPropertyRepository.get(BuildProperty.NAVER_MAP_API_CLIENT_ID)),
        )
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
