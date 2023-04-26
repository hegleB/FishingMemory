package com.qure.memo.delete

import android.app.ActionBar
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
import com.qure.memo.R
import com.qure.memo.databinding.FragmentDeleteDialogBinding
import com.qure.memo.detail.DetailMemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeleteDialogFragment(uuid: String) : DialogFragment() {

    private var _binding: FragmentDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailMemoViewModel>()
    private var uuid: String

    init {
        this.uuid = uuid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_delete_dialog,
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
        observe()
    }

    private fun initView() {
        binding.buttonFragmentDialogDeleteDelete.setOnSingleClickListener {
            viewModel.deleteMemo(uuid)
        }

        binding.buttonFragmentDialogDeleteCancel.setOnSingleClickListener {
            dismiss()
        }
    }

    private fun observe() {
        viewModel.error
            .onEach { errorMessage -> FishingMemoryToast().error(requireContext(), errorMessage) }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect {
                        if (it.isDeleteInitialized) {
                            FishingMemoryToast().show(requireContext(), it.deleteSuccessMessage)
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "DeleteDialogFragment"
        fun newInstance(uuid: String): DeleteDialogFragment {
            return DeleteDialogFragment(uuid)
        }
    }
}