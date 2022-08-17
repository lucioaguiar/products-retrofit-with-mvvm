package com.lucioaguiar.products.ui.products


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.lucioaguiar.products.R
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.databinding.FragmentCreateEditProductBinding
import com.lucioaguiar.products.ui.auth.AuthViewModel
import com.lucioaguiar.products.util.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateEditProductFragment : Fragment() {

    val authViewModel: AuthViewModel by viewModel()
    val viewModel: ProductViewModel by viewModel()

    lateinit var product: Product
    var remoteImage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreateEditProductBinding.inflate(layoutInflater)
        context ?: return binding.root

        var typeAction = TypesActionsEnum.CREATE

        arguments?.getParcelable<Product>(EntityNamesConstants.PRODUCT)?.let { product ->
            this.product = product
            remoteImage = product.image
            typeAction = TypesActionsEnum.EDIT
            updateUi(binding)
        }

        val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data

                uri?.let {
                    loadImage(it, binding)
                }

                val iStream = uri?.let { it1 ->
                    requireActivity().contentResolver.openInputStream(it1)
                }
                iStream?.let {
                    Log.i("retorno", "parou aqui")
                    val imageBody = RequestBody.create(MediaType.parse("image/*"), it.readBytes())
                    val image = MultipartBody.Part.createFormData("image", "image", imageBody)

                    authViewModel.getSession { sessionJWT ->
                        if(sessionJWT != null) {
                            viewModel.upload(image, sessionJWT)
                        }
                    }
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                binding.pbImage.hide()
                toast(ImagePicker.getError(data))
            } else {
                binding.pbImage.hide()
            }
        }

        addEvents(startForProfileImageResult, binding)
        subscribeUi(binding)

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            authViewModel.getSession { sessionJWT ->
                if (validation(binding)) {
                    if(sessionJWT != null) {

                        if (typeAction == TypesActionsEnum.CREATE)
                            viewModel.storeProduct(
                                binding.edName.text.toString(),
                                binding.edDescription.text.toString(),
                                remoteImage,
                                sessionJWT
                            )
                        else if (typeAction == TypesActionsEnum.EDIT) {
                            viewModel.updateProduct(
                                product.id,
                                binding.edName.text.toString(),
                                binding.edDescription.text.toString(),
                                remoteImage,
                                sessionJWT
                            )
                        }

                    }
                }
            }
        }

        return binding.root
    }

    private fun subscribeUi(binding: FragmentCreateEditProductBinding) {

        viewModel.store.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    findNavController().popBackStack()
                }
            }
        })

        viewModel.update.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    findNavController().popBackStack()
                }
            }
        })

        viewModel.upload.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is UiState.Loading -> {
                    binding.pbImage.show()
                }
                is UiState.Failure -> {
                    toast(state.error)
                    binding.pbImage.hide()
                }
                is UiState.Success -> {
                    remoteImage = state.data.url
                    binding.pbImage.hide()
                }
            }
        })

    }

    private fun loadImage(uri: Uri, binding: FragmentCreateEditProductBinding) {
        Glide.with(binding.root)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .override(200)
            .into(binding.ivImage)
    }

    private fun addEvents(
        startForProfileImageResult: ActivityResultLauncher<Intent>,
        binding: FragmentCreateEditProductBinding
    ) {

        binding.ivImage.setOnClickListener {
            binding.pbImage.show()
            ImagePicker.with(this)
                .compress(50)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

    }

    private fun validation(binding: FragmentCreateEditProductBinding): Boolean {
        var isValid = true

        binding.tilName.error = ""
        binding.tilDescription.error = ""

        if (binding.edName.text.isNullOrEmpty()){
            isValid = false
            binding.tilName.error = getString(R.string.required_field)
        } else if (binding.edName.text.length > 15){
            isValid = false
            binding.tilName.error = getString(R.string.limit_max_of_caracter, 15)
        } else {
            binding.tilName.error = ""
        }

        if (binding.edDescription.text.isNullOrEmpty()){
            isValid = false
            binding.tilDescription.error = getString(R.string.required_field)
        } else if (binding.edDescription.text.length > 170){
            isValid = false
            binding.tilDescription.error = getString(R.string.limit_max_of_caracter, 170)
        } else {
            binding.tilDescription.error = ""
        }
        return isValid
    }

    private fun updateUi(binding: FragmentCreateEditProductBinding) {
        binding.edName.setText(product.name.toString())
        binding.edDescription.setText(product.description)
        if(remoteImage != null) {
            Glide.with(binding.root)
                .load(remoteImage)
                .transition(DrawableTransitionOptions.withCrossFade())
                .override(200)
                .into(binding.ivImage)
        }
    }


}