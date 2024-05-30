package com.qure.create.location

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.qure.core.extensions.DefaultLatitude
import com.qure.core.extensions.DefaultLongitude
import com.qure.core.extensions.toReverseCoordsString
import com.qure.core_design.compose.components.FMButton
import com.qure.core_design.compose.components.FMCloseButton
import com.qure.core_design.compose.components.FMNaverMap
import com.qure.core_design.compose.components.FMProgressBar
import com.qure.core_design.compose.theme.Black
import com.qure.core_design.compose.theme.Blue500
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.theme.Gray100
import com.qure.core_design.compose.theme.Gray300
import com.qure.core_design.compose.theme.White
import com.qure.core_design.compose.utils.FMPreview
import com.qure.create.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class LocationData(
    val title: String = "",
    val subTitle: String = "",
)

@SuppressLint("MutableCollectionMutableState")
@Composable
fun LocationSettingRoute(
    viewModel: LocationSettingViewModel,
    onClickClose: () -> Unit,
    onClickNext: () -> Unit,
    onClickPrevious: () -> Unit,
    setReverseCoordsString: (String) -> Unit,
    setGeoconding: (String) -> Unit,
    setLocation: (String, String) -> Unit,
    setDoIndex: (Int) -> Unit,
    setCityIndex: (Int) -> Unit,
    setRegions: (Array<String>) -> Unit,
    setSelectedRegions: (Array<String>) -> Unit,
) {
    val geoCodingUiState by viewModel.geoCodingUiState.collectAsStateWithLifecycle()
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
        geoCodingUiState = geoCodingUiState,
        currentPage = locationUiState.currentPage,
        onClickClose = onClickClose,
        onClickNext = onClickNext,
        onClickPrevious = onClickPrevious,
        locationPages = locationPages,
        selectDoIndex = setDoIndex,
        selectCityIndex = setCityIndex,
        selectedDoIndex = locationUiState.doIndex,
        selectedCityIndex = locationUiState.cityIndex,
        setReverseCoordsString = setReverseCoordsString,
        setGeocoding = setGeocoding,
        setLocation = setLocation,
        setRegions = setRegions,
        setSelectedRegions = setSelectedRegions,
        regions = locationUiState.regions.toTypedArray(),
        selectedRegions = locationUiState.selectedRegions.toTypedArray(),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LocationSettingScreen(
    geoCodingUiState: GeoCodingUiState = GeoCodingUiState.Success(),
    modifier: Modifier = Modifier,
    currentPage: Int = 0,
    onClickClose: () -> Unit = { },
    onClickNext: () -> Unit = { },
    onClickPrevious: () -> Unit = { },
    locationPages: List<LocationData> = emptyList(),
    selectDoIndex: (Int) -> Unit = { },
    selectCityIndex: (Int) -> Unit = { },
    selectedDoIndex: Int = -1,
    selectedCityIndex: Int = -1,
    setReverseCoordsString: (String) -> Unit = { },
    setGeocoding: (String) -> Unit = { },
    setLocation: (String, String) -> Unit = { _, _ -> },
    setRegions: (Array<String>) -> Unit = { },
    setSelectedRegions: (Array<String>) -> Unit = { },
    regions: Array<String> = emptyArray(),
    selectedRegions: Array<String> = emptyArray(),
) {
    val pagerState = rememberPagerState {
        locationPages.size
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val isLoading = geoCodingUiState is GeoCodingUiState.Loading

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        if (isLoading) {
            FMProgressBar(
                modifier = Modifier
                    .align(Alignment.Center),
            )
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            when (currentPage) {
                0 -> {
                    setRegions(Region.getArray(context))
                }

                1 -> {
                    setRegions(Region.getArray(context, selectedDoIndex))
                }
            }

            FMCloseButton(
                modifier = modifier
                    .size(25.dp),
                onClickClose = { onClickClose() },
                iconColor = MaterialTheme.colorScheme.onBackground,
            )

            LocationContent(
                modifier = Modifier
                    .weight(4f),
                pagerState = pagerState,
                locationPages = locationPages,
                selectedDoIndex = selectedDoIndex,
                selectDoIndex = selectDoIndex,
                selectCityIndex = selectCityIndex,
                selectedRegions = selectedRegions,
                setSelectedRegions = setSelectedRegions,
                regions = regions,
                selectedCityIndex = selectedCityIndex,
                setReverseCoordsString = setReverseCoordsString,
                geoCodingUiState = geoCodingUiState,
            )
            PageChangeContent(
                modifier = modifier,
                currentPage = currentPage,
                onClickPrevious = onClickPrevious,
                coroutineScope = coroutineScope,
                selectedDoIndex = selectedDoIndex,
                pagerState = pagerState,
                onClickNext = onClickNext,
                setLocation = setLocation,
                geoCodingUiState = geoCodingUiState,
                selectedRegions = selectedRegions,
                setGeocoding = setGeoconding,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PageChangeContent(
    modifier: Modifier,
    currentPage: Int,
    onClickPrevious: () -> Unit,
    coroutineScope: CoroutineScope,
    selectedDoIndex: Int,
    pagerState: PagerState,
    onClickNext: () -> Unit,
    setLocation: (String, String) -> Unit,
    geoCodingUiState: GeoCodingUiState,
    selectedRegions: Array<String>,
    setGeocoding: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (currentPage > 0) {
            FMButton(
                modifier = modifier
                    .width(100.dp)
                    .height(50.dp)
                    .align(Alignment.CenterStart),
                onClick = {
                    onClickPrevious()
                    coroutineScope.launch {

                        pagerState.animateScrollToPage(
                            page = currentPage.minus(1),
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        )
                    }
                },
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
            onClick = {
                onClickNext()
                coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        page = currentPage.plus(1),
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    )
                }
                when (currentPage) {
                    1 -> setGeocoding(selectedRegions.joinToString(" "))
                    2 -> {
                        if (geoCodingUiState is GeoCodingUiState.Success) {
                            val areaName = geoCodingUiState.reverseGeocoding?.areaName ?: selectedRegions.joinToString(" ")
                            val coords = geoCodingUiState.geocoding?.coords ?: "${String.DefaultLatitude},${String.DefaultLongitude}"
                            setLocation(areaName, coords)
                        }
                    }
                }
            },
            shape = RoundedCornerShape(15.dp),
            text = if (currentPage == 2) {
                stringResource(id = R.string.location_setting_setting)
            } else stringResource(
                id = R.string.location_setting_next
            ),
            buttonColor = Blue500,
            textStyle = MaterialTheme.typography.displaySmall,
            fontColor = White,
            isEnabled = selectedDoIndex != -1
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LocationContent(
    modifier: Modifier,
    pagerState: PagerState,
    locationPages: List<LocationData>,
    selectedDoIndex: Int,
    selectDoIndex: (Int) -> Unit,
    selectCityIndex: (Int) -> Unit,
    selectedRegions: Array<String>,
    setSelectedRegions: (Array<String>) -> Unit,
    regions: Array<String>,
    selectedCityIndex: Int,
    setReverseCoordsString: (String) -> Unit,
    geoCodingUiState: GeoCodingUiState
) {
    HorizontalPager(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        state = pagerState,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = false,
    ) { page ->
        val notExistCityName = stringResource(id = R.string.not_exist_city_name)
        LocationPage(
            locationData = locationPages[page],
            selectIndex = {
                if (pagerState.currentPage == 0) {
                    if (selectedDoIndex != it) {
                        selectDoIndex(it)
                        selectCityIndex(-1)
                        selectedRegions[1] = ""
                    }
                } else {
                    selectCityIndex(it)
                }
            },
            selectName = {
                if (it != notExistCityName) selectedRegions[pagerState.currentPage] = it
                setSelectedRegions(selectedRegions)
            },
            regions = regions,
            selectedIndex = when (pagerState.currentPage) {
                0 -> selectedDoIndex
                else -> selectedCityIndex
            },
            selectedRegionName = selectedRegions
                .copyOfRange(0, pagerState.currentPage.plus(1))
                .filter { it != notExistCityName }
                .joinToString(" "),
            currentPage = pagerState.currentPage,
            setReverseCoordsString = setReverseCoordsString,
            geoCodingUiState = geoCodingUiState,
        )
    }
}

@Composable
private fun LocationPage(
    modifier: Modifier = Modifier,
    locationData: LocationData = LocationData(),
    selectIndex: (Int) -> Unit = { },
    selectName: (String) -> Unit = { },
    regions: Array<String> = emptyArray(),
    selectedIndex: Int = -1,
    selectedRegionName: String = "",
    currentPage: Int = 0,
    setReverseCoordsString: (String) -> Unit = { },
    geoCodingUiState: GeoCodingUiState = GeoCodingUiState.Success(),
) {
    Column(
        modifier = modifier
            .padding(top = 10.dp),
    ) {
        Text(
            text = locationData.title,
            modifier = modifier,
            fontSize = 20.sp,
            style = MaterialTheme.typography.headlineLarge,
        )

        if (geoCodingUiState is GeoCodingUiState.Success) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
            ) {
                Text(
                    text = locationData.subTitle,
                    modifier = modifier
                        .align(Alignment.CenterStart),
                    color = Gray300,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.displaySmall,
                )

                Text(
                    text = geoCodingUiState.reverseGeocoding?.areaName ?: selectedRegionName,
                    modifier = modifier
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayLarge
                )
            }
            if (currentPage == 2) {
                val latitude = geoCodingUiState.geocoding?.y ?: String.DefaultLatitude
                val longitude = geoCodingUiState.geocoding?.x ?: String.DefaultLongitude
                var markerState by remember {
                    mutableStateOf(MarkerState(LatLng(latitude.toDouble(), longitude.toDouble())))
                }
                val cameraPositionState = rememberCameraPositionState {
                    position =
                        CameraPosition(LatLng(latitude.toDouble(), longitude.toDouble()), 14.0)
                }
                FMNaverMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp, bottom = 20.dp),
                    cameraPositionState = cameraPositionState,
                    markerState = markerState,
                    markerHeight = 30.dp,
                    markerWidth = 35.dp,
                    icon = OverlayImage.fromResource(com.qure.core_design.R.drawable.bg_map_fill_marker),
                    onMapClick = {
                        markerState = MarkerState(it)
                        setReverseCoordsString(it.toReverseCoordsString())
                    }
                )
            } else {
                LazyVerticalGrid(
                    modifier = modifier
                        .padding(top = 20.dp, bottom = 20.dp),
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    itemsIndexed(regions) { index, region ->
                        Box(
                            modifier = modifier
                                .width(120.dp)
                                .height(35.dp)
                                .clip(
                                    shape = RoundedCornerShape(10.dp),
                                )
                                .border(
                                    width = 1.dp,
                                    color = Gray100,
                                    shape = RoundedCornerShape(10.dp),
                                )
                                .clickable {
                                    selectIndex(index)
                                    selectName(region)
                                },
                        ) {

                            Text(
                                modifier = modifier
                                    .padding(start = 10.dp)
                                    .align(Alignment.CenterStart),
                                text = region,
                                fontSize = 10.sp,
                                color = if (selectedIndex == index) Blue600 else Black,
                                style = MaterialTheme.typography.displaySmall,
                            )

                            if (selectedIndex == index) {
                                Icon(
                                    modifier = modifier
                                        .size(20.dp)
                                        .padding(end = 5.dp)
                                        .align(Alignment.CenterEnd),
                                    painter = painterResource(id = com.qure.core_design.R.drawable.ic_check),
                                    contentDescription = null,
                                    tint = Blue600,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationSettingContentPreView() = FMPreview {
    LocationSettingScreen()
}