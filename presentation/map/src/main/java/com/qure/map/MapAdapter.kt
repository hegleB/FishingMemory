package com.qure.map

import android.annotation.SuppressLint
import android.graphics.Paint
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
import com.qure.map.databinding.ItemFishingSpotBinding
import com.qure.memo.databinding.ItemMemoListBinding
import com.qure.memo.model.MemoUI
import com.qure.model.FishingSpotUI

class MapAdapter(
    private val onItemClick: (item: Any) -> Unit,
    private val onPhoneNumberClick: (phoneNumber: String) -> Unit,
) :
    ListAdapter<Any, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val VIEW_TYPE_MEMO = 1
    private val VIEW_TYPE_FISHING_SPOT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_MEMO -> {
                val binding =
                    ItemMemoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MemoViewHolder(binding, onItemClick)
            }
            VIEW_TYPE_FISHING_SPOT -> {
                val binding = ItemFishingSpotBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FishingSpotViewHolder(binding, onItemClick, onPhoneNumberClick)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder.itemViewType) {
            VIEW_TYPE_MEMO -> {
                val memoItem = item as MemoUI
                (holder as MemoViewHolder).bind(memoItem)
            }

            VIEW_TYPE_FISHING_SPOT -> {
                val spotItem = item as FishingSpotUI
                (holder as FishingSpotViewHolder).bind(spotItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is MemoUI -> VIEW_TYPE_MEMO
            is FishingSpotUI -> VIEW_TYPE_FISHING_SPOT
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    inner class MemoViewHolder(
        private val binding: ItemMemoListBinding,
        private val onMemoClick: (item: MemoUI) -> Unit
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


    inner class FishingSpotViewHolder(
        private val binding: ItemFishingSpotBinding,
        private val onSpotClick: (item: FishingSpotUI) -> Unit,
        private val onPhoneNumberClick: (phoneNumber: String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FishingSpotUI) {

            val field = item

            binding.apply {
                root.setOnSingleClickListener {
                    onSpotClick.invoke(item)
                }

                textViewItemFishingSpotPhoneNumberData.setOnSingleClickListener {
                    onPhoneNumberClick.invoke(field.phone_number)
                }

                textViewItemFishingSpotName.text = field.fishing_spot_name
                textViewItemFishingSpotNumberAddressData.text = field.number_address
                textViewItemFishingSpotRoadAddressData.text = field.road_address
                textViewItemFishingSpotFishTypeData.text = field.fish_type
                textViewItemFishingSpotGroundType.text = field.fishing_ground_type
                textViewItemFishingSpotPhoneNumberData.text = field.phone_number
                textViewItemFishingSpotMainPointData.text = field.main_point
                textViewItemFishingSpotFeeData.text = field.fee
                textViewItemFishingSpotPhoneNumberData.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                val diffOldItem = when (oldItem) {
                    is MemoUI -> oldItem.name
                    is FishingSpotUI -> oldItem.number
                    else -> oldItem
                }
                val diffNewItem = when (newItem) {
                    is MemoUI -> newItem.name
                    is FishingSpotUI -> newItem.number
                    else -> newItem
                }
                return diffOldItem == diffNewItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }
        }
    }
}