package com.lucioaguiar.products.data.models

data class Product(
        var id: Int,
        var name: String? = null,
        var image: String? = null,
        var description: String? = null)