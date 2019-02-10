package br.com.fiap.fiapgames.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.fiap.fiapgames.R
import br.com.fiap.fiapgames.api.UserApi
import br.com.fiap.fiapgames.extensions.getValue
import br.com.fiap.fiapgames.home.HomeActivity
import br.com.fiap.fiapmoney.model.Usuario
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        btCreate.setOnClickListener {
            if (!isValid()) {

                Toast.makeText(
                    this@SignUpActivity,
                    "Todos os campos são obrigatórios",
                    Toast.LENGTH_SHORT
                ).show()
            }

            createUser()
        }
    }

    private fun createUser() {
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
        usuario.email = inputEmail.getValue()
        usuario.password = inputPassword.getValue()

        userApi.create(
            usuario
        ).enqueue(object : Callback<Usuario> {
            override fun onFailure(call: Call<Usuario>?, t: Throwable?) {
                Toast.makeText(
                    this@SignUpActivity,
                    t?.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            override fun onResponse(call: Call<Usuario>?, response: Response<Usuario>?) {
                if (response?.isSuccessful == true) {

                    Toast.makeText(
                        this@SignUpActivity,
                        "Cadastro realizado com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))

                } else {

                    // error case
                    when (response?.code()) {
                        500 -> Toast.makeText(
                            this@SignUpActivity,
                            "Servidor fora do ar",
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> Toast.makeText(
                            this@SignUpActivity,
                            "Erro desconhecido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }

            }
        })
    }

    private fun isValid(): Boolean {
        if (inputEmail?.getValue().isNullOrEmpty()) return false
        if (inputPassword?.getValue().isNullOrEmpty()) return false
        return true
    }
}
