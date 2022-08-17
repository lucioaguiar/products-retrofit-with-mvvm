package com.lucioaguiar.products.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lucioaguiar.products.R
import com.lucioaguiar.products.databinding.FragmentRegisterBinding
import com.lucioaguiar.products.util.UiState
import com.lucioaguiar.products.util.hide
import com.lucioaguiar.products.util.show
import com.lucioaguiar.products.util.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment() {

    val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegisterBinding.inflate(layoutInflater)
        context ?: return binding.root

        addEvents(binding)
        subscribeUi(binding)

        return binding.root
    }

    private fun subscribeUi(binding: FragmentRegisterBinding) {
        viewModel.register.observe(viewLifecycleOwner, Observer { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.pbRegister.show()
                    binding.btRegister.text = ""
                }
                is UiState.Failure -> {
                    binding.pbRegister.hide()
                    binding.btRegister.text = getString(R.string.login)
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pbRegister.hide()
                    binding.btRegister.text = getString(R.string.login)
                    toast(state.data)
                    findNavController().navigate(R.id.action_registerFragment_to_home_navigation)
                }
            }
        })
    }

    private fun addEvents(binding: FragmentRegisterBinding) {
        binding.btRegister.setOnClickListener{
            if (validation(binding)) {
                viewModel.register(
                    binding.edName.text.toString(),
                    binding.edEmail.text.toString(),
                    binding.edPassword.text.toString()
                )
            }
        }
    }

    fun validation(binding: FragmentRegisterBinding): Boolean {
        var isValid = true

        binding.tilName.error = ""
        binding.edEmail.error = ""
        binding.tilPassword.error = ""

        if (binding.edName.text.isNullOrEmpty()){
            isValid = false
            binding.tilName.error = getString(R.string.required_field)
        }else{
            binding.tilName.error = ""
        }

        if (binding.edEmail.text.isNullOrEmpty()){
            isValid = false
            binding.tilEmail.error = getString(R.string.required_field)
        }else{
            binding.tilEmail.error = ""
        }
        if (binding.edPassword.text.isNullOrEmpty()){
            isValid = false
            binding.tilPassword.error = getString(R.string.required_field)
        }else{
            binding.tilPassword.error = ""
        }
        return isValid
    }


}