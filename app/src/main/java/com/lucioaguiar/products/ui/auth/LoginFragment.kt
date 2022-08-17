package com.lucioaguiar.products.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lucioaguiar.products.R
import com.lucioaguiar.products.databinding.FragmentLoginBinding
import com.lucioaguiar.products.util.UiState
import com.lucioaguiar.products.util.hide
import com.lucioaguiar.products.util.show
import com.lucioaguiar.products.util.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(layoutInflater)
        context ?: return binding.root

        addEvents(binding)
        subscribeUi(binding)

        return binding.root
    }

    private fun subscribeUi(binding: FragmentLoginBinding) {
        viewModel.login.observe(viewLifecycleOwner, Observer { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.pbLogin.show()
                    binding.btLogin.text = ""
                }
                is UiState.Failure -> {
                    binding.pbLogin.hide()
                    binding.btLogin.text = getString(R.string.login)
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.pbLogin.hide()
                    binding.btLogin.text = getString(R.string.login)
                    toast(state.data)
                    findNavController().navigate(R.id.action_loginFragment_to_home_navigation)
                }
            }
        })
    }

    private fun addEvents(binding: FragmentLoginBinding) {
        binding.tvRegisterNow.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btLogin.setOnClickListener {
            if(validation(binding)) {
                viewModel.login(
                    binding.edEmail.text.toString(),
                    binding.edPassword.text.toString()
                )
            }
        }
    }

    fun validation(binding: FragmentLoginBinding): Boolean {
        var isValid = true

        binding.tilEmail.error = ""
        binding.tilPassword.error = ""

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

    override fun onStart() {
        super.onStart()
        viewModel.getSession { sessionJWT ->
            if (sessionJWT != null){
                findNavController().navigate(R.id.action_loginFragment_to_home_navigation)
            }
        }
    }

}