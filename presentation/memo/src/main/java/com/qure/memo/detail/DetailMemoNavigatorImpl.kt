package com.qure.memo.detail

import android.content.Context
import android.content.Intent
import com.qure.navigator.DetailMemoNavigator
import javax.inject.Inject

class DetailMemoNavigatorImpl @Inject constructor(): DetailMemoNavigator {
    override fun intent(context: Context): Intent {
        return Intent(context, DetailMemoActivity::class.java)
    }
}