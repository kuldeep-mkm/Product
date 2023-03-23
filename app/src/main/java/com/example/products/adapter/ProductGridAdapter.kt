package com.example.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.products.databinding.AdapterGridProductBinding

import com.example.products.model.ProductsItem

class ProductGridAdapter: RecyclerView.Adapter<MainGridViewHolder>() {
    private var productList = mutableListOf<ProductsItem?>()

    fun setProductList(products: List<ProductsItem?>) {
        productList = products.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainGridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterGridProductBinding.inflate(inflater, parent, false)
        return MainGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainGridViewHolder, position: Int) {
        val productData = productList[position]
        holder.binding.model = productData
        if (productData?.thumbnail != null) {
            Glide.with(holder.itemView.context).load(productData.thumbnail)
                .into(holder.binding.imageview)
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProductData(products: ProductsItem) {
        productList.add(products)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}

class MainGridViewHolder(val binding: AdapterGridProductBinding) : RecyclerView.ViewHolder(binding.root) {
}