package com.qure.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.qure.core.BaseActivity
import com.qure.onboarding.databinding.ActivityOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewPager()
        initEvent()
    }

    private fun initViewPager() {
        binding.apply {
            viewpagerActivityOnboardingOnboarding.adapter =
                ScreenSlidePagerAdapter(this@OnboardingActivity)
            viewpagerActivityOnboardingOnboarding.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewpagerActivityOnboardingOnboarding.isUserInputEnabled = false
        }

        TabLayoutMediator(
            binding.tabLayoutActivityOnboardingDot,
            binding.viewpagerActivityOnboardingOnboarding
        ) { tab, position -> }.attach()
    }

    private fun initEvent() {
        setViewPagePosition()
        setButtonTextToPageTransition()
    }

    private fun setViewPagePosition() {
        binding.apply {
            buttonActivityOnboardingNext.setOnClickListener {
                viewpagerActivityOnboardingOnboarding.run {
                    currentItem += PAGE_INCREMENT_VALUE
                }
            }

        }
    }

    private fun setButtonTextToPageTransition() {
        binding.apply {
            viewpagerActivityOnboardingOnboarding.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> buttonActivityOnboardingNext.text = getString(R.string.onboarding_next)
                        1 -> buttonActivityOnboardingNext.text = getString(R.string.onboarding_next)
                        else -> buttonActivityOnboardingNext.text =
                            getString(R.string.onboarding_start)
                    }
                }
            })
        }
    }

    private inner class ScreenSlidePagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = PAGES_NUMBER

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    OnboardingFragment.newInstance(
                        title = getString(R.string.onboarding_first_title),
                        onboardingLottieResId = R.raw.first_page
                    )
                }
                1 -> {
                    OnboardingFragment.newInstance(
                        title = getString(R.string.onboarding_second_title),
                        onboardingLottieResId = R.raw.second_page
                    )
                }
                else -> {
                    OnboardingFragment.newInstance(
                        title = getString(R.string.onboarding_third_title),
                        onboardingLottieResId = R.raw.third_page
                    )
                }
            }
        }

    }

    companion object {
        private const val PAGES_NUMBER = 3
        private const val PAGE_INCREMENT_VALUE = 1
    }
}

