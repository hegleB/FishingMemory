package com.qure.memo.share

import android.content.ActivityNotFoundException
import android.content.Context
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.qure.model.extensions.HashTag
import com.qure.ui.model.MemoUI

class KakaoLinkSender(
    private val context: Context,
    private val memo: MemoUI,
    private val errorMessage: (String) -> Unit,
) {
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
        val tags =
            listOf(
                memo.waterType,
                memo.fishType,
                memo.fishSize + SIZE_UNIT,
            ).joinToString(String.HashTag)
        return FeedTemplate(
            content =
            Content(
                title = memo.title,
                description = "#$tags",
                imageUrl = memo.image,
                link =
                Link(
                    mobileWebUrl = MOBILE_WEB_URL,
                    androidExecutionParams = createLinkParams(),
                ),
            ),
            buttons = listOf(createFeedButton()),
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
            QUERY_IMAGE_PATH to imagePath,
        )
    }

    private fun createFeedButton(): Button {
        return Button(
            title = FEED_BUTTON_DETAIL,
            link = Link(androidExecutionParams = createLinkParams()),
        )
    }

    private fun shareToKakaoTalk(feedTemplate: FeedTemplate) {
        ShareClient.instance.shareDefault(context, feedTemplate) { sharingResult, error ->
            if (error != null) {
                errorMessage(TOAST_ERROR_SHARE)
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
            errorMessage(TOAST_ERROR_BROWSER)
        }
        try {
            KakaoCustomTabsClient.open(context, sharerUrl)
        } catch (e: ActivityNotFoundException) {
            errorMessage(TOAST_ERROR_BROWSER)
        }
    }

    companion object {
        private const val TOAST_ERROR_BROWSER = "chrome 또는 인터넷 브라우저를 설치해주세요"
        private const val TOAST_ERROR_SHARE = "카카오 공유 실패"
        private const val FEED_BUTTON_DETAIL = "자세히 보기"
        private const val MOBILE_WEB_URL =
            "https://play.google.com/store/apps/details?id=com.qure.fishingmemory"
        private const val SIZE_UNIT = "CM"

        const val QUERY_TITLE = "title"
        const val QUERY_WATER_TYPE = "waterType"
        const val QUERY_FISH_SIZE = "fishSize"
        const val QUERY_FISH_TYPE = "fishType"
        const val QUERY_CREATE_TIME = "createTime"
        const val QUERY_LOCATION = "location"
        const val QUERY_CONTENT = "content"
        const val QUERY_BASE_URL = "baseUrl"
        const val QUERY_IMAGE_PATH = "imagePath"

        const val SLASH = "%2F"
    }
}
