import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.library")
}

android {
    setNamespace("core.testing")
}

dependencies {
    compileOnly(libs.junit4)
    compileOnly(libs.junit.vintage.engine)
    compileOnly(libs.kotlin.test)
    compileOnly(libs.mockk)
    compileOnly(libs.turbine)
    compileOnly(libs.coroutines.test)
}