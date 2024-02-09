package com.qure.onboarding

import androidx.annotation.RawRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qure.core_design.compose.components.FMButton
import com.qure.core_design.compose.components.FMLottieAnimation
import com.qure.core_design.compose.theme.Blue500
import com.qure.core_design.compose.theme.Gray200
import com.qure.core_design.compose.theme.Gray450
import com.qure.core_design.compose.theme.White
import com.qure.core_design.compose.utils.FMPreview
import com.qure.core_design.compose.utils.clickableWithoutRipple
import kotlinx.coroutines.launch

data class OnboardingData(
    val title: String,
    @RawRes val onboardingResId: Int,
)

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    navigateToPermission: () -> Unit,
) {
    OnboardingContent(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        navigateToPermission = navigateToPermission,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingContent(
    modifier: Modifier = Modifier,
    navigateToPermission: () -> Unit,
) {
    val onboardingData = listOf(
        OnboardingData(
            title = stringResource(id = R.string.onboarding_first_title),
            onboardingResId = R.raw.first_page,
        ),
        OnboardingData(
            title = stringResource(id = R.string.onboarding_second_title),
            onboardingResId = R.raw.second_page,
        ),
        OnboardingData(
            title = stringResource(id = R.string.onboarding_third_title),
            onboardingResId = R.raw.third_page,
        ),
    )
    Box(
        modifier = modifier,
    ) {
        val pagerState = rememberPagerState {
            onboardingData.size
        }
        val coroutineScope = rememberCoroutineScope()
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = pagerState,
            userScrollEnabled = false,
        ) { pageIndex ->
            val (title, resId) = onboardingData[pageIndex]
            Column(
                modifier = Modifier
                    .padding(bottom = 50.dp),
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .align(Alignment.CenterHorizontally),
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
                FMLottieAnimation(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 50.dp)
                        .fillMaxSize(),
                    lottieId = resId,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                onboardingData.forEachIndexed { index, _ ->
                    val dotSize = if (pagerState.currentPage == index) 18.dp else 12.dp
                    val dotColor = if (pagerState.currentPage == index) Gray450 else Gray200
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .background(color = dotColor, shape = CircleShape)
                            .align(Alignment.CenterVertically)
                            .clickableWithoutRipple {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                }
            }
            FMButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
                onClick = {
                    if (pagerState.currentPage == onboardingData.size - 1) {
                        navigateToPermission()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                buttonColor = Blue500,
                shape = RoundedCornerShape(15.dp),
                fontColor = White,
                text = if (pagerState.currentPage == onboardingData.size - 1) {
                    stringResource(id = R.string.onboarding_start)
                } else {
                    stringResource(id = R.string.onboarding_next)
                },
            )
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() = FMPreview {
    OnboardingContent(
        navigateToPermission = {},
    )
}
