package com.yourdomain.ecommerceapp.ui.merchant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourdomain.ecommerceapp.databinding.FragmentMerchantBinding
import com.yourdomain.ecommerceapp.ui.common.BaseFragment

class MerchantFragment : BaseFragment() {

    private var _binding: FragmentMerchantBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MerchantViewModel by viewModels()

    private val productAdapter by lazy {
        MerchantProductAdapter(
            onEditProduct = { productId ->
                // Handle edit product action
            },
            onDeleteProduct = { productId ->
                viewModel.deleteProduct(productId)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupViews() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }

        binding.addProductButton.setOnClickListener {
            // Navigate to add product screen
        }
    }

    override fun setupObservers() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
            binding.emptyView.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it) {
                    viewModel.retryLoading()
                }
                viewModel.clearError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
