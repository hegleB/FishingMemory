import com.fishingmemory.app.configureHiltAndroid
import com.fishingmemory.app.configureKotestAndroid
import com.fishingmemory.app.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()