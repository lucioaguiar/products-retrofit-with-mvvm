package com.lucioaguiar.products.data.models

data class SessionJWT(
        var status: String,
        var user: User,
        var authorisation: Authorisation)