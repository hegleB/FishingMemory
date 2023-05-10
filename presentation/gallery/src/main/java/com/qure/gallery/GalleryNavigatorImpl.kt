package com.qure.gallery

import android.content.Context
import android.content.Intent
import com.qure.navigator.GalleryNavigator
import javax.inject.Inject

class GalleryNavigatorImpl @Inject constructor(): GalleryNavigator {
    override fun intent(context: Context): Intent {
        return Intent(context, GalleryActivity::class.java)
    }
}