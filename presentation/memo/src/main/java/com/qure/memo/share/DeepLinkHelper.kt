package com.qure.memo.share

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.qure.memo.model.MemoUI
import timber.log.Timber

class DeepLinkHelper(private val context: Context) {
    fun createDynamicLink(memo: MemoUI) {
        val baseUrl = memo.image.split("%2F")[0]
        val imagePath = memo.image.split("%2F")[1]
        val uri = generateUri(memo, baseUrl, imagePath)
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(uri))
            .setDynamicLinkDomain("fishmemo.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder(context.packageName)
                    .setMinimumVersion(1)
                    .build(),
            )
            .buildShortDynamicLink()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val deepLink = task.result.shortLink ?: Uri.EMPTY
                    shareDeepLink(deepLink)
                } else {
                    Timber.i(task.toString())
                }
            }
    }

    private fun generateUri(
        memo: MemoUI,
        baseUrl: String,
        imagePath: String,
    ) = Uri.Builder()
        .scheme("https")
        .authority("fishingmemory.com")
        .path("/memo")
        .appendQueryParameter("title", memo.title)
        .appendQueryParameter("waterType", memo.waterType)
        .appendQueryParameter("fishType", memo.fishType)
        .appendQueryParameter("fishSize", memo.fishSize)
        .appendQueryParameter("createTime", memo.date.toString())
        .appendQueryParameter("content", memo.content)
        .appendQueryParameter("baseUrl", baseUrl)
        .appendQueryParameter("imagePath", imagePath)
        .appendQueryParameter("location", memo.location)
        .build()
        .toString()

    private fun shareDeepLink(deepLink: Uri) {
        val sendIntent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, deepLink.toString())
                type = "text/plain"
            }

        try {
            context.startActivity(Intent.createChooser(sendIntent, "Share"))
        } catch (ignored: ActivityNotFoundException) {
            Timber.d("$ignored")
        }
    }
}
