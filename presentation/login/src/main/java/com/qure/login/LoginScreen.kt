package com.qure.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.qure.core.util.FishingMemoryToast
import com.qure.core_design.compose.components.FMLoginButton
import com.qure.core_design.compose.theme.Yellow100
import com.qure.core_design.compose.utils.FMPreview
import com.qure.login.extension.loginWithKakaoOrThrow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    navigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val action = viewModel.action
    val error = viewModel.error

    LaunchedEffect(action) {
        action.collectLatest { loginAction ->
            when (loginAction) {
                LoginViewModel.Action.AlreadySignUp -> navigateToHome()
                LoginViewModel.Action.FirstSignUp -> navigateToHome()
                LoginViewModel.Action.LauchKakaoLogin -> {
                    kotlin.runCatching {
                        UserApiClient.loginWithKakaoOrThrow(context)
                    }.onSuccess { oAuthToken ->
                        UserApiClient.instance.me { user, _ ->
                            if (user != null) {
                                user.kakaoAccount?.email?.let { email ->
                                    viewModel.createUser(
                                        email = email,
                                        accessToken = oAuthToken.accessToken,
                                    )
                                }
                            }
                        }
                    }.onFailure { throwable ->
                        if (throwable is ClientError && throwable.reason == ClientErrorCause.Cancelled) {
                            FishingMemoryToast().error(
                                context = context,
                                title = context.getString(R.string.message_kakao_cancellation_requested_User),
                            )
                        } else {
                            FishingMemoryToast().error(
                                context = context,
                                title = context.getString(R.string.message_kakao_login_failure),
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(error) {
        error.collectLatest { errorMessage ->
            FishingMemoryToast().error(
                context,
                errorMessage,
            )
        }
    }

    LoginContent(
        modifier = Modifier.fillMaxSize(),
        onKakaoLoginButtonClick = { viewModel.onClickedKakaoLogin() },
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    onClickKakaoLogin: () -> Unit = { },
) {
    Box(modifier = modifier) {
        GlideImage(
            modifier = Modifier.fillMaxSize(),
            model = R.drawable.img_bg_login,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        when (loginUiState) {
            LoginUiState.Initial -> Unit
            LoginUiState.Loading -> {
                FMProgressBar(
                    modifier = Modifier
                        .align(Alignment.Center),
                )
            }

            LoginUiState.LaunchLogin -> navigateToKakoLauncher()
            LoginUiState.Success -> navigateToHome()
        }

        Column {
            Spacer(modifier = Modifier.heightIn(200.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = com.qure.core_design.R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 40.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        FMLoginButton(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(90.dp)
                .align(Alignment.BottomCenter)
                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
            text = stringResource(id = R.string.kakao_login),
            onClick = { onClickKakaoLogin() },
            buttonColor = Yellow100,
            shape = RoundedCornerShape(15.dp),
            textStyle = MaterialTheme.typography.displayMedium,
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() = FMPreview {
    LoginScreen(onClickKakaoLogin = { })
}
