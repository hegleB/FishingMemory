package com.qure.create.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qure.create.databinding.ItemRegionBinding

class LocationRegionAdapter(val regionNames: Array<String>) :
    RecyclerView.Adapter<LocationRegionAdapter.RegionViewHolder>() {

    private var selectedPostion = -1

    interface ItemClickListener {
        fun onClick(position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class RegionViewHolder(val binding: ItemRegionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                setSingleSelection(adapterPosition)
                itemClickListener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val binding = ItemRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return regionNames.size
    }

    override fun getItemId(position: Int): Long {
        return regionNames.get(position).hashCode().toLong()
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.binding.textViewItemRegionName.text = regionNames[position]

        if (selectedPostion == position) {
            holder.binding.imageViewItemRegionCheck.isSelected = true
            holder.binding.textViewItemRegionName.isSelected = true
        } else {
            holder.binding.imageViewItemRegionCheck.isSelected = false
            holder.binding.textViewItemRegionName.isSelected = false
        }
    }

    private fun setSingleSelection(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        notifyItemChanged(selectedPostion)
        selectedPostion = position
        notifyItemChanged(selectedPostion)
    }
}