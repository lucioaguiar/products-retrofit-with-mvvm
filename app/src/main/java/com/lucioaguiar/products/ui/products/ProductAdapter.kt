package com.lucioaguiar.products.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucioaguiar.products.data.models.Product
import com.lucioaguiar.products.databinding.ViewHolderProductBinding
import com.lucioaguiar.products.ui.products.ProductViewHolder
import com.lucioaguiar.products.util.TypesProductsActionsEnum

class ProductAdapter(
    private val onActionClicked: (TypesProductsActionsEnum, Product) -> Unit,
) : RecyclerView.Adapter<ProductViewHolder>() {

    private var list = mutableListOf<Product>()

    fun setProductList(productList: List<Product>){
        this.list = productList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(list[position], onActionClicked)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}