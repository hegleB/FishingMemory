package com.qure.fishingspot

import android.content.Context
import android.content.Intent
import com.qure.navigator.FishingSpotNavigator
import javax.inject.Inject

class FishingSpotNavigatorImpl
    @Inject
    constructor() : FishingSpotNavigator {
        override fun intent(context: Context): Intent {
            return Intent(context, FishingSpotActivity::class.java)
        }
    }
