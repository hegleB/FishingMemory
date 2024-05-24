package com.qure.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.qure.core_design.compose.components.FMButton
import com.qure.core_design.compose.theme.Blue400
import com.qure.core_design.compose.theme.Blue600
import com.qure.core_design.compose.utils.FMPreview

@Composable
fun PermissionRoute(
    navigateToLogin: () -> Unit,
) {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(checkPermission(context)) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        hasLocationPermission =
            permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
            permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true

        if (hasLocationPermission) {
            navigateToLogin()
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }
    PermissionContent(
        modifier = Modifier.fillMaxSize(),
        onPermissionClick = {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        },
    )
}

private fun checkPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun PermissionContent(
    modifier: Modifier = Modifier,
    onPermissionClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 60.dp),
        ) {
            PermissionTitle()
            PermissionSettingDescription()
            AccessPermission(R.string.required_permission)

            PermissiontTypeDescription(
                typeStringId = R.string.permission_location,
                descriptionStringId = R.string.permission_location_description,
            )
            AccessPermission(R.string.selected_permission)
            PermissiontTypeDescription(
                typeStringId = R.string.permission_camera,
                descriptionStringId = R.string.permission_camera_description,
            )
            PermissiontTypeDescription(
                typeStringId = R.string.permission_storage,
                descriptionStringId = R.string.permission_storage_description,
            )
            PermissionOptionalDescription(R.string.permission_optional_description)
            PermissionOptionalDescription(R.string.permission_optional_setting)
        }
        Spacer(modifier = Modifier.weight(1f, fill = true))
        FMButton(
            onClick = { onPermissionClick() },
            text = stringResource(id = R.string.confirm),
            textStyle = MaterialTheme.typography.displayMedium,
            textModifier = Modifier,
            buttonColor = Blue600,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            fontColor = Color.White,
            shape = RectangleShape,
        )
    }
}

@Composable
private fun AccessPermission(@StringRes permissionType: Int) {
    Text(
        modifier = Modifier.padding(top = 20.dp),
        text = stringResource(id = permissionType),
        color = Blue400,
        fontSize = 15.sp,
        style = MaterialTheme.typography.displaySmall,
    )
}

@Composable
private fun PermissionSettingDescription() {
    Text(
        modifier = Modifier.padding(top = 10.dp),
        text = stringResource(id = R.string.permission_description),
        style = MaterialTheme.typography.bodySmall,
    )
}

@Composable
private fun PermissionTitle() {
    Text(
        text = stringResource(id = R.string.permission_title),
        style = MaterialTheme.typography.headlineLarge,
        fontSize = 25.sp,
    )
}

@Composable
private fun PermissionOptionalDescription(@StringRes descriptionStringId: Int) {
    Row(modifier = Modifier.padding(top = 20.dp, start = 10.dp)) {
        Text(text = stringResource(id = R.string.dot))
        Text(text = stringResource(id = descriptionStringId))
    }
}

@Composable
private fun PermissiontTypeDescription(
    @StringRes typeStringId: Int,
    @StringRes descriptionStringId: Int,
) {
    BoxWithConstraints(modifier = Modifier.padding(top = 30.dp)) {
        val guideline = maxWidth * 0.3f
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = stringResource(id = typeStringId),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .width(1.dp)
                .align(Alignment.TopStart)
                .offset(x = guideline),
        ) {
            Text(
                text = stringResource(id = descriptionStringId),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PermissionScreenPreview() = FMPreview {
    PermissionScreen({})
}
