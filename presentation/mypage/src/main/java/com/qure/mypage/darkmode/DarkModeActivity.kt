package com.qure.mypage.darkmode

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qure.core.BaseActivity
import com.qure.core.util.setOnSingleClickListener
import com.qure.domain.THEME_DARK
import com.qure.domain.THEME_LIGHT
import com.qure.domain.THEME_SYSTEM
import com.qure.mypage.R
import com.qure.mypage.databinding.ActivityDarkModeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DarkModeActivity : BaseActivity<ActivityDarkModeBinding>(R.layout.activity_dark_mode) {

    private val viewModel by viewModels<DarkModeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDarkMode()
        observe()
        initView()
    }

    private fun initView() {

        binding.imageViewActivityDarkModeBack.setOnSingleClickListener {
            finish()
        }

        binding.radioGroupActivityDarkMode.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_activityDarMode_darkMode -> {
                    viewModel.setDarkMode(THEME_DARK)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                R.id.radioButton_activityDarMode_lightMode -> {
                    viewModel.setDarkMode(THEME_LIGHT)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                else -> {
                    viewModel.setDarkMode(THEME_SYSTEM)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentThemeMode.collect {
                    when (it) {
                        THEME_DARK -> binding.radioButtonActivityDarModeDarkMode.isChecked = true
                        THEME_LIGHT -> binding.radioButtonActivityDarModeLightMode.isChecked = true
                        else -> binding.radioButtonActivityDarModeSystemMode.isChecked = true
                    }

                }
            }
        }
    }
}