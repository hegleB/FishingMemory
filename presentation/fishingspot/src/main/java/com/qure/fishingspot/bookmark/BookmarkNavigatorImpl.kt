package com.qure.fishingspot.bookmark

import android.content.Context
import android.content.Intent
import com.qure.navigator.BookmarkNavigator
import javax.inject.Inject

class BookmarkNavigatorImpl @Inject constructor() : BookmarkNavigator {
    override fun intent(context: Context): Intent {
        return Intent(context, BookmarkActivity::class.java)
    }
}