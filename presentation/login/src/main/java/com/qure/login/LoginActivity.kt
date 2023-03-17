package com.qure.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.qure.core.BaseActivity
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
        binding.linearLayoutActivityLoginKakao.setOnClickListener {
            lifecycleScope.launch {
                kotlin.runCatching {
                    UserApiClient.loginWithKakaoOrThrow(context)
                }.onSuccess { oAuthToken ->
                    UserApiClient.instance.me { user, _ ->
                        if (user != null) {
                            user.kakaoAccount?.email?.let { email ->
                                viewModel.createUser(
                                    email = email,
                                    userId = user.id.toString()
                                )
                            }
                        }
                    }
                }.onFailure { throwable ->
                    if (throwable is ClientError && throwable.reason == ClientErrorCause.Cancelled) {
                        Timber.d("사용자가 명시적으로 카카오 취소")
                    } else {
                        Timber.d("로그인 실패 : ${throwable.message}")
                    }
                }
            }
        }
    }

    private fun observe() {
        viewModel.action.onEach { action ->
            val intent = when(action) {
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