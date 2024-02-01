package com.qure.permission

import androidx.compose.runtime.Composable
import com.qure.core.BaseComposeActivity
import com.qure.core_design.compose.theme.FishingMemoryTheme
import com.qure.navigator.LoginNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity : BaseComposeActivity() {
    @Inject
    lateinit var loginNavigator: LoginNavigator

    @Composable
    override fun Screen() {
        FishingMemoryTheme {
            PermissionScreen(navigateToLogin = {
                startActivity(loginNavigator.intent(this))
                finish()
            })
        }
    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding.buttonActivityPermissionConfirm.setOnSingleClickListener {
//            if (checkPermission()) {
//                startActivity(loginNavigator.intent(this))
//                finish()
//            } else {
//                requestPermission()
//            }
//        }
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//            ),
//            PERMISSION_REQUEST_ACCESS_LOCATION,
//        )
//    }
//
//    private fun checkPermission(): Boolean {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//            ) == PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startActivity(loginNavigator.intent(this))
//                finish()
//            } else {
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri: Uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }
//        }
//    }
//
//    companion object {
//        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
//    }
}
