package com.qure.home.home.memo

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
import com.qure.home.databinding.ItemMemoBinding
import com.qure.memo.model.MemoUI

class MemoAdapter(
    private val onMemoClick: (memo: MemoUI) -> Unit,
) : ListAdapter<MemoUI, MemoAdapter.MemoViewHoler>(DIFF_UTIL) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MemoViewHoler {
        val view = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoViewHoler(
            binding = view,
            onMemoClick = onMemoClick,
        )
    }

    override fun onBindViewHolder(
        holder: MemoViewHoler,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return minOf(super.getItemCount(), 5)
    }

    inner class MemoViewHoler(
        private val binding: ItemMemoBinding,
        private val onMemoClick: (memo: MemoUI) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MemoUI) {
            val field = item
            val background = binding.imageViewItemMemoFish.background as GradientDrawable
            val radius = background.cornerRadius
            val roundedCorners = RoundedCorners(radius.toInt())

            binding.apply {
                root.setOnSingleClickListener {
                    onMemoClick.invoke(item)
                }

                Glide.with(itemView)
                    .load(field.image)
                    .apply(RequestOptions.bitmapTransform(roundedCorners))
                    .into(imageViewItemMemoFish)
                textViewItemMemoTitle.text = field.title
                textViewItemMemoName.text = field.fishType
                textViewItemMemoLocation.text = field.location
                textViewItemMemoContent.text = field.content
                textViewItemMemoCreateTime.text = field.date
            }
        }
    }

    companion object {
        private val DIFF_UTIL =
            object : DiffUtil.ItemCallback<MemoUI>() {
                override fun areItemsTheSame(
                    oldItem: MemoUI,
                    newItem: MemoUI,
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: MemoUI,
                    newItem: MemoUI,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
