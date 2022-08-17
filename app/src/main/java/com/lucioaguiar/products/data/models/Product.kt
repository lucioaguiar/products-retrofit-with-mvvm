package com.lucioaguiar.products.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
        var id: Int,
        var name: String? = null,
        var image: String? = null,
        var description: String? = null
): Parcelable