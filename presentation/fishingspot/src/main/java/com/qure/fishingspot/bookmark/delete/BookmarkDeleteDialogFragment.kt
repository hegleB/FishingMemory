package com.qure.fishingspot.bookmark

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qure.core.util.FishingMemoryToast
import com.qure.core.util.setOnSingleClickListener
import com.qure.fishingspot.databinding.FragmentBookmarkDeleteDialogBinding
import com.qure.memo.R
import com.qure.memo.databinding.FragmentDeleteDialogBinding
import com.qure.memo.detail.DetailMemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class BookmarkDeleteDialogFragment : DialogFragment() {

    private var _binding: FragmentBookmarkDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var listener: OnDeleteClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnDeleteClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            com.qure.fishingspot.R.layout.fragment_bookmark_delete_dialog,
            container,
            false
        )
        setCancelable(false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels * 0.8).toInt()
        val height = ActionBar.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.buttonFragmentBookmarkDeleteDialogDeleteDelete.setOnSingleClickListener {
            listener.onDeleteClicked()
            dismiss()
        }

        binding.buttonFragmentBookmarkDeleteDialogDeleteCancel.setOnSingleClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "bookmarkDeleteDialog"
    }
}

interface OnDeleteClickListener {
    fun onDeleteClicked()
}