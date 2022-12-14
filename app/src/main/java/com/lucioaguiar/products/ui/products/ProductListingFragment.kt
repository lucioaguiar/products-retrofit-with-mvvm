package com.lucioaguiar.products.ui.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lucioaguiar.products.R
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.databinding.FragmentProductListingBinding
import com.lucioaguiar.products.ui.auth.AuthViewModel
import com.lucioaguiar.products.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductListingFragment : Fragment() {

    val authViewModel: AuthViewModel by viewModel()
    val viewModel: ProductViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProductListingBinding.inflate(layoutInflater)
        context ?: return binding.root

        val adapter = ProductAdapter({ action, product -> click(action, product)})
        binding.rvProductList.adapter = adapter

        addEvents(binding)
        subscribeUi(adapter, binding)

        return binding.root
    }

    private fun click(action: TypesProductsActionsEnum, product: Product){
        when (action) {
            TypesProductsActionsEnum.ITEM -> {
                editProduct(product)
            }
            TypesProductsActionsEnum.EDIT -> {
                editProduct(product)
            }
            TypesProductsActionsEnum.DELETE -> {
                authViewModel.getSession { sessionJWT ->
                    if(sessionJWT != null){
                        viewModel.delete(product.id, sessionJWT)
                    }
                }
            }
        }
    }

    private fun editProduct(product: Product) {
        val bundle = bundleOf(EntityNamesConstants.PRODUCT to product)
        findNavController().navigate(R.id.action_productListingFragment_to_createEditProductFragment, bundle)
    }

    private fun subscribeUi(adapter: ProductAdapter, binding: FragmentProductListingBinding) {
        viewModel.products.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    state.data.let { list ->
                        if (list.size > 0) {
                            binding.rvProductList.show()
                            binding.llNoProduct.hide()
                            adapter.setProductList(list)
                        }else{
                            binding.rvProductList.hide()
                            binding.llNoProduct.show()
                        }
                    }
                }
            }
        })

        viewModel.delete.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    state.data.let { product ->
                        refreshProducts()
                    }
                }
            }
        })

    }

    private fun addEvents(binding: FragmentProductListingBinding) {
        binding.ibtLogout.setOnClickListener {
            authViewModel.getSession { sessionJWT ->
                if(sessionJWT != null){
                    authViewModel.logout (sessionJWT) {
                        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                    }
                }
            }
        }
        binding.ibtCreateNewProduct.setOnClickListener {
            createProduct()
        }

        binding.btCreateProductNow.setOnClickListener {
            createProduct()
        }
    }

    private fun createProduct() {
        findNavController().navigate(R.id.action_productListingFragment_to_createEditProductFragment)
    }

    private fun refreshProducts(){
        authViewModel.getSession { sessionJWT ->
            if (sessionJWT != null) {
                viewModel.getProducts(sessionJWT)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshProducts()
    }

}