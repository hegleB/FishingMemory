package com.qure.onboarding

import android.os.Bundle
import android.view.View
import com.qure.core.BaseFragment
import com.qure.onboarding.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(R.layout.fragment_onboarding) {

    private var title: String? = null
    private var onboardingLottieResId: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)
            onboardingLottieResId = it.getInt(ARG_PARAM2)
        }
        setOnboarding()
    }

    private fun setOnboarding() {
        binding.apply {
            textView.text = title
            lottieAnimationViewFragmentOnboardingImage.apply {
                onboardingLottieResId?.let {
                    setAnimation(it)
                }
                playAnimation()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, onboardingLottieResId: Int) =
            OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putInt(ARG_PARAM2, onboardingLottieResId)
                }
            }
    }
}