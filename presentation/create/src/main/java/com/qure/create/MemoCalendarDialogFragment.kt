package com.qure.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.qure.create.databinding.DialogMemoCalendarBinding
import java.util.Date

class MemoCalendarDialogFragment(listener: DatePickerListener) : DialogFragment() {

    private var _binding: DialogMemoCalendarBinding? = null
    private val binding get() = _binding!!

    private var dialogFragmentListener: DatePickerListener? = null

    init {
        this.dialogFragmentListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DialogMemoCalendarBinding.inflate(inflater, container, false)
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
        with(binding) {
            buttonDialogMemoCalendarCancel.setOnClickListener {
                dismiss()
            }

            buttonDialogMemoCalendarSelection.setOnClickListener {
                val selectedDate = getSelectedDate()
                dialogFragmentListener?.selectDate(selectedDate)
                dismiss()
            }
        }
    }

    private fun getSelectedDate(): String {
        val year = binding.dataPickerDialogMemoCalendar.year
        val month = "%02d".format(binding.dataPickerDialogMemoCalendar.month + 1)
        val day = "%02d".format(binding.dataPickerDialogMemoCalendar.dayOfMonth)
        val selectedDate = "${year}/${month}/${day} "
        return selectedDate
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "MemoCalendarDialogFragment"
    }

    interface DatePickerListener {
        fun selectDate(date: String)
    }
}