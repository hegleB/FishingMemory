package com.qure.create

import android.content.Context
import android.content.Intent
import com.qure.navigator.MemoCreateNavigator
import javax.inject.Inject

class MemoCreateNavigatorImpl @Inject constructor() : MemoCreateNavigator {

    override fun intent(context: Context): Intent {
        return Intent(context, MemoCreateActivity::class.java)
    }

}