package com.yourdomain.ecommerceapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yourdomain.ecommerceapp.data.Result
import com.yourdomain.ecommerceapp.data.model.Product
import com.yourdomain.ecommerceapp.data.safeCall
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getFeaturedProducts(limit: Int = 10): Result<List<Product>> = safeCall {
        firestore.collection(Product.COLLECTION_NAME)
            .whereEqualTo("featured", true)
            .limit(limit.toLong())
            .get()
            .await()
            .toObjects(Product::class.java)
    }

    suspend fun getNewArrivals(limit: Int = 10): Result<List<Product>> = safeCall {
        firestore.collection(Product.COLLECTION_NAME)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .await()
            .toObjects(Product::class.java)
    }

    suspend fun getBestSellers(minRating: Double = 4.0, limit: Int = 10): Result<List<Product>> = safeCall {
        firestore.collection(Product.COLLECTION_NAME)
            .whereGreaterThan("rating", minRating)
            .orderBy("rating", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .await()
            .toObjects(Product::class.java)
    }

    suspend fun getProductsByCategory(category: String, limit: Int = 20): Result<List<Product>> = safeCall {
        firestore.collection(Product.COLLECTION_NAME)
            .whereEqualTo("category", category)
            .limit(limit.toLong())
            .get()
            .await()
            .toObjects(Product::class.java)
    }

    suspend fun searchProducts(query: String, limit: Int = 20): Result<List<Product>> = safeCall {
        firestore.collection(Product.COLLECTION_NAME)
            .orderBy("name")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .limit(limit.toLong())
            .get()
            .await()
            .toObjects(Product::class.java)
    }

    suspend fun getProductById(productId: String): Result<Product?> = safeCall {
        firestore.collection(Product.COLLECTION_NAME)
            .document(productId)
            .get()
            .await()
            .toObject(Product::class.java)
    }

    companion object {
        @Volatile
        private var instance: ProductRepository? = null

        fun getInstance(): ProductRepository {
            return instance ?: synchronized(this) {
                instance ?: ProductRepository().also { instance = it }
            }
        }
    }
}
