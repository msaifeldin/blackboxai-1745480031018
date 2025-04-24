package com.yourdomain.ecommerceapp.data.model

data class Cart(
    val userId: String = "",
    val items: MutableList<CartItem> = mutableListOf(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val totalItems: Int
        get() = items.sumOf { it.quantity }

    val subtotal: Double
        get() = items.sumOf { it.product.displayPrice * it.quantity }

    fun addItem(product: Product, quantity: Int = 1) {
        val existingItem = items.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            items.add(CartItem(product, quantity))
        }
    }

    fun removeItem(productId: String) {
        items.removeIf { it.product.id == productId }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        items.find { it.product.id == productId }?.let { item ->
            if (quantity <= 0) {
                removeItem(productId)
            } else {
                item.quantity = quantity
            }
        }
    }

    fun clear() {
        items.clear()
    }

    companion object {
        const val COLLECTION_NAME = "carts"
    }
}

data class CartItem(
    val product: Product,
    var quantity: Int = 1
) {
    val total: Double
        get() = product.displayPrice * quantity
}
