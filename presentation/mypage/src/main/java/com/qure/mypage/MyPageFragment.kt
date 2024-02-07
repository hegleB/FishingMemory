package com.qure.mypage

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.kakao.sdk.user.UserApiClient
import com.qure.core.BaseComposeFragment
import com.qure.core.util.FishingMemoryToast
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.domain.WEB_URL
import com.qure.domain.repository.AuthRepository
import com.qure.mypage.darkmode.DarkModeActivity
import com.qure.navigator.BookmarkNavigator
import com.qure.navigator.LoginNavigator
import com.qure.navigator.ProgramInformationNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyPageFragment : BaseComposeFragment() {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var loginNavigator: LoginNavigator

    @Inject
    lateinit var programInformationNavigator: ProgramInformationNavigator

    @Inject
    lateinit var bookmarkNavigator: BookmarkNavigator

    private val viewModel by viewModels<MyPageViewModel>()

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            MyPageScreen(
                viewModel = viewModel,
                email = authRepository.getEmailFromLocal(),
                navigateToBookmark = { onBookmarkButtonClicked() },
                navigateToLogin = { startActivity(loginNavigator.intent(requireContext())); activity?.finish() },
                onClickLogout = { onLogoutButtonClicked() },
                onClickWithdrawService = { withdrawService() },
                navigateToPolicyPrivacy = { onPolicyPrivacyButtonClicked() },
                navigateToPolicyService = { onPolicyServiceButtonClicked() },
                navigateToOpenSourceLicense = { onOpenSourceLicenseButtonClicked() },
                navigateToDarkMode = {
                    startActivity(
                        Intent(requireContext(), DarkModeActivity::class.java),
                    )
                },
            )
        }
    }

    private fun onPolicyServiceButtonClicked() {
        val intent = programInformationNavigator.intent(requireContext())
        intent.putExtra(WEB_URL, POLICY_SERVICE)
        startActivity(intent)
    }

    private fun onPolicyPrivacyButtonClicked() {
        val intent = programInformationNavigator.intent(requireContext())
        intent.putExtra(WEB_URL, POLICY_PRIVACY)
        startActivity(intent)
    }

    private fun onOpenSourceLicenseButtonClicked() {
        val intent = programInformationNavigator.intent(requireContext())
        intent.putExtra(WEB_URL, OPENSOURCE_LICENSE)
        startActivity(intent)
    }

    private fun onBookmarkButtonClicked() {
        val intent = bookmarkNavigator.intent(requireContext())
        startActivity(intent)
    }

    private fun withdrawService() {
        UserApiClient.instance.unlink { throwable ->
            if (throwable != null) {
                FishingMemoryToast().error(requireContext(), throwable.message)
            } else {
                viewModel.withdrawService()
            }
        }
    }

    private fun onLogoutButtonClicked() {
        viewModel.logoutUser()
    }

    companion object {
        private const val POLICY_SERVICE =
            "https://sites.google.com/view/fishingmemory-policyservice/%ED%99%88"
        private const val POLICY_PRIVACY =
            "https://sites.google.com/view/fishingmemory-privacypolicy/%ED%99%88"
        private const val OPENSOURCE_LICENSE =
            "https://sites.google.com/view/fishingmemory-license/%ED%99%88"
    }
}
