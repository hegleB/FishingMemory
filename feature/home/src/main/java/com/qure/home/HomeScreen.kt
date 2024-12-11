package com.qure.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.qure.designsystem.component.FMLottieAnimation
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.component.FMRefreshLayout
import com.qure.designsystem.theme.Blue300
import com.qure.designsystem.theme.Gray300
import com.qure.designsystem.theme.Purple700
import com.qure.designsystem.theme.White
import com.qure.designsystem.utils.FMPreview
import com.qure.designsystem.utils.clickableWithoutRipple
import com.qure.feature.home.R
import com.qure.home.location.GpsTransfer
import com.qure.home.location.LatXLngY
import com.qure.model.extensions.DefaultLatitude
import com.qure.model.extensions.DefaultLongitude
import com.qure.model.weather.SkyState
import com.qure.ui.component.CardFront
import com.qure.ui.component.Tape
import com.qure.ui.model.HourOfWeatherState
import com.qure.ui.model.MemoUI
import com.qure.ui.model.WeatherUI
import com.qure.ui.model.categorizeWeather
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.math.absoluteValue


@Composable
fun HomeRoute(
    padding: PaddingValues,
    navigateToMemoList: () -> Unit,
    navigateToDetailMemo: (MemoUI) -> Unit,
    navigateToMap: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToMemoCreate: () -> Unit,
) {

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val latXLngY by viewModel.latLng.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }
    var isRefresh by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    if (hasLocationPermission) {
        LaunchedEffect(Unit) {
            val location = getCurrentLocation(fusedLocationClient)
            val latitude = location?.latitude ?: String.DefaultLatitude.toDouble()
            val longitude = location?.longitude ?: String.DefaultLongitude.toDouble()
            val latLng = GpsTransfer().convertGRID_GPS(0, latitude, longitude)
            viewModel.setLatLng(latLng)
            viewModel.fetchData()
        }
    }


    HomeScreen(
        modifier = Modifier
            .padding(padding),
        homeUiState = homeUiState,
        onClickMemoMore = navigateToMemoList,
        onClickMemo = { memo -> navigateToDetailMemo(memo) },
        latXLngY = latXLngY,
        navigateToMap = navigateToMap,
        onRefresh = {
            viewModel.fetchData()
            isRefresh = true
        },
        isRefresh = isRefresh,
        onClickRecord = navigateToMemoCreate,
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(fusedLocationClient: FusedLocationProviderClient): Location? {
    return suspendCancellableCoroutine { continuation ->
        val locationTask: Task<Location> = fusedLocationClient.lastLocation
        locationTask.addOnSuccessListener { location ->
            continuation.resume(location) { }
        }
        locationTask.addOnFailureListener { _ ->
            continuation.resume(null) { }
        }
    }
}

@Composable
private fun HomeScreen(
    homeUiState: HomeUiState = HomeUiState.Loading,
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    onClickMemoMore: () -> Unit = { },
    onClickMemo: (MemoUI) -> Unit = { },
    latXLngY: LatXLngY = LatXLngY(),
    navigateToMap: () -> Unit = { },
    onRefresh: () -> Unit = { },
    isRefresh: Boolean = false,
    onClickRecord: () -> Unit = { },
) {
    FMRefreshLayout(
        onRefresh = { onRefresh() },
        isRefresh = if (isRefresh) homeUiState is HomeUiState.Loading else false,
    ) {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceTint)
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 30.dp),
        ) {
            when (homeUiState) {
                HomeUiState.Loading -> {
                    FMProgressBar(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }

                is HomeUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        HomeItemList(
                            context = context,
                            latXLngY = latXLngY,
                            onClickMemo = onClickMemo,
                            onClickMemoMore = onClickMemoMore,
                            memos = homeUiState.memos,
                            weather = homeUiState.weather,
                            onClickRecord = onClickRecord,
                        )
                        MapCard(navigateToMap)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeItemList(
    context: Context,
    latXLngY: LatXLngY,
    onClickMemo: (MemoUI) -> Unit,
    onClickMemoMore: () -> Unit,
    memos: List<MemoUI> = emptyList(),
    weather: List<WeatherUI> = emptyList(),
    onClickRecord: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (memos.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = stringResource(R.string.go_to_record),
                    modifier = Modifier
                        .clickableWithoutRipple { onClickRecord() },
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.memo_more),
                    modifier = Modifier
                        .clickableWithoutRipple { onClickMemoMore() },
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            MemoItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp),
                memos = memos.sortedByDescending { it.fishSize.toFloat() },
                onClickMemo = onClickMemo,
            )
        } else {
            MemoEmptyItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp),
                onClick = onClickRecord,
            )
        }

        if (weather.isNotEmpty()) {
            var city = ""
            fetchCurrentAddress(context, latXLngY) { address ->
                city = address
            }

            WeatherItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Purple700, Blue300)
                        )
                    )
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        )
                    ),
                hoursOfWeather = weather.categorizeWeather().toImmutableList(),
                location = city,
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Purple700, Blue300)
                        )
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = stringResource(id = R.string.no_seacrh_weather),
                    style = MaterialTheme.typography.displayLarge,
                    color = White,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun MemoEmptyItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(color = Color.Transparent),
    ) {
        CardFront(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .background(color = Color.White),
        )
        Text(
            text = stringResource(R.string.empty_memo),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            fontSize = 18.sp,
        )
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 50.dp),
        ) {
            Text(
                text = stringResource(R.string.go_to_record),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
            )
        }
        Tape(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    rotationZ = 50f
                    rotationY = 180f
                }
                .height(65.dp)
                .width(30.dp)
                .alpha(0.5f)
                .background(color = Color.Transparent)
        )
    }
}

@Composable
private fun MemoItem(
    modifier: Modifier = Modifier,
    memos: List<MemoUI> = emptyList(),
    onClickMemo: (MemoUI) -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { memos.size }
    )
    val contentPadding = 30.dp
    val pageSpacing = 10.dp
    val scaleSizeRatio = 0.8f

    Box(
        modifier = modifier
            .background(color = Color.Transparent),
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            state = pagerState,
            key = { memos[it].uuid },
            contentPadding = PaddingValues(horizontal = contentPadding),
            pageSpacing = pageSpacing,
        ) { page ->
            val memo = memos[page]
            val pageOffset = pagerState.currentPage - page + pagerState.currentPageOffsetFraction
            val isTaped = pageOffset.absoluteValue.coerceIn(0f, 1f) <= 0.2f
            val tapeScale by animateFloatAsState(
                targetValue = if (isTaped) 1f else 0f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                label = "tapeScale"
            )
            val tapeAlpha by animateFloatAsState(
                targetValue = if (isTaped) 1f else 0f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                label = "tapeAlpha"
            )
            val tapeTranslation by animateFloatAsState(
                targetValue = if (isTaped) 0f else -100f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                label = "tapeTranslation"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                        )
                        lerp(
                            start = 1f,
                            stop = scaleSizeRatio,
                            fraction = pageOffset.absoluteValue.coerceIn(0f, 1f),
                        ).let {
                            scaleX = it
                            scaleY = it
                            val sign = if (pageOffset > 0) 1 else -1
                            translationX = sign * size.width * (1 - it) / 2
                        }
                    },
            ) {
                CardFront(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .background(color = Color.White)
                        .clickableWithoutRipple { onClickMemo(memo) },
                    fishType = memo.fishType,
                    waterType = memo.waterType,
                    size = memo.fishSize,
                    imageUrl = memo.image,
                )
                Tape(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .graphicsLayer {
                            scaleX = tapeScale
                            scaleY = tapeScale
                            alpha = tapeAlpha
                            translationY = tapeTranslation
                            rotationZ = 50f
                            rotationY = 180f
                        }
                        .height(65.dp)
                        .width(30.dp)
                        .alpha(0.5f)
                        .background(color = Color.Transparent)
                )
            }
        }
    }
}

@Composable
private fun MapCard(
    navigateToMap: () -> Unit,
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .height(80.dp)
            .clickable { navigateToMap() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.map_home),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.displayLarge
            )
            FMLottieAnimation(
                modifier = Modifier
                    .width(100.dp)
                    .align(Alignment.CenterEnd),
                lottieId = com.qure.core.designsystem.R.raw.map,
            )
        }
    }
}

@Composable
private fun WeatherItem(
    modifier: Modifier = Modifier,
    hoursOfWeather: ImmutableList<HourOfWeatherState>,
    location: String = "",
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val height = if (isExpanded) 250.dp else 150.dp
    Column(
        modifier = modifier
            .heightIn(height)
    ) {
        ItemTitle(
            title = stringResource(id = R.string.weather)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .padding(horizontal = 20.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 20.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = hoursOfWeather.first().toTemperatureString(),
                        color = White,
                        fontSize = 34.sp,
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = hoursOfWeather.first().toCurrentDateTimeWeather(),
                        color = White,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = location,
                    color = White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.displayLarge,
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = SkyState.from(hoursOfWeather.first().skyState.fcstValue.toInt()),
                    color = White,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.displaySmall,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable { isExpanded = !isExpanded },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = White,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.weather_time_zone_more),
                        color = White,
                    )
                }

                if (isExpanded) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        items(hoursOfWeather.drop(1)) { weather ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = weather.toTemperatureString(),
                                    color = White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                FMLottieAnimation(
                                    modifier = Modifier
                                        .size(50.dp),
                                    lottieId = weather.toWeatherAnimation()
                                )
                                Text(
                                    text = weather.convertToHourString(),
                                    color = White,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                }
            }
            FMLottieAnimation(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp)
                    .size(80.dp),
                lottieId = hoursOfWeather.first().toWeatherAnimation(),
            )
        }
    }
}

@Composable
private fun ItemTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier
            .padding(top = 5.dp, start = 10.dp),
        text = title,
        color = Gray300,
    )
}


private fun fetchCurrentAddress(
    context: Context,
    latXLngY: LatXLngY,
    sendAddressCityName: (String) -> Unit,
) {
    val geoCoder = Geocoder(context, Locale.getDefault())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geoCoder.getFromLocation(
            latXLngY.lat,
            latXLngY.lng,
            1
        ) { address: List<Address> ->
            sendAddressCityName(address[1].toString())
        }
    } else {
        val address: Address? = geoCoder.getFromLocation(latXLngY.lat, latXLngY.lng, 1)?.get(0)
        if (address != null) {
            sendAddressCityName(address.getAddressLine(0).split(" ")[1])
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF6F7F9)
private fun HomeScreenPreview() = FMPreview {
    HomeScreen()
}