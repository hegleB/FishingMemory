package com.qure.fishingspot.bookmark

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.qure.core.util.setOnSingleClickListener
import com.qure.fishingspot.databinding.ItemBookmarkBinding
import com.qure.model.FishingSpotUI

class BookmarkAdatper(
    private val onItemClick: (item: FishingSpotUI) -> Unit,
    private val onPhoneNumberClick: (phoneNumber: String) -> Unit,
) : ListAdapter<FishingSpotUI, RecyclerView.ViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FishingSpotViewHolder(binding, onItemClick, onPhoneNumberClick)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        val spotItem = item as FishingSpotUI
        (holder as FishingSpotViewHolder).bind(spotItem)
    }

    inner class FishingSpotViewHolder(
        private val binding: ItemBookmarkBinding,
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
        private val DIFF_UTIL =
            object : DiffUtil.ItemCallback<FishingSpotUI>() {
                override fun areItemsTheSame(
                    oldItem: FishingSpotUI,
                    newItem: FishingSpotUI,
                ): Boolean {
                    return oldItem.number == newItem.number
                }

                override fun areContentsTheSame(
                    oldItem: FishingSpotUI,
                    newItem: FishingSpotUI,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
