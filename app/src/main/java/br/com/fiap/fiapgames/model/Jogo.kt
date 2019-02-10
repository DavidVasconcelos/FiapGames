package br.com.fiap.fiapgames.model

import com.google.gson.annotations.SerializedName

data class Jogo(

    @SerializedName("id")
    var id: String? = null,
    @SerializedName("titulo")
    var titulo: String? = null,
    @SerializedName("plataforma")
    var plataforma: String? = null
)