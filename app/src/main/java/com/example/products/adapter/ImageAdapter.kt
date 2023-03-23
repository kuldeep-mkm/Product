package com.example.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.products.databinding.AdapterImagesBinding
import java.io.File

class ImageAdapter: RecyclerView.Adapter<ImageViewHolder>() {
    private var imageList = mutableListOf<String?>()

    fun setImageList(images: List<String?>) {
        imageList = images.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterImagesBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageData = imageList[position]

        Glide.with(holder.itemView.context).load(imageData)
                .into(holder.binding.imgView)

    }
    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setProductData(products: String) {
        imageList.add(products)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
class ImageViewHolder(val binding: AdapterImagesBinding) : RecyclerView.ViewHolder(binding.root) {
}