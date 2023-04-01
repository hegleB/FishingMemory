package com.qure.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qure.core.BaseActivity
import com.qure.create.databinding.ActivityMemoCreateBinding
import com.qure.history.MemoCalendarDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoCreateActivity : BaseActivity<ActivityMemoCreateBinding>(R.layout.activity_memo_create), MemoCalendarDialogFragment.DatePickerListener {

    lateinit var listener: MemoCalendarDialogFragment.DatePickerListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this
        setDate()
    }

    private fun setDate() {
        binding.textViewActivityMemoCreateDate.setOnClickListener {
            MemoCalendarDialogFragment(listener).show(supportFragmentManager, MemoCalendarDialogFragment.TAG)
        }
    }

    override fun selectDate(date: String) {
        binding.textViewActivityMemoCreateDate.text = date
    }
}