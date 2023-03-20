package com.qure.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.qure.core.BaseActivity
import com.qure.home.home.HomeFragment
import com.qure.home.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout_activityMain, fragment).commit()
    }
}