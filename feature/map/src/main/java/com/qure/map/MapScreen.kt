package com.qure.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.qure.designsystem.component.FMBackButton
import com.qure.designsystem.component.FMChipGroup
import com.qure.designsystem.component.FMFishingSpotItem
import com.qure.designsystem.component.FMMemoItem
import com.qure.designsystem.component.FMNaverMap
import com.qure.designsystem.component.FMProgressBar
import com.qure.designsystem.component.Orientation
import com.qure.designsystem.theme.Blue400
import com.qure.designsystem.theme.Blue600
import com.qure.designsystem.theme.Gray300
import com.qure.designsystem.theme.White
import com.qure.designsystem.utils.FMPreview
import com.qure.feature.map.R
import com.qure.model.FishingSpotUI
import com.qure.model.extensions.DefaultLatitude
import com.qure.model.extensions.DefaultLongitude
import com.qure.model.map.MapType
import com.qure.model.map.MarkerType
import com.qure.model.toTedClusterItem
import com.qure.ui.model.FishingPlaceInfo
import com.qure.ui.model.MemoUI
import com.qure.ui.model.MovingCameraType
import com.qure.ui.model.MovingCameraWrapper
import com.qure.ui.model.SheetHeight
import com.qure.ui.model.toTedClusterItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import ted.gun0912.clustering.clustering.TedClusterItem

@Composable
fun MapRoute(
    onBack: () -> Unit,
    navigateToDetailFishingSpot: (FishingSpotUI) -> Unit,
    navigateToDetailMemo: (MemoUI) -> Unit,
    onClickPhoneNumber: (String) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    viewModel: MapViewModel = hiltViewModel(),
) {

    LaunchedEffect(viewModel.error) {
        viewModel.error.collectLatest(onShowErrorSnackBar)
    }

    val mapUiState by viewModel.mapUiState.collectAsStateWithLifecycle()
    var hasLocationPermission by remember { mutableStateOf(false) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    MapScreen(
        uiState = mapUiState,
        onBack = onBack,
        onClickMarkerType = {
            viewModel.setMarkerType(markerType = MarkerType.getMarkerType(it))
        },
        onClickMapType = { viewModel.setMapType(mapType = MapType.getMapType(it)) },
        onClickLocation = { location ->
            viewModel.updateMovingCamera(movingCameraWrapper = MovingCameraWrapper.Moving(
                Location(MovingCameraType.MY_LOCATION.name).apply {
                    latitude = location.latitude
                    longitude = location.longitude
                }
            ))
        },
        onClickFishingSpot = navigateToDetailFishingSpot,
        onClickMemo = navigateToDetailMemo,
        onClickClusterMarkers = { items ->
            viewModel.updateClusterMarkers(clusterMarkers = items)
        },
        onClickMarker = { item ->
            viewModel.updateClusterMarkers(clusterMarkers = listOf(item))
        },
        onClickPhoneNumber = onClickPhoneNumber,
        updateMovingCamera = { movingCamera -> viewModel.updateMovingCamera(movingCameraWrapper = movingCamera) },
        updateSheetHeight = { sheetHeight -> viewModel.updateSheetHeight(sheetHeight = sheetHeight) },
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    sendErrorMessage: (String) -> Unit,
): Location? {
    return suspendCancellableCoroutine { continuation ->
        val locationTask: Task<Location> = fusedLocationClient.lastLocation
        locationTask.addOnSuccessListener { location ->
            continuation.resume(location) { }
        }
        locationTask.addOnFailureListener { exceptions ->
            continuation.resume(null) { sendErrorMessage(exceptions.message ?: "") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreen(
    modifier: Modifier = Modifier,
    uiState: MapUiState = MapUiState.Loading,
    onBack: () -> Unit = { },
    onClickMarkerType: (String) -> Unit = { },
    onClickMapType: (String) -> Unit = { },
    onClickLocation: (Location) -> Unit = { },
    onClickFishingSpot: (FishingSpotUI) -> Unit = { },
    onClickMemo: (MemoUI) -> Unit = { },
    onClickClusterMarkers: (List<TedClusterItem>) -> Unit = { },
    onClickMarker: (TedClusterItem) -> Unit = { },
    onClickPhoneNumber: (String) -> Unit = { },
    updateMovingCamera: (MovingCameraWrapper) -> Unit = { },
    updateSheetHeight: (SheetHeight) -> Unit = { },
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState()
    )

    when (uiState) {
        is MapUiState.Loading -> {
            FMProgressBar(
                modifier = Modifier
                    .size(50.dp)
            )
        }

        is MapUiState.Success -> {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContent = {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.background,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .align(Alignment.CenterHorizontally),
                                text = if (uiState.markerType == MarkerType.MEMO) {
                                    stringResource(
                                        id = R.string.memo_counter,
                                        uiState.selectedPlaceItems.size
                                    )
                                } else {
                                    stringResource(
                                        id = R.string.fishing_spot_counter,
                                        uiState.selectedPlaceItems.size
                                    )
                                },
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            LazyColumn(
                                modifier = Modifier
                                    .padding(top = 10.dp),
                            ) {
                                items(uiState.selectedPlaceItems) { item ->
                                    when (item) {
                                        is FishingPlaceInfo.MemoInfo -> {
                                            FMMemoItem(
                                                modifier = Modifier
                                                    .padding(top = 10.dp),
                                                imageUrl = item.memoUI.image,
                                                title = item.memoUI.title,
                                                location = item.memoUI.location,
                                                fishType = item.memoUI.fishType,
                                                content = item.memoUI.content,
                                                date = item.memoUI.date,
                                                onMemoClicked = { onClickMemo(item.memoUI) },
                                            )
                                        }

                                        is FishingPlaceInfo.FishingSpotInfo -> {
                                            FMFishingSpotItem(
                                                fishingSpotName = item.fishingSpotUI.fishing_spot_name,
                                                fishingGroundType = item.fishingSpotUI.fishing_ground_type,
                                                numberAddress = item.fishingSpotUI.number_address,
                                                roadAddress = item.fishingSpotUI.road_address,
                                                phoneNumber = item.fishingSpotUI.phone_number,
                                                fishType = item.fishingSpotUI.fish_type,
                                                mainPoint = item.fishingSpotUI.main_point,
                                                fee = item.fishingSpotUI.fee,
                                                onClickPhoneNumber = onClickPhoneNumber,
                                                onFishingSpotClicked = { onClickFishingSpot(item.fishingSpotUI) },
                                            )
                                        }
                                    }
                                    HorizontalDivider(modifier = Modifier.padding(top = 10.dp))
                                }
                            }
                        }
                    }
                },
                sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                sheetContainerColor = MaterialTheme.colorScheme.background,
                sheetPeekHeight = uiState.sheetHeight.height,
            ) {
                MapContent(
                    uiState = uiState,
                    onBack = onBack,
                    onClickMap = { updateSheetHeight(SheetHeight.MEDIUM) },
                    onClickMarkerType = { type ->
                        onClickMarkerType(type)
                        updateSheetHeight(SheetHeight.MEDIUM)
                    },
                    onClickMapType = onClickMapType,
                    onClickLocation = onClickLocation,
                    onClickCluster = { items ->
                        onClickClusterMarkers(items)
                        updateSheetHeight(SheetHeight.LARGE)
                    },
                    onClickMarker = { item ->
                        onClickMarker(item)
                        updateSheetHeight(SheetHeight.LARGE)
                    },
                    updateMovingCamera = updateMovingCamera,
                )
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun MapContent(
    uiState: MapUiState.Success,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { },
    onClickMap: () -> Unit = { },
    onClickMarkerType: (String) -> Unit = { },
    onClickMapType: (String) -> Unit = { },
    onClickLocation: (Location) -> Unit = { },
    onClickCluster: (List<TedClusterItem>) -> Unit = { },
    onClickMarker: (TedClusterItem) -> Unit = { },
    updateMovingCamera: (MovingCameraWrapper) -> Unit = { },
) {

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val coroutineScope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(uiState.movingCameraState) {
        when (uiState.movingCameraState) {
            MovingCameraWrapper.Default -> {}
            is MovingCameraWrapper.MyLocation -> {
                cameraPositionState.animate(
                    update = CameraUpdate.scrollTo(LatLng(uiState.movingCameraState.location))
                )
                updateMovingCamera(MovingCameraWrapper.Default)
            }

            is MovingCameraWrapper.Moving -> {
                cameraPositionState.animate(
                    update = CameraUpdate.scrollTo(LatLng(uiState.movingCameraState.location))
                )
                updateMovingCamera(MovingCameraWrapper.Default)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        FMNaverMap(
            locationSource = rememberFusedLocationSource(),
            cameraPositionState = cameraPositionState,
            modifier = Modifier
                .fillMaxSize(),
            onMapClick = {
                onClickMap()
            },
            markers = when (uiState.markerType) {
                MarkerType.MEMO -> {
                    uiState.memos.map { it.memoUI.toTedClusterItem() }
                }

                else -> {
                    uiState.fishingSpots.map { it.fishingSpotUI.toTedClusterItem() }
                }
            }.sortedByDescending { it.getTedLatLng().latitude },
            isClusteringMarkers = true,
            mapType = getMapType(type = uiState.mapType),
            onClickClusterItems = { clusterItems ->
                onClickCluster(clusterItems)
            },
            onClickMarker = { item ->
                onClickMarker(item)
            },
            mapHeight = uiState.sheetHeight.height + 20.dp,
            locationTrackingMode = LocationTrackingMode.NoFollow,
            onMapLoaded = {
                if (MapViewModel.initialMarkerLoadFlag) {
                    MapViewModel.initialMarkerLoadFlag = false
                    coroutineScope.launch {
                        val location = getCurrentLocation(fusedLocationClient) { _ ->

                        }
                        val lat = location?.latitude ?: String.DefaultLatitude.toDouble()
                        val lng = location?.longitude ?: String.DefaultLongitude.toDouble()
                        updateMovingCamera(MovingCameraWrapper.MyLocation(
                            Location(MovingCameraType.MY_LOCATION.name).apply {
                                latitude = lat
                                longitude = lng
                            }
                        ))
                    }
                }
            }
        )

        FMBackButton(
            modifier = Modifier
                .padding(top = 30.dp, start = 30.dp)
                .size(35.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape,
                )
                .align(Alignment.TopStart),
            onClickBack = { onBack() },
            iconColor = MaterialTheme.colorScheme.onBackground,
        )

        FMChipGroup(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 25.dp, start = 150.dp),
            elements = listOf(
                stringResource(id = R.string.map_memo),
                stringResource(id = R.string.map_sea),
                stringResource(id = R.string.map_reservoir),
                stringResource(id = R.string.map_flatland),
                stringResource(id = R.string.map_other),
            ),
            selectedChip = uiState.markerType.value,
            selectedFontColor = White,
            unSelectedFontColor = White,
            selectedChipColor = Color(0xFF212121),
            unSelectedChipColor = Color(0xFFBFBFBF),
            chipFontSize = 12.sp,
            chipModifier = Modifier
                .width(60.dp),
            borderColor = Color.Transparent,
            chipTextStyle = MaterialTheme.typography.displayLarge,
            onClickChip = { type ->
                onClickMarkerType(type)
            },
            interval = 2.dp,
        )

        FMChipGroup(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    bottom = uiState.sheetHeight.height + 30.dp,
                    start = 5.dp,
                ),
            elements = listOf(
                stringResource(id = R.string.basic_map),
                stringResource(id = R.string.satellite_map),
                stringResource(id = R.string.terrain_map),
            ),
            selectedChip = uiState.mapType.value,
            selectedFontColor = White,
            unSelectedFontColor = White,
            selectedChipColor = Blue400,
            unSelectedChipColor = Gray300,
            chipFontSize = 12.sp,
            chipModifier = Modifier
                .width(80.dp)
                .height(30.dp),
            borderColor = Color.Transparent,
            chipTextStyle = MaterialTheme.typography.displayLarge,
            orientation = Orientation.Vertical,
            interval = 15.dp,
            onClickChip = onClickMapType,
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = uiState.sheetHeight.height + 20.dp)
                .background(
                    color = Blue600,
                    shape = CircleShape,
                ),
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val location = getCurrentLocation(fusedLocationClient, {})
                        val lat = location?.latitude ?: String.DefaultLatitude.toDouble()
                        val lng = location?.longitude ?: String.DefaultLongitude.toDouble()
                        onClickLocation(Location(MovingCameraType.MY_LOCATION.name).apply {
                            latitude = lat
                            longitude = lng
                        })
                    }
                },
                modifier = Modifier
                    .size(35.dp),
            ) {
                Icon(
                    painter = painterResource(id = com.qure.core.designsystem.R.drawable.ic_location),
                    contentDescription = null,
                    tint = White,
                )
            }
        }
    }
}

private fun getMapType(type: MapType): com.naver.maps.map.compose.MapType {
    return when (type) {
        MapType.BASIC_MAP -> com.naver.maps.map.compose.MapType.Basic
        MapType.SATELLITE_MAP -> com.naver.maps.map.compose.MapType.Satellite
        MapType.TERRAIN_MAP -> com.naver.maps.map.compose.MapType.Terrain
    }
}

@Composable
@Preview
private fun MapScreenPreview() = FMPreview {
    MapScreen()
}