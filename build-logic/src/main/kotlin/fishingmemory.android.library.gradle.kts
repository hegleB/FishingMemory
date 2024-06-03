import com.fishingmemory.app.configureCoroutineAndroid
import com.fishingmemory.app.configureHiltAndroid
import com.fishingmemory.app.configureKotest
import com.fishingmemory.app.configureKotestAndroid
import com.fishingmemory.app.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()
configureKotest()
configureKotestAndroid()