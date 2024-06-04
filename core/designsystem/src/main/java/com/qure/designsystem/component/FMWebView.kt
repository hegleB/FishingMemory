package com.qure.designsystem.component

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun FMWebView(
    modifier: Modifier = Modifier,
    webUrl: String = ""
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                with(settings) {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }
            }
        },
    ) { webView ->
        webView.loadUrl(webUrl)
    }
}

