package com.example.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.products.databinding.AdapterProductBinding
import com.example.products.model.ProductsItem

class ProductAdapter: RecyclerView.Adapter<MainViewHolder>() {
    private var productList = mutableListOf<ProductsItem?>()

    fun setProductList(products: List<ProductsItem?>) {
        productList = products.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterProductBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
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

}
class MainViewHolder(val binding: AdapterProductBinding) : RecyclerView.ViewHolder(binding.root) {
}