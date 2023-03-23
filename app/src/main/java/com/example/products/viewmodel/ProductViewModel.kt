package com.example.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.products.model.ProductsItem
import com.example.products.model.Resource
import com.example.products.model.Response
import com.example.products.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    val repository: MainRepository
): ViewModel() {

    sealed class ProductEvent{
        class Success(val result: List<ProductsItem?>?):ProductEvent()
        class Failure(val errorText:String):ProductEvent()
        object Loading:ProductEvent()
        object Empty:ProductEvent()
    }

    private val _conversion = MutableStateFlow<ProductEvent>(ProductEvent.Empty)
    val conversion: StateFlow<ProductEvent> = _conversion

    fun getProductViewModel(){
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = ProductEvent.Loading
            when(val quotesResponse = repository.getProductData()) {
                is Resource.Error<*> -> _conversion.value = ProductEvent.Failure(quotesResponse.message!!)
                is Resource.Success<*> -> {
                    val quote = quotesResponse.data!!.products
                    if(quote == null) {
                        _conversion.value = ProductEvent.Failure("Unexpected error")
                    } else {
                        _conversion.value = ProductEvent.Success(
                            quote
                        )
                    }
                }
            }
        }
    }

}