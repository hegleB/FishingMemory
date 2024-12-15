package com.qure.memo.share

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.qure.ui.model.MemoUI
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.LinkProperties

class DeepLinkHelper(
    private val context: Context,
    private val errorMessage: (String) -> Unit,
) {
    fun createDynamicLink(memo: MemoUI) {
        val baseUrl = memo.image.split("%2F")[0]
        val imagePath = memo.image.split("%2F")[1]

        val branchUniversalObject = BranchUniversalObject()
            .setCanonicalIdentifier("memo/${memo.uuid}")
            .setTitle(memo.title)
            .setContentImageUrl(baseUrl + imagePath)
            .setContentDescription(memo.content)
            .addKeyWords(arrayListOf("#${memo.fishType} #${memo.fishSize}CM ${memo.waterType}"))
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)

        val linkProperties = LinkProperties()
            .setChannel("app")
            .setFeature("sharing")
            .setCampaign("fishing_memo_campaign")
            .addControlParameter("title", memo.title)
            .addControlParameter("waterType", memo.waterType)
            .addControlParameter("fishType", memo.fishType)
            .addControlParameter("fishSize", memo.fishSize)
            .addControlParameter("createTime", memo.date)
            .addControlParameter("content", memo.content)
            .addControlParameter("baseUrl", baseUrl)
            .addControlParameter("imagePath", imagePath)
            .addControlParameter("location", memo.location)
            .addControlParameter("uuid", memo.uuid)
            .addControlParameter("email", memo.email)
            .addControlParameter("date", memo.date)
            .addControlParameter("createTime", memo.createTime)
            .addControlParameter("coords", memo.coords)




        branchUniversalObject.generateShortUrl(context, linkProperties) { url, error ->
            if (error == null) {
                shareDeepLink(Uri.parse(url))
            } else {
                errorMessage("Error creating Branch link: ${error.message}")
            }
        }
    }

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
//            Timber.d("$ignored")
        }
    }
}
