package com.qure.memo

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
import com.qure.memo.databinding.ItemMemoListBinding
import com.qure.memo.model.MemoUI

class MemoListAdapter(private val onMemoClick: (memo: MemoUI) -> Unit) :
    ListAdapter<MemoUI, MemoListAdapter.MemoViewHoler>(DIFF_UTIL) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MemoViewHoler {
        val view = ItemMemoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        return super.getItemCount()
    }

    inner class MemoViewHoler(
        private val binding: ItemMemoListBinding,
        private val onMemoClick: (memo: MemoUI) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MemoUI) {
            val field = item
            val background = binding.imageViewItemMemoListFish.background as GradientDrawable
            val radius = background.cornerRadius
            val roundedCorners = RoundedCorners(radius.toInt())
            binding.apply {
                root.setOnSingleClickListener {
                    onMemoClick.invoke(item)
                }

                Glide.with(itemView)
                    .load(field.image)
                    .apply(RequestOptions.bitmapTransform(roundedCorners))
                    .into(imageViewItemMemoListFish)
                textViewItemMemoListTitle.text = field.title
                textViewItemMemoListName.text = field.fishType
                textViewItemMemoListLocation.text = field.location
                textViewItemMemoListContent.text = field.content
                textViewItemMemoListCreateTime.text = field.date
            }
        }
    }

    companion object {
        private const val ITEM_COUNT = 5
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
