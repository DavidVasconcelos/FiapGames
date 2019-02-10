package br.com.fiap.fiapmoney.model

import com.google.gson.annotations.SerializedName

data class Usuario (
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null
)