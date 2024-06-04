import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.library")
    id("fishingmemory.android.hilt")
}

android {
    setNamespace("build_property")
}