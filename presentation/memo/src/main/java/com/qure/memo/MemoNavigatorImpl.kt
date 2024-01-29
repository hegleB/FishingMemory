package com.qure.memo

import android.content.Context
import android.content.Intent
import com.qure.navigator.MemoNavigator
import javax.inject.Inject

class MemoNavigatorImpl
    @Inject
    constructor() : MemoNavigator {
        override fun intent(context: Context): Intent {
            return Intent(context, MemoListActivity::class.java)
        }
    }
