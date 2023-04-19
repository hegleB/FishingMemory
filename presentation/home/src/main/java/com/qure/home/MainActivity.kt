package com.qure.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.qure.core.BaseActivity
import com.qure.core.extensions.Empty
import com.qure.history.HistoryFragment
import com.qure.home.databinding.ActivityMainBinding
import com.qure.home.home.HomeFragment
import com.qure.memo.detail.DetailMemoActivity
import com.qure.memo.detail.DetailMemoActivity.Companion.MEMO_DATA
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_TITLE
import com.qure.memo.model.MemoUI
import com.qure.navigator.DetailMemoNavigator
import com.qure.navigator.MemoNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    @Inject
    lateinit var detaiMemoNavigator: DetailMemoNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data ?: Uri.EMPTY
            if (uri != null) {
                val memo = DetailMemoActivity().createMemoUI(uri)
                val detailMemoIntent = detaiMemoNavigator.intent(this).apply {
                    putExtra(MEMO_DATA, memo)
                }
                startActivity(detailMemoIntent)
            }
        }

        initNavigationBar()
    }

    private fun initNavigationBar() {
        binding.bottomNavigationBarActivityMain.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.homeFragment -> {
                        changeFragment(HomeFragment())
                    }
                    R.id.historyFragment -> {
                        changeFragment(HistoryFragment())
                    }
                    R.id.mypageFragment -> {

                    }
                }
                true
            }
            selectedItemId = R.id.homeFragment
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout_activityMain, fragment)
            .commit()
    }
}