package com.example.products.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.products.model.ProductsItem

class AddProductViewModel : ViewModel() {

    var lst = MutableLiveData<ArrayList<ProductsItem>>()
    var newlist = arrayListOf<ProductsItem>()

    fun add(blog: ProductsItem){
        newlist.add(blog)
        lst.value=newlist
    }

}