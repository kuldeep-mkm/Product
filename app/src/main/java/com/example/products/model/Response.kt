package com.example.products.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


data class Response(
	val total: Int? = null,
	val limit: Int? = null,
	val skip: Int? = null,
	val products: List<ProductsItem?>? = null
)

@Parcelize
data class ProductsItem(
	val discountPercentage: @RawValue Any? = null,
	val thumbnail: @RawValue String? = null,
	val images: @RawValue ArrayList<String?>? = null,
	val price: @RawValue Int? = null,
	val rating: @RawValue Float? = null,
	val description: @RawValue String? = null,
	val id: @RawValue Int? = null,
	val title: @RawValue String? = null,
	val stock: @RawValue Int? = null,
	val category: @RawValue String? = null,
	val brand: @RawValue String? = null
) : Parcelable {



}


