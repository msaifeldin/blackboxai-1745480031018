package com.yourdomain.ecommerceapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val discountedPrice: Double? = null,
    val imageUrl: String = "",
    val category: String = "",
    val merchantId: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val stockQuantity: Int = 0,
    val tags: List<String> = listOf(),
    val featured: Boolean = false,
    val newArrival: Boolean = false,
    val bestSeller: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable {
    
    val hasDiscount: Boolean
        get() = discountedPrice != null && discountedPrice < price

    val displayPrice: Double
        get() = discountedPrice ?: price

    val discountPercentage: Int
        get() = if (hasDiscount && price > 0) {
            ((price - (discountedPrice ?: price)) / price * 100).toInt()
        } else 0

    val isInStock: Boolean
        get() = stockQuantity > 0

    companion object {
        const val COLLECTION_NAME = "products"
    }
}
