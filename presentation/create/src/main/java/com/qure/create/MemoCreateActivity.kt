package com.qure.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qure.core.BaseActivity
import com.qure.create.databinding.ActivityMemoCreateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoCreateActivity : BaseActivity<ActivityMemoCreateBinding>(R.layout.activity_memo_create) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_create)
    }
}