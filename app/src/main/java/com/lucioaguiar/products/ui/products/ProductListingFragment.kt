package com.lucioaguiar.products.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lucioaguiar.products.R
import com.lucioaguiar.products.databinding.FragmentProductListingBinding
import com.lucioaguiar.products.databinding.FragmentRegisterBinding
import com.lucioaguiar.products.ui.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductListingFragment : Fragment() {

    val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProductListingBinding.inflate(layoutInflater)
        context ?: return binding.root

        addEvents(binding)

        return binding.root
    }

    private fun addEvents(binding: FragmentProductListingBinding) {
        binding.ibtLogout.setOnClickListener {

            viewModel.getSession { sessionJWT ->
                if(sessionJWT != null){
                    viewModel.logout (sessionJWT) {
                        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                    }
                }
            }

        }
    }

}