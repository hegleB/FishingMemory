package com.qure.home.home.memo

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.qure.domain.entity.memo.Memo
import com.qure.home.databinding.ItemMemoBinding


class MemoAdapter(

) : ListAdapter<Memo, MemoAdapter.MemoViewHoler>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHoler {
        val view = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoViewHoler(view)
    }

    override fun onBindViewHolder(holder: MemoViewHoler, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return minOf(super.getItemCount(), 5)
    }

    inner class MemoViewHoler(val binding: ItemMemoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Memo) {
            val field = item.fields!!.fields
            val background = binding.imageViewItemMemoFish.background as GradientDrawable
            val radius = background.cornerRadius
            val roundedCorners = RoundedCorners(radius.toInt())
            binding.apply {
                Glide.with(itemView)
                    .load(field.image.stringValue)
                    .apply(RequestOptions.bitmapTransform(roundedCorners))
                    .into(imageViewItemMemoFish)
                textViewItemMemoTitle.text = field.title.stringValue
                textViewItemMemoName.text = field.fishType.stringValue
                textViewItemMemoLocation.text = field.location.stringValue
                textViewItemMemoContent.text = field.content.stringValue
                textViewItemMemoCreateTime.text = field.date.stringValue
            }
        }
    }

    companion object {
        private const val ITEM_COUNT = 5
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Memo>() {
            override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
                return oldItem == newItem
            }
        }
    }
}