package com.qure.memo.share

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.qure.core.util.setOnSingleClickListener
import com.qure.memo.R
import com.qure.memo.databinding.FragmentShareDialogBinding
import com.qure.memo.model.MemoUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareDialogFragment(memo: MemoUI) : DialogFragment() {
    private var _binding: FragmentShareDialogBinding? = null
    val binding get() = _binding!!
    private var memo: MemoUI

    init {
        this.memo = memo
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext()) {
            override fun onBackPressed() {
                dismiss()
            }
        }.apply {
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.fragment_share_dialog,
                container,
                false,
            )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels * 0.7).toInt()
        val height = LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            imageViewFragmentDialogShareClose.setOnSingleClickListener {
                dismiss()
            }

            imageViewFragmentDialogShareKakao.setOnSingleClickListener {
                KakaoLinkSender(requireContext(), memo).send()
            }

            imageViewFragmentDialogShareMore.setOnSingleClickListener {
                DeepLinkHelper(requireContext()).createDynamicLink(memo)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SHARE_TITLE = "Sharing images"
        const val TAG = "ShareDialogFragment"

        fun newInstance(memo: MemoUI): ShareDialogFragment {
            return ShareDialogFragment(memo)
        }
    }
}
