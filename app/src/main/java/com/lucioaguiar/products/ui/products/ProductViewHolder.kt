package com.lucioaguiar.products.ui.products

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lucioaguiar.products.R
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.databinding.ViewHolderProductBinding
import com.lucioaguiar.products.util.TypesProductsActionsEnum

class ProductViewHolder(private val binding: ViewHolderProductBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product, onActionClicked: (TypesProductsActionsEnum, Product) -> Unit){

        binding.tvName.text = product.name.toString()
        binding.tvDescription.text = product.description

        product.image?.let { image ->
            if(image.isNotBlank()){
                Glide.with(binding.root)
                    .load(image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .override(200)
                    .into(binding.ivImage)
            }
        }


        itemView.setOnClickListener {
            onActionClicked(TypesProductsActionsEnum.ITEM, product)
        }

        binding.ibtAction.setOnClickListener {
            val popup = PopupMenu(binding.root.context, binding.ibtAction)
            popup.inflate(R.menu.product_actions)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        onActionClicked(TypesProductsActionsEnum.EDIT, product)
                        true
                    }
                    R.id.action_delete -> {
                        onActionClicked(TypesProductsActionsEnum.DELETE, product)
                        true
                    }
                    else -> false
                }
            }

            popup.show()

        }

    }
}