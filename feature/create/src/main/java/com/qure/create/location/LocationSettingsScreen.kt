package com.qure.create.location

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
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.qure.designsystem.component.FMButton
import com.qure.designsystem.component.FMCloseButton
import com.qure.designsystem.component.FMNaverMap
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.theme.Blue500
import com.qure.designsystem.theme.Blue600
import com.qure.designsystem.theme.Gray100
import com.qure.designsystem.theme.Gray300
import com.qure.designsystem.theme.White
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.create.R
import com.qure.model.extensions.DefaultLatitude
import com.qure.model.extensions.DefaultLongitude
import com.qure.ui.extentions.toReverseCoordsString
import com.qure.ui.model.MemoUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale

data class LocationData(
    val title: String = "",
    val subTitle: String = "",
)

@SuppressLint("MutableCollectionMutableState")
@Composable
fun LocationSettingRoute(
    memo: MemoUI = MemoUI(),
    viewModel: LocationSettingViewModel = hiltViewModel(),
    onBack: () -> Unit,
    navigateToMemoCreate: (MemoUI) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }
    val geoCodingUiState by viewModel.geoCodingUiState.collectAsStateWithLifecycle()
    val reverseGeoCodingUiState by viewModel.reverseGeoCodingUiState.collectAsStateWithLifecycle()
    val locationUiState by viewModel.locationUiState.collectAsStateWithLifecycle()
    val locationPages = listOf(
        LocationData(
            title = stringResource(id = R.string.selection_do),
            subTitle = stringResource(id = R.string.do_name),
        ),
        LocationData(
            title = stringResource(id = R.string.selection_city),
            subTitle = stringResource(id = R.string.city_name),
        ),
        LocationData(
            title = stringResource(id = R.string.selection_map),
            subTitle = stringResource(id = R.string.map),
        )
    )

    LocationSettingScreen(
        onBack = onBack,
        locationPages = locationPages,
        setDoIndex = viewModel::setDoIndexData,
        setCityIndex = viewModel::setCityIndexData,
        locationUiState = locationUiState,
        onNextPage = viewModel::onClickNext,
        onPreviousPage = viewModel::onClickPrevious,
        doIndex = viewModel.doIndex,
        cityIndex = viewModel.cityIndex,
        geoCodingUiState = geoCodingUiState,
        reverseGeoCodingUiState = reverseGeoCodingUiState,
        fetchGeocoding = viewModel::fetchGeocoding,
        fetchReverseGeocoding = viewModel::fetchReverseGeocoding,
        setRegionName = viewModel::setRegionName,
        selectedRegionNames = viewModel.selectedRegionName,
        navigateToMemoCreate = { location, coords ->
            navigateToMemoCreate(
                memo.copy(
                    location = location,
                    coords = coords
                )
            )
        },
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

enum class Direction {
    LEFT,
    RIGHT,
}

@Composable
private fun LocationSettingScreen(
    geoCodingUiState: GeoCodingUiState = GeoCodingUiState.Idle,
    reverseGeoCodingUiState: ReverseGeoCodingUiState = ReverseGeoCodingUiState.Idle,
    onBack: () -> Unit = { },
    locationPages: List<LocationData> = emptyList(),
    setDoIndex: (Int) -> Unit = { },
    setCityIndex: (Int) -> Unit = { },
    locationUiState: LocationUiState = LocationUiState(),
    doIndex: Int = -1,
    cityIndex: Int = -1,
    onNextPage: () -> Unit = { },
    onPreviousPage: () -> Unit = { },
    fetchGeocoding: (String) -> Unit = { },
    fetchReverseGeocoding: (String) -> Unit = { },
    setRegionName: (String) -> Unit = { },
    selectedRegionNames: List<String> = emptyList(),
    navigateToMemoCreate: (String, String) -> Unit = { _, _ -> },
) {
    var direction by remember { mutableStateOf(Direction.LEFT) }
    var latitude by remember { mutableDoubleStateOf(String.DefaultLatitude.toDouble()) }
    var longitude by remember { mutableDoubleStateOf(String.DefaultLongitude.toDouble()) }
    var coords by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    var currentAddresses by remember { mutableStateOf(emptyList<Address>()) }
    LaunchedEffect(doIndex) {
        if (doIndex == Regions.entries.lastIndex) {
            val location = getCurrentLocation(fusedLocationClient)
            latitude = location?.latitude ?: String.DefaultLatitude.toDouble()
            longitude = location?.longitude ?: String.DefaultLongitude.toDouble()
            coords = "$latitude, $longitude"
            fetchCurrentAddresses(latitude, longitude, context) {
                currentAddresses = it
            }
            val currentAddress = currentAddresses.last()
                .getAddressLine(0)
                .split(" ")
                .drop(1)
                .joinToString(" ") {
                    it.replace("-", " ")
                }
            setRegionName(currentAddress)
        }
    }

    LaunchedEffect(geoCodingUiState) {
        if (geoCodingUiState is GeoCodingUiState.Success) {
            latitude =
                geoCodingUiState.geocoding?.y?.toDouble() ?: String.DefaultLatitude.toDouble()
            longitude =
                geoCodingUiState.geocoding?.x?.toDouble() ?: String.DefaultLongitude.toDouble()
            coords = geoCodingUiState.geocoding?.coords ?: ""
            if (locationUiState.currentPage == 1) {
                onNextPage()
                direction = Direction.LEFT
            }
        }
    }
    LaunchedEffect(reverseGeoCodingUiState) {
        if (reverseGeoCodingUiState is ReverseGeoCodingUiState.Success) {
            val regionName = if (doIndex == Regions.entries.lastIndex) {
                reverseGeoCodingUiState.reverseGeocoding
                    ?.areaName
                    ?.split(" ")
                    ?.joinToString(" ") ?: ""
            } else {
                reverseGeoCodingUiState.reverseGeocoding
                    ?.areaName
                    ?.split(" ")?.drop(2)
                    ?.joinToString(" ") ?: ""
            }
            setRegionName(regionName)
        }
    }

    LocationContent(
        onBack = onBack,
        locationPages = locationPages,
        onNextPage = {
            when (locationUiState.currentPage) {
                1 -> {
                    coroutineScope.launch {
                        fetchGeocoding(selectedRegionNames.joinToString(" "))
                    }
                }

                2 -> {
                    navigateToMemoCreate(selectedRegionNames.joinToString(" "), coords)
                }

                else -> {
                    coroutineScope.launch {
                        onNextPage()
                    }
                    direction = Direction.LEFT
                }
            }
        },
        onPreviousPage = {
            onPreviousPage()
            direction = Direction.RIGHT
        },
        locationUiState = locationUiState,
        isEnabledNext = doIndex != -1,
        selectedRegionName = selectedRegionNames.joinToString(" "),
    ) { paddingValues ->
        AnimatedContent(
            targetState = locationUiState,
            transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(300)
                val transitionDirection = getTransitionDirection(
                    direction = direction,
                )
                slideIntoContainer(
                    towards = transitionDirection,
                    animationSpec = animationSpec,
                ) togetherWith slideOutOfContainer(
                    towards = transitionDirection,
                    animationSpec = animationSpec
                )
            },
            label = "LocationSettingScreenDataAnimation"
        ) { targetState ->
            val regions = if (targetState.region == Regions.REGION) {
                RegionData.regions.map { it.name }
            } else {
                if (doIndex != Regions.entries.lastIndex) {
                    RegionData.regions[doIndex].subRegions
                } else {
                    emptyList()
                }
            }
            if (geoCodingUiState is GeoCodingUiState.Loading || reverseGeoCodingUiState is ReverseGeoCodingUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    FMProgressBar(
                        modifier = Modifier
                            .align(Alignment.Center),
                    )
                }
            }

            if (targetState.currentPage == 2) {
                LocationMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    latitude = latitude,
                    longitude = longitude,
                    fetchReverseGeocoding = fetchReverseGeocoding,
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                ) {
                    if (locationUiState.currentPage == 0) {
                        MyLocationContent(
                            selectedIndex = doIndex,
                            onClick = { index ->
                                setDoIndex(index)
                            },
                            setRegionName = setRegionName,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    RegionContent(
                        modifier = Modifier,
                        selectedIndex = if (locationUiState.currentPage == 0) doIndex else cityIndex,
                        onClick = if (locationUiState.currentPage == 0) setDoIndex else setCityIndex,
                        regions = regions,
                        setRegionName = setRegionName,
                    )
                }
            }
        }
    }
}

private fun fetchCurrentAddresses(
    latitude: Double,
    longitude: Double,
    context: Context,
    setCurrentAddress: (List<Address>) -> Unit,
) {
    val geocoder = Geocoder(context, Locale.KOREA)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getGeocoderLocation(geocoder, latitude, longitude) {
            setCurrentAddress(it)
        }
    } else {
        setCurrentAddress(geocoder.getFromLocation(latitude, longitude, 1) ?: emptyList())
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun getGeocoderLocation(
    geocoder: Geocoder,
    latitude: Double,
    longitude: Double,
    getFromLocation: (List<Address>) -> Unit,
) {
    geocoder.getFromLocation(latitude, longitude, 1) { result ->
        getFromLocation(result)
    }
}

@Composable
private fun LocationMap(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    fetchReverseGeocoding: (String) -> Unit
) {
    var markerState by remember {
        mutableStateOf(MarkerState(LatLng(latitude, longitude)))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(latitude, longitude), 14.0)
    }

    FMNaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        markerState = markerState,
        markerHeight = 30.dp,
        markerWidth = 35.dp,
        icon = OverlayImage.fromResource(com.qure.core.designsystem.R.drawable.bg_map_fill_marker),
        onMapClick = { latLng ->
            markerState = MarkerState(latLng)
            fetchReverseGeocoding(latLng.toReverseCoordsString())
        }
    )
}

private fun getTransitionDirection(
    direction: Direction,
): AnimatedContentTransitionScope.SlideDirection {
    return if (direction == Direction.LEFT) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        AnimatedContentTransitionScope.SlideDirection.Right
    }
}

@Composable
private fun LocationContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
    onNextPage: () -> Unit = { },
    onPreviousPage: () -> Unit = { },
    locationPages: List<LocationData> = emptyList(),
    locationUiState: LocationUiState = LocationUiState(),
    isEnabledNext: Boolean = true,
    selectedRegionName: String = "",
    content: @Composable (PaddingValues) -> Unit = { },
) {
    Scaffold(
        topBar = {
            Column(
                modifier = modifier
                    .padding(20.dp),
            ) {
                FMCloseButton(
                    modifier = modifier
                        .size(25.dp),
                    onClickClose = { onBack() },
                    iconColor = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    modifier = Modifier
                        .padding(top = 30.dp),
                    text = locationPages[locationUiState.currentPage].title,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                ) {
                    Text(
                        text = locationPages[locationUiState.currentPage].subTitle,
                        modifier = modifier
                            .align(Alignment.CenterStart),
                        color = Gray300,
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.displaySmall,
                    )

                    Text(
                        text = selectedRegionName,
                        modifier = modifier
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displayLarge,
                    )
                }
            }
        },
        content = content,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
            ) {
                if (locationUiState.currentPage > 0) {
                    FMButton(
                        modifier = modifier
                            .width(100.dp)
                            .height(50.dp)
                            .align(Alignment.CenterStart),
                        onClick = onPreviousPage,
                        shape = RoundedCornerShape(15.dp),
                        text = stringResource(id = R.string.location_setting_previous),
                        buttonColor = Blue500,
                        textStyle = MaterialTheme.typography.displaySmall,
                        fontColor = White,
                    )
                }

                FMButton(
                    modifier = modifier
                        .width(100.dp)
                        .height(50.dp)
                        .align(Alignment.CenterEnd),
                    onClick = onNextPage,
                    shape = RoundedCornerShape(15.dp),
                    text = if (locationUiState.currentPage == 2) {
                        stringResource(id = R.string.location_setting_setting)
                    } else stringResource(
                        id = R.string.location_setting_next
                    ),
                    buttonColor = Blue500,
                    textStyle = MaterialTheme.typography.displaySmall,
                    fontColor = White,
                    isEnabled = isEnabledNext
                )
            }
        }
    )
}

@Composable
private fun MyLocationContent(
    selectedIndex: Int,
    onClick: (Int) -> Unit,
    setRegionName: (String) -> Unit,
) {
    RegionItem(
        region = "현재 나의 위치",
        isSelected = selectedIndex == Regions.entries.lastIndex,
        onClick = {
            onClick(Regions.entries.lastIndex)
        }
    )
}

@Composable
private fun RegionContent(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onClick: (Int) -> Unit,
    regions: List<String>,
    setRegionName: (String) -> Unit,
) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        itemsIndexed(regions) { index, region ->
            RegionItem(
                region = region,
                isSelected = index == selectedIndex,
                onClick = {
                    onClick(index)
                    setRegionName(region)
                },
            )
        }
    }
}

@Composable
private fun RegionItem(
    region: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(35.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = Gray100,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterStart),
            text = region,
            fontSize = 10.sp,
            color = if (isSelected) Blue600 else MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displaySmall
        )

        if (isSelected) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 5.dp)
                    .align(Alignment.CenterEnd),
                painter = painterResource(id = com.qure.core.designsystem.R.drawable.ic_check),
                contentDescription = null,
                tint = Blue600
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LocationSettingContentPreView() = FMPreview {
    LocationSettingScreen()
}