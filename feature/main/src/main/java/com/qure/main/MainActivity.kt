package com.qure.main

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
import com.qure.data.utils.NetworkMonitor
import com.qure.designsystem.theme.FishingMemoryTheme
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_BASE_URL
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_CONTENT
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_COORDS
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_CREATE_TIME
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_DATE
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_EMAIL
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_FISH_SIZE
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_FISH_TYPE
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_IMAGE_PATH
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_LOCATION
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_TITLE
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_UUID
import com.qure.memo.share.KakaoLinkSender.Companion.QUERY_WATER_TYPE
import com.qure.memo.share.KakaoLinkSender.Companion.SLASH
import com.qure.model.darkmode.DarkModeConfig
import com.qure.model.extensions.Empty
import com.qure.ui.model.MemoUI
import dagger.hilt.android.AndroidEntryPoint
import io.branch.referral.Branch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

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
            val isConnectNetwork by networkMonitor.isConnectNetwork.collectAsStateWithLifecycle(true)

            val navigator: MainNavigator = rememberMainNavigator()
            FishingMemoryTheme(isDarkTheme) {
                val memo by viewModel.memo.collectAsStateWithLifecycle()
                intent.putExtra("branch_force_new_session", true)
                val uri = intent.data ?: Uri.EMPTY
                val deepLinkType = DeepLinkType.fromUri(uri.toString())

                if (memo.isValidMemo.not() && deepLinkType is DeepLinkType.Branch) {
                    handleBranchDeepLink(
                        uri = uri,
                        setMemo = viewModel::setMemo,
                        updateBranchForceNewSession = {
                            intent.putExtra("branch_force_new_session", false)
                        },
                    )
                }
                when(deepLinkType) {
                    is DeepLinkType.Branch -> {
                        MainScreen(
                            navigator = navigator,
                            memo = memo,
                            isConnectNetwork = isConnectNetwork,
                            deepLinkType = deepLinkType,
                        )
                    }

                    is DeepLinkType.Kakao -> {
                        val kakaoMemo = getMemoForKakaoShare(uri)
                        viewModel.setKakaoDeepLink(true)
                        MainScreen(
                            navigator = navigator,
                            memo = kakaoMemo,
                            isConnectNetwork = isConnectNetwork,
                            deepLinkType = deepLinkType,
                        )
                    }
                    is DeepLinkType.None -> {
                        MainScreen(
                            navigator = navigator,
                            isConnectNetwork = isConnectNetwork,
                            deepLinkType = deepLinkType,
                        )
                    }
                }
            }
        }
    }

    private fun getMemoForKakaoShare(uri: Uri): MemoUI {
        return MemoUI(
            title = uri.getQueryParameter(QUERY_TITLE) ?: String.Empty,
            waterType = uri.getQueryParameter(QUERY_WATER_TYPE) ?: String.Empty,
            fishSize = uri.getQueryParameter(QUERY_FISH_SIZE) ?: String.Empty,
            fishType = uri.getQueryParameter(QUERY_FISH_TYPE) ?: String.Empty,
            location = uri.getQueryParameter(QUERY_LOCATION) ?: String.Empty,
            content = uri.getQueryParameter(QUERY_CONTENT) ?: String.Empty,
            image =
            uri.getQueryParameter(QUERY_BASE_URL) + SLASH +
                    uri.getQueryParameter(QUERY_IMAGE_PATH),
            uuid = uri.getQueryParameter(QUERY_UUID) ?: String.Empty,
            createTime = uri.getQueryParameter(QUERY_CREATE_TIME) ?: String.Empty,
            date = uri.getQueryParameter(QUERY_DATE) ?: String.Empty,
            email = uri.getQueryParameter(QUERY_EMAIL) ?: String.Empty,
            coords = uri.getQueryParameter(QUERY_COORDS) ?: String.Empty,
        )
    }

    private fun getMemoForBranchDeepLink(jsonObject: JSONObject): MemoUI {
        return MemoUI(
            title = jsonObject.optString(QUERY_TITLE) ?: String.Empty,
            waterType = jsonObject.optString(QUERY_WATER_TYPE) ?: String.Empty,
            fishSize = jsonObject.optString(QUERY_FISH_SIZE) ?: String.Empty,
            fishType = jsonObject.optString(QUERY_FISH_TYPE) ?: String.Empty,
            location = jsonObject.optString(QUERY_LOCATION) ?: String.Empty,
            content = jsonObject.optString(QUERY_CONTENT) ?: String.Empty,
            image =
            jsonObject.optString(QUERY_BASE_URL) + SLASH +
                    jsonObject.optString(QUERY_IMAGE_PATH),
            uuid = jsonObject.optString(QUERY_UUID),
            createTime = jsonObject.optString(QUERY_CREATE_TIME),
            date = jsonObject.optString(QUERY_DATE),
            email = jsonObject.optString(QUERY_EMAIL),
            coords = jsonObject.optString(QUERY_COORDS),
        )
    }

    private fun handleBranchDeepLink(
        uri: Uri,
        setMemo: (MemoUI) -> Unit,
        updateBranchForceNewSession: () -> Unit,
    ) {
        Branch.sessionBuilder(this)
            .withCallback { referringParams, error ->
                if (error == null && referringParams != null) {
                    setMemo(getMemoForBranchDeepLink(referringParams))
                }
                updateBranchForceNewSession()
            }
            .withData(uri)
            .init()
    }
}
