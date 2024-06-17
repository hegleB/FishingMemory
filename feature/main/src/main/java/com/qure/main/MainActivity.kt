package com.qure.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qure.designsystem.theme.FishingMemoryTheme
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_BASE_URL
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_CONTENT
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_CREATE_TIME
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_FISH_SIZE
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_FISH_TYPE
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_IMAGE_PATH
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_LOCATION
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_TITLE
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_WATER_TYPE
import com.qure.memo.share.KakaoLinkSender.Companion.SLASH
import com.qure.model.darkmode.DarkModeConfig
import com.qure.model.extensions.Empty
import com.qure.ui.model.MemoUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var uiState: MainUiState by mutableStateOf(MainUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userData
                    .onEach { uiState = it }
                    .collect {}
            }
        }

        setContent {

            val isDarkTheme = when (uiState) {
                MainUiState.Loading -> isSystemInDarkTheme()
                is MainUiState.Success -> when ((uiState as MainUiState.Success).userData.darkModeConfig) {
                    DarkModeConfig.DARK -> true
                    DarkModeConfig.LIGHT -> false
                    DarkModeConfig.SYSTEM -> isSystemInDarkTheme()
                }
            }
            val route by viewModel.currentRoute.collectAsStateWithLifecycle()

            val navigator: MainNavigator = rememberMainNavigator()
            FishingMemoryTheme(isDarkTheme) {
                if (intent.action == Intent.ACTION_VIEW) {
                    val uri = intent.data ?: Uri.EMPTY
                    if (uri != null) {
                        val memo = getMemoForKakaoShare(uri)
                        viewModel.setKakaoDeepLink(true)
                        MainScreen(
                            navigator = navigator,
                            memo = memo,
                            route = MainTab.HOME.route,
                        )
                        return@FishingMemoryTheme
                    }
                }

                MainScreen(
                    navigator = navigator,
                    setRoute = viewModel::setRoute,
                    route = route,
                )

            }
        }
    }

    private fun getMemoForKakaoShare(uri: Uri): MemoUI {
        return MemoUI(
            title = uri.getQueryParameter(QUERY_TITLE) ?: String.Empty,
            waterType = uri.getQueryParameter(QUERY_WATER_TYPE) ?: String.Empty,
            fishSize = uri.getQueryParameter(QUERY_FISH_SIZE) ?: String.Empty,
            fishType = uri.getQueryParameter(QUERY_FISH_TYPE) ?: String.Empty,
            date = uri.getQueryParameter(QUERY_CREATE_TIME) ?: String.Empty,
            location = uri.getQueryParameter(QUERY_LOCATION) ?: String.Empty,
            content = uri.getQueryParameter(QUERY_CONTENT) ?: String.Empty,
            image =
            uri.getQueryParameter(QUERY_BASE_URL) + SLASH +
                    uri.getQueryParameter(QUERY_IMAGE_PATH),
        )
    }
}
