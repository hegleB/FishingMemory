package com.qure.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qure.core.util.setOnSingleClickListener
import com.qure.gallery.databinding.ItemGalleryCameraBinding
import com.qure.gallery.databinding.ItemGalleryImageBinding
import java.io.File

class GalleryAdapter(
    private val context: Context,
    private val images: List<GalleryImage>,
    private val setOnItemClickListener: OnItemClickListener,
) : ListAdapter<GalleryImage, RecyclerView.ViewHolder>(DIFF_UTIL) {
    private val VIEW_TYPE_CAMERA = 1
    private val VIEW_TYPE_IMAGE = 2
    private var preSelectedItem: String? = null
    private var preGroup: Group? = null
    private var preCheckBox: CheckBox? = null

    inner class CarmeraViewHoler(private val binding: ItemGalleryCameraBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnSingleClickListener {
                setOnItemClickListener.setOnItemClickListener()
            }
        }
    }

    inner class ImageViewHoler(private val binding: ItemGalleryImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: GalleryImage) {
            Glide.with(binding.root)
                .load(File(item.path))
                .into(binding.imageViewItemGalleryImage)

            binding.groupItemGalleryImage.setOnSingleClickListener {
                if (preSelectedItem != item.path) {
                    preCheckBox?.isChecked = false
                    preGroup?.background = null
                }
                binding.groupItemGalleryImage.background =
                    if (binding.groupItemGalleryImage.background == null) {
                        context.getDrawable(com.qure.core_design.R.drawable.bg_rect_blue500_outline)
                    } else {
                        null
                    }
                binding.checkBoxItemGalleryImage.isChecked =
                    !binding.checkBoxItemGalleryImage.isChecked
                preSelectedItem = item.path
                preGroup = binding.groupItemGalleryImage
                preCheckBox = binding.checkBoxItemGalleryImage
                setOnItemClickListener.setOnItemClickListener(Uri.fromFile(File(item.path)))
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CAMERA -> {
                CarmeraViewHoler(
                    ItemGalleryCameraBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }
            VIEW_TYPE_IMAGE -> {
                ImageViewHoler(
                    ItemGalleryImageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    ),
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        if (getItemViewType(position) == VIEW_TYPE_IMAGE) {
            (holder as ImageViewHoler).bind(images[position - 1])
        }
    }

    override fun getItemCount(): Int {
        return images.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_CAMERA else VIEW_TYPE_IMAGE
    }

    companion object {
        private val DIFF_UTIL =
            object : DiffUtil.ItemCallback<GalleryImage>() {
                override fun areItemsTheSame(
                    oldItem: GalleryImage,
                    newItem: GalleryImage,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: GalleryImage,
                    newItem: GalleryImage,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    interface OnItemClickListener {
        fun setOnItemClickListener(uri: Uri)

        fun setOnItemClickListener()
    }
}
