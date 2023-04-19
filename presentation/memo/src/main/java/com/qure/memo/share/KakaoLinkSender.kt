package com.qure.memo.share

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.qure.core.util.FishingMemoryToast
import com.qure.memo.detail.DetailMemoActivity
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_BASE_URL
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_CONTENT
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_CREATE_TIME
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_FISH_SIZE
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_FISH_TYPE
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_IMAGE_PATH
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_LOCATION
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_TITLE
import com.qure.memo.detail.DetailMemoActivity.Companion.QUERY_WATER_TYPE
import com.qure.memo.detail.DetailMemoActivity.Companion.SIZE_UNIT
import com.qure.memo.detail.DetailMemoActivity.Companion.SLASH
import com.qure.memo.model.MemoUI

class KakaoLinkSender(private val context: Context, private val memo: MemoUI) {

    private val imageBaseUrl: String by lazy { memo.image.split(SLASH)[0] }
    private val imagePath: String by lazy { memo.image.split(SLASH)[1] }

    fun send() {
        val feedTemplate = createFeedTemplate()
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            shareToKakaoTalk(feedTemplate)
        } else {
            shareToWeb(feedTemplate)
        }
    }

    private fun createFeedTemplate(): FeedTemplate {
        val tags = listOf(
            memo.waterType,
            memo.fishType,
            memo.fishSize + SIZE_UNIT
        ).joinToString(" #")
        return FeedTemplate(
            content = Content(
                title = memo.title,
                description = "#$tags",
                imageUrl = memo.image,
                link = Link(
                    mobileWebUrl = MOBILE_WEB_URL,
                    androidExecutionParams = createLinkParams()
                )
            ),
            buttons = listOf(createFeedButton())
        )
    }

    private fun createLinkParams(): Map<String, String> {
        return mapOf(
            QUERY_TITLE to memo.title,
            QUERY_WATER_TYPE to memo.waterType,
            QUERY_FISH_TYPE to memo.fishType,
            QUERY_FISH_SIZE to memo.fishSize,
            QUERY_CREATE_TIME to memo.date,
            QUERY_LOCATION to memo.location,
            QUERY_CONTENT to memo.content,
            QUERY_BASE_URL to imageBaseUrl,
            QUERY_IMAGE_PATH to imagePath
        )
    }

    private fun createFeedButton(): Button {

        return Button(
            title = FEED_BUTTON_DETAIL,
            link = Link(androidExecutionParams = createLinkParams())
        )
    }

    private fun shareToKakaoTalk(feedTemplate: FeedTemplate) {
        ShareClient.instance.shareDefault(context, feedTemplate) { sharingResult, error ->
            if (error != null) {
                FishingMemoryToast().error(context, TOAST_ERROR_SHARE)
            } else if (sharingResult != null) {
                context.startActivity(sharingResult.intent)
            }
        }
    }

    private fun shareToWeb(feedTemplate: FeedTemplate) {
        val sharerUrl = WebSharerClient.instance.makeDefaultUrl(feedTemplate)
        try {
            KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
        } catch (e: UnsupportedOperationException) {
            FishingMemoryToast().error(context, TOAST_ERROR_BROWSER)
        }
        try {
            KakaoCustomTabsClient.open(context, sharerUrl)
        } catch (e: ActivityNotFoundException) {
            FishingMemoryToast().error(context, TOAST_ERROR_BROWSER)
        }
    }

    companion object {
        private const val TOAST_ERROR_BROWSER = "chrome 또는 인터넷 브라우저를 설치해주세요"
        private const val TOAST_ERROR_SHARE = "카카오 공유 실패"
        private const val FEED_BUTTON_DETAIL = "자세히 보기"
        private const val MOBILE_WEB_URL = "https://play.google.com/store/apps/details?id=com.qure.fishingmemory"
    }
}