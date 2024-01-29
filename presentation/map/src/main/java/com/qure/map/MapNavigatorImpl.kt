package com.qure.map

import android.content.Context
import android.content.Intent
import com.qure.navigator.MapNavigator
import javax.inject.Inject

class MapNavigatorImpl
    @Inject
    constructor() : MapNavigator {
        override fun intent(context: Context): Intent {
            return Intent(context, MapActivity::class.java)
        }
    }
