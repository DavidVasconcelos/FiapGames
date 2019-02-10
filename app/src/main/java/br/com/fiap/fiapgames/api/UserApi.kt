package br.com.fiap.fiapgames.api


import br.com.fiap.fiapmoney.model.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @POST("/user/login")
    fun login(@Body usuario: Usuario) : Call<Usuario>

    @POST("/user")
    fun create(@Body usuario: Usuario) : Call<Usuario>


}