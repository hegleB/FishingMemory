package com.qure.login

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.sdk.user.UserApiClient
import com.qure.designsystem.component.FMGlideImage
import com.qure.designsystem.component.FMLoginButton
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.theme.Yellow100
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.login.R
import com.qure.login.extension.loginWithKakaoOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val error = viewModel.error
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(error) {
        error.collectLatest(onShowErrorSnackBar)
    }

    LoginScreen(
        loginUiState = loginUiState,
        onClickKakaoLogin = viewModel::onClickedKakaoLogin,
        navigateToHome = navigateToHome,
        navigateToKakoLauncher = { launchKakaLogin(coroutineScope, context, viewModel) },
    )
}

@Composable
private fun LoginScreen(
    loginUiState: LoginUiState = LoginUiState.Initial,
    modifier: Modifier = Modifier,
    onClickKakaoLogin: () -> Unit = { },
    navigateToHome: () -> Unit = { },
    navigateToKakoLauncher: @Composable () -> Unit = { },
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        FMGlideImage(
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
                text = stringResource(id = R.string.app_name),
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun launchKakaLogin(
    coroutineScope: CoroutineScope,
    context: Context,
    viewModel: LoginViewModel,
) {
    coroutineScope.launch {
        runCatching {
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
            viewModel.sendErrorMessage(throwable)
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() = FMPreview {
    LoginScreen(onClickKakaoLogin = { })
}
