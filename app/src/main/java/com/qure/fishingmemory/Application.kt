package com.qure.fishingmemory

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.domain.DARK_MODE_KEY
import com.qure.domain.THEME_DARK
import com.qure.domain.THEME_LIGHT
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class Application : Application() {
    @Inject
    lateinit var buildPropertyRepository: BuildPropertyRepository

    @Inject
    lateinit var fishMemorySharedPreference: FishMemorySharedPreference

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKakaoSdk()
        initNaverMapSdk()
        initDarkMode()
    }

    private fun initDarkMode() {
        val isDarkMode = when(fishMemorySharedPreference.getTheme(DARK_MODE_KEY)) {
            THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(isDarkMode)

    }

    private fun initNaverMapSdk() {
        NaverMapSdk.getInstance(this).setClient(
            NaverMapSdk.NaverCloudPlatformClient(buildPropertyRepository.get(BuildProperty.NAVER_MAP_API_CLIENT_ID))
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