package br.com.fiap.fiapgames.api

import br.com.fiap.fiapgames.model.Jogo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JogoApi {

    @POST("/jogo")
    fun save(@Body jogo: Jogo) : Call<Jogo>

    @GET("/jogo")
    fun searchAll() : Call<List<Jogo>>

    @GET("/jogo/{titulo}")
    fun search(@Path("titulo")titulo: String) : Call<Jogo>
}