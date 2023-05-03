package com.qure.program_information

import android.os.Bundle
import com.qure.core.BaseActivity
import com.qure.core.extensions.Empty
import com.qure.core.util.setOnSingleClickListener
import com.qure.domain.WEB_URL
import com.qure.program_information.databinding.ActivityProgramInformationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgramInformationActivity :
    BaseActivity<ActivityProgramInformationBinding>(R.layout.activity_program_information) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        val webUrl = intent.getStringExtra(WEB_URL) ?: String.Empty

        binding.imageViewActivityPolicyClose.setOnSingleClickListener {
            finish()
        }

        binding.webViewActivityPolicy.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        binding.webViewActivityPolicy.loadUrl(webUrl)
    }
}