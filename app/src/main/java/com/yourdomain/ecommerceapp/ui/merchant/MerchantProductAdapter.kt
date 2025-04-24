package com.yourdomain.ecommerceapp.ui.merchant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yourdomain.ecommerceapp.R
import com.yourdomain.ecommerceapp.data.model.Product
import com.yourdomain.ecommerceapp.databinding.ItemMerchantProductBinding
import java.text.NumberFormat
import java.util.Locale

class MerchantProductAdapter(
    private val onEditProduct: (String) -> Unit,
    private val onDeleteProduct: (String) -> Unit
) : ListAdapter<Product, MerchantProductAdapter.MerchantProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantProductViewHolder {
        val binding = ItemMerchantProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MerchantProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MerchantProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MerchantProductViewHolder(
        private val binding: ItemMerchantProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.editButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditProduct(getItem(position).id)
                }
            }

            binding.deleteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteProduct(getItem(position).id)
                }
            }
        }

        fun bind(product: Product) {
            binding.apply {
                Glide.with(productImage)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.placeholder_product)
                    .error(R.drawable.placeholder_product)
                    .centerCrop()
                    .into(productImage)

                productName.text = product.name

                val formattedPrice = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(product.displayPrice)
                productPrice.text = formattedPrice
            }
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
