package com.qure.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.qure.core.BaseActivity
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.login.databinding.ActivityLoginBinding
import com.qure.login.extension.loginWithKakaoOrThrow
import com.qure.navigator.HomeNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    @Inject
    lateinit var homeNavigator: HomeNavigator

    protected val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKakaoLogin()
        observe()
    }

    private fun startKakaoLogin() {
        val context = this
        binding.linearLayoutActivityLoginKakao.setOnSingleClickListener {
            lifecycleScope.launch {
                kotlin.runCatching {
                    UserApiClient.loginWithKakaoOrThrow(context)
                }.onSuccess { oAuthToken ->
                    UserApiClient.instance.me { user, _ ->
                        if (user != null) {
                            user.kakaoAccount?.email?.let { email ->
                                viewModel.createUser(
                                    email = email,
                                    accessToken = oAuthToken.accessToken
                                )
                            }
                        }
                    }
                }.onFailure { throwable ->
                    if (throwable is ClientError && throwable.reason == ClientErrorCause.Cancelled) {
                        FishingMemoryToast().error(
                            this@LoginActivity,
                            getString(R.string.message_kakao_cancellation_requested_User)
                        )
                    } else {
                        FishingMemoryToast().error(
                            this@LoginActivity,
                            getString(R.string.message_kakao_login_failure)
                        )
                    }
                }
            }
        }
    }

    private fun observe() {
        viewModel.error
            .onEach { errorMessage ->
                FishingMemoryToast().error(
                    this,
                    errorMessage,
                )
            }.launchIn(lifecycleScope)

        viewModel.action.onEach { action ->
            val intent = when (action) {
                LoginViewModel.Action.AlreadySignUp -> {
                    homeNavigator.intent(this)
                }
                LoginViewModel.Action.FirstSignUp -> {
                    homeNavigator.intent(this)
                }
            }
            startActivity(intent)
            finish()
        }.launchIn(lifecycleScope)
    }
}