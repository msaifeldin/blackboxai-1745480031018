package com.yourdomain.ecommerceapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yourdomain.ecommerceapp.data.model.Cart
import com.yourdomain.ecommerceapp.data.model.Product
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    private val _cart = MutableStateFlow(Cart())
    val cart: Flow<Cart> = _cart.asStateFlow()

    suspend fun loadCart() {
        try {
            val userId = auth.currentUser?.uid ?: return
            val cartDoc = firestore.collection(Cart.COLLECTION_NAME)
                .document(userId)
                .get()
                .await()

            cartDoc.toObject(Cart::class.java)?.let { loadedCart ->
                _cart.value = loadedCart
            }
        } catch (e: Exception) {
            // Handle error, possibly rethrow or log
            throw e
        }
    }

    suspend fun addToCart(product: Product, quantity: Int = 1) {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            
            // Update local cart
            val updatedCart = _cart.value.copy()
            updatedCart.addItem(product, quantity)
            _cart.value = updatedCart

            // Update Firestore
            firestore.collection(Cart.COLLECTION_NAME)
                .document(userId)
                .set(updatedCart)
                .await()

        } catch (e: Exception) {
            // Handle error, possibly rethrow or log
            throw e
        }
    }

    suspend fun removeFromCart(productId: String) {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            
            // Update local cart
            val updatedCart = _cart.value.copy()
            updatedCart.removeItem(productId)
            _cart.value = updatedCart

            // Update Firestore
            firestore.collection(Cart.COLLECTION_NAME)
                .document(userId)
                .set(updatedCart)
                .await()

        } catch (e: Exception) {
            // Handle error, possibly rethrow or log
            throw e
        }
    }

    suspend fun updateQuantity(productId: String, quantity: Int) {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            
            // Update local cart
            val updatedCart = _cart.value.copy()
            updatedCart.updateQuantity(productId, quantity)
            _cart.value = updatedCart

            // Update Firestore
            firestore.collection(Cart.COLLECTION_NAME)
                .document(userId)
                .set(updatedCart)
                .await()

        } catch (e: Exception) {
            // Handle error, possibly rethrow or log
            throw e
        }
    }

    suspend fun clearCart() {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            
            // Update local cart
            _cart.value = Cart(userId = userId)

            // Update Firestore
            firestore.collection(Cart.COLLECTION_NAME)
                .document(userId)
                .delete()
                .await()

        } catch (e: Exception) {
            // Handle error, possibly rethrow or log
            throw e
        }
    }

    companion object {
        @Volatile
        private var instance: CartRepository? = null

        fun getInstance(): CartRepository {
            return instance ?: synchronized(this) {
                instance ?: CartRepository().also { instance = it }
            }
        }
    }
}
