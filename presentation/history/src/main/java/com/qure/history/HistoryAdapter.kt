package com.qure.history

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.qure.core.util.setOnSingleClickListener
import com.qure.history.databinding.ItemCalendarMemoBinding
import com.qure.memo.model.MemoUI

class HistoryAdapter(private val onMemoClick: (memo: MemoUI) -> Unit) :
    ListAdapter<MemoUI, HistoryAdapter.CalendarMemoViewHoler>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarMemoViewHoler {
        val view =
            ItemCalendarMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarMemoViewHoler(
            binding = view,
            onMemoClick = onMemoClick,
        )
    }

    override fun onBindViewHolder(holder: CalendarMemoViewHoler, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    inner class CalendarMemoViewHoler(
        private val binding: ItemCalendarMemoBinding,
        private val onMemoClick: (memo: MemoUI) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MemoUI) {
            val field = item
            val background = binding.imageViewItemCalendarMemoFish.background as GradientDrawable
            val radius = background.cornerRadius
            val roundedCorners = RoundedCorners(radius.toInt())
            binding.apply {
                root.setOnSingleClickListener {
                    onMemoClick.invoke(item)
                }
                textViewItemCalendarMemoTitle.text = field.title
                textViewItemCalendarMemoLocation.text = field.location
                textViewItemCalendarMemoName.text = field.fishType
                textViewItemCalendarMemoContent.text = field.content
                textViewItemCalendarMemoCreateTime.text = field.date
                Glide.with(itemView)
                    .load(field.image)
                    .apply(RequestOptions.bitmapTransform(roundedCorners))
                    .into(imageViewItemCalendarMemoFish)

            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<MemoUI>() {
            override fun areItemsTheSame(oldItem: MemoUI, newItem: MemoUI): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: MemoUI, newItem: MemoUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}