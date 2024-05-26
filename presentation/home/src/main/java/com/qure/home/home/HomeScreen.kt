package com.qure.home.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.qure.core.extensions.DefaultLatitude
import com.qure.core.extensions.DefaultLongitude
import com.qure.core.extensions.Spacing
import com.qure.core_design.compose.components.FMChipGroup
import com.qure.core_design.compose.components.FMLottieAnimation
import com.qure.core_design.compose.components.FMMemoItem
import com.qure.core_design.compose.components.FMProgressBar
import com.qure.core_design.compose.components.FMRefreshLayout
import com.qure.core_design.compose.theme.Blue300
import com.qure.core_design.compose.theme.Blue500
import com.qure.core_design.compose.theme.Gray300
import com.qure.core_design.compose.theme.Gray600
import com.qure.core_design.compose.theme.Gray700
import com.qure.core_design.compose.theme.Purple700
import com.qure.core_design.compose.theme.White
import com.qure.core_design.compose.utils.FMPreview
import com.qure.core_design.custom.barchart.BarChartView
import com.qure.core_design.custom.barchart.CustomBarChartView
import com.qure.domain.entity.weather.SkyState
import com.qure.home.R
import com.qure.memo.model.MemoUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    navigateToMemoList: () -> Unit,
    navigateToDetailMemo: (MemoUI) -> Unit,
    navigateToMap: () -> Unit,
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val selectedChip by viewModel.selectedChip.collectAsStateWithLifecycle()
    val latXLngY by viewModel.latLng.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    println(homeUiState)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }
    var isRefresh by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    LaunchedEffect(latXLngY) {
        viewModel.fetchData()
    }

    if (hasLocationPermission) {
        LaunchedEffect(Unit) {
            val location = getCurrentLocation(fusedLocationClient)
            val latitude = location?.latitude ?: String.DefaultLatitude.toDouble()
            val longitude = location?.longitude ?: String.DefaultLongitude.toDouble()
            val latXLngY = GpsTransfer().convertGRID_GPS(0, latitude, longitude)
            viewModel.setLatLng(latXLngY)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.fetchData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    if (homeUiState is HomeUiState.Success) {
        println((homeUiState as HomeUiState.Success).weather.map { it.fcstTime })
    }
    HomeScreen(
        homeUiState = homeUiState,
        onClickFishType = viewModel::setSelectedChip,
        onClickMemoMore = navigateToMemoList,
        onClickMemo = { memo -> navigateToDetailMemo(memo) },
        selectedChip = selectedChip,
        latXLngY = latXLngY,
        navigateToMap = navigateToMap,
        onRefresh = {
            viewModel.fetchData()
            isRefresh = true
        },
        isRefresh = isRefresh,
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
        locationTask.addOnFailureListener { exception ->
            continuation.resume(null) { }
        }
    }
}

data class HomeItemData(
    val modifier: Modifier,
    val title: String,
)

@Composable
private fun HomeScreen(
    homeUiState: HomeUiState = HomeUiState.Empty,
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    onClickFishType: (String) -> Unit = { },
    onClickMemoMore: () -> Unit = { },
    onClickMemo: (MemoUI) -> Unit = { },
    selectedChip: String = stringResource(id = R.string.fish_type),
    latXLngY: LatXLngY = LatXLngY(),
    navigateToMap: () -> Unit = { },
    onRefresh: () -> Unit = { },
    isRefresh: Boolean = false,
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
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .verticalScroll(rememberScrollState())
            ) {
                HomeItemList(
                    homeUiState = homeUiState,
                    context = context,
                    latXLngY = latXLngY,
                    selectedChip = selectedChip,
                    onClickFishType = onClickFishType,
                    onClickMemo = onClickMemo,
                    onClickMemoMore = onClickMemoMore
                )
                MapCard(navigateToMap)
            }
            if (homeUiState is HomeUiState.Loading && isRefresh.not()) {
                FMProgressBar(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun HomeItemList(
    homeUiState: HomeUiState,
    context: Context,
    latXLngY: LatXLngY,
    selectedChip: String,
    onClickFishType: (String) -> Unit,
    onClickMemo: (MemoUI) -> Unit,
    onClickMemoMore: () -> Unit,
) {
    val items = listOf(
        HomeItemData(
            modifier = Modifier.height(220.dp),
            title = stringResource(id = R.string.caught_fish)
        ),
        HomeItemData(
            modifier = Modifier.height(180.dp),
            title = stringResource(id = R.string.weather)
        ),
        HomeItemData(
            modifier = Modifier.height(200.dp),
            title = stringResource(id = R.string.writhing)
        ),
    )

    items.forEachIndexed { index, item ->
        HomeItem(
            modifier = item.modifier.fillMaxWidth(),
            title = item.title
        ) {
            HomeContent(
                index = index,
                homeUiState = homeUiState,
                context = context,
                latXLngY = latXLngY,
                selectedChip = selectedChip,
                onClickFishType = onClickFishType,
                onClickMemo = onClickMemo,
                onClickMemoMore = onClickMemoMore
            )
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
            .clickable { navigateToMap() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Gray600 else White)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.map_home),
                color = if (isSystemInDarkTheme()) White else Gray700,
                style = MaterialTheme.typography.displayLarge
            )
            FMLottieAnimation(
                modifier = Modifier
                    .width(100.dp)
                    .align(Alignment.CenterEnd),
                lottieId = R.raw.map
            )
        }
    }
}

@Composable
fun HomeContent(
    index: Int,
    homeUiState: HomeUiState,
    context: Context,
    latXLngY: LatXLngY,
    selectedChip: String,
    onClickFishType: (String) -> Unit,
    onClickMemo: (MemoUI) -> Unit,
    onClickMemoMore: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (homeUiState is HomeUiState.Success) {
            when (index) {
                0 -> FishItem(
                    elements = listOf(
                        stringResource(id = R.string.fish_type),
                        stringResource(id = R.string.fish_size),
                        stringResource(id = R.string.place)
                    ),
                    context = context,
                    onClickChip = onClickFishType,
                    selectedChip = selectedChip,
                    memos = homeUiState.memos
                )

                1 -> {
                    if (homeUiState.weather.isNotEmpty()) {
                        WeatherItem(
                            temperature = homeUiState.toTemperatureString(),
                            skyState = SkyState.from(homeUiState.getSkyState().fcstValue.toInt()),
                            location = getCurrentAddress(context, latXLngY),
                            weatherRes = homeUiState.toWeatherAnimation()
                        )
                    }
                }

                2 -> MemoItem(
                    memos = homeUiState.memos,
                    clickMemo = onClickMemo,
                    clickMemoMore = onClickMemoMore
                )
            }
        }
    }
}

@Composable
private fun MemoItem(
    memos: List<MemoUI> = emptyList(),
    clickMemo: (MemoUI) -> Unit = { },
    clickMemoMore: () -> Unit = { },
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp, top = 10.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { clickMemoMore() },
            text = stringResource(id = R.string.memo_more),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
    LazyRow(
        modifier = Modifier.padding(top = 20.dp, start = 10.dp),
        state = rememberLazyListState(),
    ) {
        items(items = memos) { memo ->
            FMMemoItem(
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp),
                imageUrl = memo.image,
                title = memo.title,
                location = memo.location,
                fishType = memo.fishType,
                content = memo.content,
                date = memo.date,
                onMemoClicked = { clickMemo(memo) }
            )
        }
    }
}

@Composable
private fun FishItem(
    elements: List<String> = emptyList(),
    context: Context,
    onClickChip: (String) -> Unit,
    memos: List<MemoUI> = emptyList(),
    selectedChip: String = stringResource(id = R.string.fish_type),
) {
    val filteredFishes = when (selectedChip) {
        stringResource(id = R.string.fish_type) -> memos.map { it.fishType }
        stringResource(id = R.string.fish_size) -> memos.map { it.fishSize }
        else -> memos.map {
            val location = it.location.split(String.Spacing)
            if (location[1].isEmpty()) {
                location[0]
            } else {
                location[1]
            }
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        FMChipGroup(
            modifier = Modifier.align(Alignment.TopEnd),
            chipModifier = Modifier.width(50.dp),
            elements = elements,
            chipFontSize = 12.sp,
            onClickChip = { chip -> onClickChip(chip) },
            selectedChip = selectedChip,
            selectedFontColor = White,
            unSelectedFontColor = Blue500,
        )
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            CustomBarChartView(ctx).apply {
                setRadius((15 * Resources.getSystem().displayMetrics.density + 0.5f).toInt())
            }
        }
    ) { barChart ->
        BarChartView(
            context = context,
            resources = context.resources,
            values = countElements(filteredFishes).values.toList(),
            labels = filteredFishes.distinct()
        ).initBarChart(barChart)
    }
}

private fun <T> countElements(list: List<T>): Map<T, Float> {
    val map = mutableMapOf<T, Float>()
    for (element in list) {
        map[element] = (map.getOrDefault(element, 0)).toFloat() + 1
    }
    return map
}

@Composable
private fun WeatherItem(
    temperature: String = "",
    skyState: String = "",
    location: String = "",
    @RawRes weatherRes: Int = R.raw.weather_sunny_day,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .padding(top = 40.dp, bottom = 20.dp),
    ) {
        Column {
            Text(
                text = temperature,
                color = White,
                fontSize = 34.sp,
                style = MaterialTheme.typography.displayLarge,
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = location,
                color = White,
                fontSize = 20.sp,
                style = MaterialTheme.typography.displayLarge,
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = skyState,
                color = White,
                fontSize = 18.sp,
                style = MaterialTheme.typography.displaySmall,
            )
        }
        FMLottieAnimation(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopEnd),
            lottieId = weatherRes,
        )
    }
}

private fun getCurrentAddress(
    context: Context,
    latXLngY: LatXLngY
): String {
    val geoCoder = Geocoder(context, Locale.getDefault())
    val address = geoCoder.getFromLocation(latXLngY.lat, latXLngY.lng, 7)?.get(0)?.getAddressLine(0)
    val city = address?.split(String.Spacing)?.get(1).toString()
    return city
}

@Composable
private fun HomeItem(
    modifier: Modifier = Modifier,
    title: String = "",
    content: @Composable () -> Unit = { },
) {
    val homeItemModifier = if (title == stringResource(id = R.string.weather)) {
        Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(Purple700, Blue300)
            )
        )
    } else {
        if (isSystemInDarkTheme()) {
            Modifier.background(Gray600)
        } else {
            Modifier.background(White)
        }
    }
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .padding(bottom = 20.dp),
    ) {
        Box(
            modifier = homeItemModifier.fillMaxSize(),
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp)
                    .align(Alignment.TopStart),
                text = title,
                color = Gray300,
                style = MaterialTheme.typography.displayMedium,
            )
            content()
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF6F7F9)
private fun HomeScreenPreview() = FMPreview {
    HomeScreen()
}