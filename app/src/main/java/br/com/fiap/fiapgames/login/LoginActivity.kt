package br.com.fiap.fiapmoney.login

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.fiap.fiapgames.R
import br.com.fiap.fiapgames.api.UserApi
import br.com.fiap.fiapmoney.model.Usuario
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        btSignup.setOnClickListener{
//            startActivityForResult(Intent(this, SignUpActivity::class.java), 1)
//        }

        btLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val okHttp = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.56:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()

        val userApi = retrofit.create(UserApi::class.java)

        var usuario = Usuario()
        usuario.email = inputLoginEmail.text.toString()
        usuario.password = inputLoginPassword.text.toString()

        userApi.login(
            usuario
        ).enqueue(object : Callback<Usuario> {
            override fun onFailure(call: Call<Usuario>?, t: Throwable?) {
                Toast.makeText(this@LoginActivity,
                    t?.message,
                    Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(call: Call<Usuario>?, response: Response<Usuario>?) {
                if(response?.isSuccessful == true) {

                    val usuario = response.body()

                    if(usuario != null) {
                        Toast.makeText(this@LoginActivity,
                            "Pronto",
                            Toast.LENGTH_LONG)
                            .show()
                    }

                } else {

                   Toast.makeText(this@LoginActivity,
                        "Problema ao autenticar o usuário",
                        Toast.LENGTH_LONG)
                        .show()


                }

            }
        })

    }


}
