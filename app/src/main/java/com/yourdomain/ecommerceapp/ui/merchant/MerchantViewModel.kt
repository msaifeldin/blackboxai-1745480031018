package com.yourdomain.ecommerceapp.ui.merchant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yourdomain.ecommerceapp.data.model.Product
import com.yourdomain.ecommerceapp.data.repository.ProductRepository
import com.yourdomain.ecommerceapp.ui.common.BaseViewModel
import kotlinx.coroutines.launch

class MerchantViewModel : BaseViewModel() {

    private val productRepository = ProductRepository.getInstance()

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                // Assuming a method to get products for the merchant exists
                val merchantProducts = productRepository.getMerchantProducts()
                _products.postValue(merchantProducts)
                clearError()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                productRepository.deleteProduct(productId)
                loadProducts()
                clearError()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun retryLoading() {
        loadProducts()
    }
}
