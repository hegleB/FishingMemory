package com.qure.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.qure.designsystem.component.FMChipGroup
import com.qure.designsystem.component.FMLottieAnimation
import com.qure.designsystem.component.FMMemoItem
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.component.FMRefreshLayout
import com.qure.designsystem.theme.Blue300
import com.qure.designsystem.theme.Blue500
import com.qure.designsystem.theme.Gray300
import com.qure.designsystem.theme.Purple700
import com.qure.designsystem.theme.White
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.home.R
import com.qure.home.location.GpsTransfer
import com.qure.home.location.LatXLngY
import com.qure.model.extensions.DefaultLatitude
import com.qure.model.extensions.DefaultLongitude
import com.qure.model.extensions.Spacing
import com.qure.model.weather.SkyState
import com.qure.ui.custom.barchart.BarChartView
import com.qure.ui.custom.barchart.CustomBarChartView
import com.qure.ui.model.MemoUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale


@Composable
fun HomeRoute(
    padding: PaddingValues,
    navigateToMemoList: () -> Unit,
    navigateToDetailMemo: (com.qure.ui.model.MemoUI) -> Unit,
    navigateToMap: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val selectedChip by viewModel.selectedChip.collectAsStateWithLifecycle()
    val latXLngY by viewModel.latLng.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
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

    HomeScreen(
        modifier = Modifier
            .padding(padding),
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
        locationTask.addOnFailureListener { _ ->
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
    onClickMemo: (com.qure.ui.model.MemoUI) -> Unit = { },
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
    onClickMemo: (com.qure.ui.model.MemoUI) -> Unit,
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
                        var city = ""
                        fetchCurrentAddress(context, latXLngY) { address ->
                            city = address
                        }
                        WeatherItem(
                            dateTimeWeather = homeUiState.toCurrentDateTimeWeather(),
                            temperature = homeUiState.toTemperatureString(),
                            skyState = SkyState.from(homeUiState.getSkyState().fcstValue.toInt()),
                            location = city,
                            weatherRes = homeUiState.toWeatherAnimation()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
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
    memos: List<com.qure.ui.model.MemoUI> = emptyList(),
    clickMemo: (com.qure.ui.model.MemoUI) -> Unit = { },
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
    if (memos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(R.string.empty_memo),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    } else {
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
}

@Composable
private fun FishItem(
    elements: List<String> = emptyList(),
    context: Context,
    onClickChip: (String) -> Unit,
    memos: List<com.qure.ui.model.MemoUI> = emptyList(),
    selectedChip: String = stringResource(id = R.string.fish_type),
) {
    val filteredFishes = when (selectedChip) {
        stringResource(id = R.string.fish_type) -> memos.map { it.fishType }
        stringResource(id = R.string.fish_size) -> memos.map { it.fishSize }
        else -> memos.map {
            val location = it.location.split(String.Spacing)
            if (location.size <= 1) {
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
    if (memos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(R.string.no_fish_caught),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    } else {
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
    dateTimeWeather: String = "",
    temperature: String = "",
    skyState: String = "",
    location: String = "",
    @RawRes weatherRes: Int = com.qure.core.designsystem.R.raw.weather_sunny_day,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .padding(top = 40.dp, bottom = 20.dp),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = temperature,
                    color = White,
                    fontSize = 34.sp,
                    style = MaterialTheme.typography.displayLarge,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = dateTimeWeather,
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
        Modifier.background(
            color = MaterialTheme.colorScheme.secondary
        )
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF6F7F9)
private fun HomeScreenPreview() = FMPreview {
    HomeScreen()
}