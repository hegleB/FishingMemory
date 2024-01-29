package com.qure.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qure.core.util.setOnSingleClickListener
import com.qure.history.databinding.DialogYearPickerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class YearPickerDialogFragment : DialogFragment() {
    private var _binding: DialogYearPickerBinding? = null
    private val binding get() = _binding!!
    private var year: Int = LocalDate.now().year
    private lateinit var yearPicker: NumberPicker
    private val viewModel by activityViewModels<HistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogYearPickerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setLayout()
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pickedYear.collect {
                    yearPicker.value = it
                }
            }
        }
    }

    private fun initView() {
        yearPicker = binding.numberPickerDialogYearPickerYear
        with(yearPicker) {
            minValue = 1000
            maxValue = 10000
            wrapSelectorWheel = false
            value = year
        }
        yearPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            viewModel.pickYear(newVal)
        }

        binding.buttonDialogYearPickerConfirm.setOnSingleClickListener {
            viewModel.onYearSelectEvent(yearPicker.value)
            dismiss()
        }

        binding.buttonDialogYearPickerCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLayout() {
        requireNotNull(dialog).apply {
            requireNotNull(window).apply {
                setLayout(
                    (resources.displayMetrics.widthPixels * 0.78).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                setBackgroundDrawableResource(com.qure.core_design.R.drawable.bg_rect_gray100_r10)
            }
        }
    }

    companion object {
        const val TAG = "YearDialogFragment"

        fun newInstance(year: Int): YearPickerDialogFragment {
            val fragment = YearPickerDialogFragment()
            return fragment
        }
    }
}
