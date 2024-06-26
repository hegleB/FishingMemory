package com.qure.mypage

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qure.designsystem.theme.Gray500
import com.qure.designsystem.utils.FMPreview
import com.qure.designsystem.utils.clickableWithoutRipple
import com.qure.feature.mypage.R
import kotlinx.coroutines.flow.collectLatest

data class MyPageItemData(
    @StringRes val textResId: Int,
    val hasIcon: Boolean = false,
    val hasDescription: Boolean = false,
    val description: String? = null,
    val descriptionColor: Color = Color.Unspecified,
    val onClick: (() -> Unit)? = null,
)

@Composable
fun MyPageRoute(
    padding: PaddingValues,
    navigateToBookmark: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToPolicyPrivacy: () -> Unit,
    navigateToPolicyService: () -> Unit,
    navigateToOpenSourceLicense: () -> Unit,
    navigateToDarkMode: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val context = LocalContext.current
    val pInfo = context.packageManager.getPackageInfo(context.packageName ?: "", 0)
    val versionName = pInfo?.versionName ?: ""

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    LaunchedEffect(viewModel.withdrawSucceed) {
        viewModel.withdrawSucceed.collectLatest { isWithDrawSuccess ->
            if (isWithDrawSuccess) {
                navigateToLogin()
            }
        }
    }

    LaunchedEffect(viewModel.logoutSucceed) {
        viewModel.logoutSucceed.collectLatest { isLoginSuccess ->
            if (isLoginSuccess) {
                navigateToLogin()
            }
        }
    }

    MyPageScreen(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceTint),
        email = viewModel.email,
        navigateToBookmark = navigateToBookmark,
        versionName = versionName,
        onClickWithdrawService = viewModel::withdrawService,
        navigateToPolicyPrivacy = navigateToPolicyPrivacy,
        navigateToPolicyService = navigateToPolicyService,
        navigateToOpenSourceLicense = navigateToOpenSourceLicense,
        navigateToDarkMode = navigateToDarkMode,
        onClickLogout = viewModel::logoutUser,
    )
}

@Composable
private fun MyPageScreen(
    modifier: Modifier = Modifier,
    email: String,
    navigateToBookmark: () -> Unit,
    versionName: String = "",
    onClickWithdrawService: () -> Unit,
    navigateToPolicyPrivacy: () -> Unit,
    navigateToPolicyService: () -> Unit,
    navigateToOpenSourceLicense: () -> Unit,
    navigateToDarkMode: () -> Unit,
    onClickLogout: () -> Unit,
) {
    val basicSettingsModels = listOf(
        MyPageItemData(
            R.string.mypage_kakao_email,
            hasDescription = true,
            description = email,
            descriptionColor = MaterialTheme.colorScheme.onBackground,
        ),
        MyPageItemData(
            R.string.mypage_fishingspot_bookmark,
            hasIcon = true,
            onClick = navigateToBookmark,
        ),
        MyPageItemData(
            R.string.dark_mode_setting,
            hasIcon = true,
            onClick = navigateToDarkMode,
        ),
    )

    val infoAndPolicyModels = listOf(
        MyPageItemData(
            R.string.mypage_version_info,
            hasDescription = true,
            description = versionName,
        ),
        MyPageItemData(
            R.string.mypage_terms_of_service,
            hasIcon = true,
            onClick = navigateToPolicyService,
        ),
        MyPageItemData(
            R.string.mypage_privacy_policy,
            hasIcon = true,
            onClick = navigateToPolicyPrivacy,
        ),
        MyPageItemData(
            R.string.mypage_opensource_license,
            hasIcon = true,
            onClick = navigateToOpenSourceLicense,
        ),
    )

    val accountManagementModels = listOf(
        MyPageItemData(
            R.string.mypage_logout,
            hasIcon = true,
            onClick = onClickLogout,
        ),
        MyPageItemData(
            R.string.mypage_withdraw,
            hasIcon = true,
            onClick = onClickWithdrawService,
        ),
    )
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(73.dp))

        val groupedItems = listOf(basicSettingsModels, infoAndPolicyModels, accountManagementModels)

        groupedItems.forEachIndexed { groupIndex, group ->
            group.forEach { itemModel ->
                MyPageItem(
                    text = stringResource(id = itemModel.textResId),
                    hasIcon = itemModel.hasIcon,
                    onClick = itemModel.onClick ?: {},
                    hasDescription = itemModel.hasDescription,
                    description = itemModel.description ?: "",
                    descriptionColor = itemModel.descriptionColor,
                )
            }
            if (groupIndex < groupedItems.size - 1) {
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surfaceTint),
                )
            }
        }
    }
}

@Composable
private fun MyPageItem(
    text: String,
    description: String = "",
    hasIcon: Boolean = false,
    hasDescription: Boolean = false,
    descriptionColor: Color = Gray500,
    onClick: () -> Unit = { },
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .clickableWithoutRipple { onClick() },
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .background(color = MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            when {
                hasIcon -> {
                    Icon(
                        painter = painterResource(id = com.qure.core.designsystem.R.drawable.ic_arrow_forward),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }

                hasDescription -> {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = descriptionColor,
                        fontSize = 18.sp,
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .height(3.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceTint),
        )
    }
}

@Preview
@Composable
private fun MyPageScreenPreview() = FMPreview {
    MyPageScreen(
        email = "test@test.com",
        navigateToBookmark = { },
        onClickWithdrawService = { },
        navigateToPolicyPrivacy = { },
        navigateToPolicyService = { },
        navigateToOpenSourceLicense = { },
        navigateToDarkMode = { },
        onClickLogout = { },
    )
}
