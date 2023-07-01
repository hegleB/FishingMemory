package com.qure.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.qure.core.BaseActivity
import com.qure.core.util.setOnSingleClickListener
import com.qure.navigator.LoginNavigator
import com.qure.navigator.PermissionNavigator
import com.qure.onboarding.databinding.ActivityOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    @Inject
    lateinit var permissionNavigator: PermissionNavigator

    private val viewModel by viewModels<OnboardingViewModel>()
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
            buttonActivityOnboardingNext.setOnSingleClickListener {
                viewpagerActivityOnboardingOnboarding.run {
                    if (currentItem == END_PAGE) {
                        viewModel.writeFirstVisitor()
                        startActivity(permissionNavigator.intent(this@OnboardingActivity))
                        finish()
                    } else {
                        currentItem += PAGE_INCREMENT_VALUE
                    }
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
                        OnboardingPage.RECORD_PAGE.position -> buttonActivityOnboardingNext.text = getString(R.string.onboarding_next)
                        OnboardingPage.STATISTICS_PAGE.position -> buttonActivityOnboardingNext.text = getString(R.string.onboarding_next)
                        else -> {
                            buttonActivityOnboardingNext.text =
                                getString(R.string.onboarding_start)
                        }
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
                OnboardingPage.RECORD_PAGE.position -> {
                    OnboardingFragment.newInstance(
                        title = getString(R.string.onboarding_first_title),
                        onboardingLottieResId = R.raw.first_page
                    )
                }

                OnboardingPage.STATISTICS_PAGE.position -> {
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
        private const val END_PAGE = 2
    }
}

enum class OnboardingPage(val position: Int) {
    RECORD_PAGE(0),
    STATISTICS_PAGE(1)
}
