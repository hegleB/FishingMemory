package com.qure.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.qure.core.BaseActivity
import com.qure.domain.MEMO_DATA
import com.qure.history.HistoryFragment
import com.qure.home.databinding.ActivityMainBinding
import com.qure.home.home.HomeFragment
import com.qure.memo.detail.DetailMemoActivity
import com.qure.mypage.MyPageFragment
import com.qure.navigator.DetailMemoNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    @Inject
    lateinit var detaiMemoNavigator: DetailMemoNavigator

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data ?: Uri.EMPTY
            if (uri != null) {
                val memo = DetailMemoActivity().createMemoUI(uri)
                val detailMemoIntent =
                    detaiMemoNavigator.intent(this).apply {
                        putExtra(MEMO_DATA, memo)
                    }
                startActivity(detailMemoIntent)
            }
        }
        initNavigationBar()
        observe()
    }

    private fun observe() {
        lifecycleScope.launchWhenCreated {
            viewModel.fragment.collect {
                binding.bottomNavigationBarActivityMain.selectedItemId = it
            }
        }
    }

    private fun initNavigationBar() {
        changeFragment(HomeFragment())

        binding.bottomNavigationBarActivityMain.run {
            setOnItemSelectedListener {
                viewModel.setCurrentFragment(it.itemId)
                when (it.itemId) {
                    R.id.homeFragment -> {
                        changeFragment(HomeFragment())
                    }
                    R.id.historyFragment -> {
                        changeFragment(HistoryFragment())
                    }
                    R.id.mypageFragment -> {
                        changeFragment(MyPageFragment())
                    }
                }
                true
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout_activityMain, fragment)
            .commit()
    }
}
