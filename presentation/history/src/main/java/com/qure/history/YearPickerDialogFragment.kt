package com.qure.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.qure.core.util.setOnSingleClickListener
import com.qure.history.databinding.DialogYearPickerBinding
import java.time.LocalDate

class YearPickerDialogFragment(listener: YearDialogListner) : DialogFragment() {

    private var _binding: DialogYearPickerBinding? = null
    private val binding get() = _binding!!
    private var year: Int = LocalDate.now().year
    private var dialogFragmentListener: YearDialogListner? = null

    init {
        this.dialogFragmentListener = listener
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setLayout()
    }

    private fun initView() {
        with(binding.numberPickerDialogYearPickerYear) {
            minValue = 1000
            maxValue = 10000
            wrapSelectorWheel = false
            value = year

            binding.buttonDialogYearPickerConfirm.setOnSingleClickListener {
                dialogFragmentListener?.selectYearClick(value)
                dismiss()
            }
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
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawableResource(com.qure.core_design.R.drawable.bg_rect_gray100_r10)
            }
        }
    }

    companion object {
        const val TAG = "YearDialogFragment"

        fun newInstance(year: Int, listener: YearDialogListner): YearPickerDialogFragment {
            val fragment = YearPickerDialogFragment(listener)
            fragment.year = year
            return fragment
        }
    }

    interface YearDialogListner {
        fun selectYearClick(year: Int)
    }
}