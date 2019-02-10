package br.com.fiap.fiapmoney.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.fiap.fiapgames.R
import br.com.fiap.fiapgames.api.UserApi
import br.com.fiap.fiapgames.extensions.getValue
import br.com.fiap.fiapgames.home.HomeActivity
import br.com.fiap.fiapgames.login.SignUpActivity
import br.com.fiap.fiapmoney.model.Usuario
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context


class LoginActivity : AppCompatActivity() {

    private val KEY_APP_PREFERENCES = "email"
    private val KEY_EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (isConectado())
            iniciaApp()

        btSignup.setOnClickListener {
            startActivityForResult(Intent(this, SignUpActivity::class.java), 1)
        }

        btLogin.setOnClickListener {

            if (!isValid()) {

                Toast.makeText(
                    this@LoginActivity,
                    "Todos os campos são obrigatórios",
                    Toast.LENGTH_SHORT
                ).show()

            }
            login()
        }
    }

    private fun login() {
        val okHttp = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://fiapjogos-api.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()

        val userApi = retrofit.create(UserApi::class.java)

        var usuario = Usuario()
        usuario.email = inputLoginEmail.getValue()
        usuario.password = inputLoginPassword.getValue()

        userApi.login(usuario).enqueue(object : Callback<Usuario> {
            override fun onFailure(call: Call<Usuario>?, t: Throwable?) {
                Toast.makeText(
                    this@LoginActivity,
                    t?.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Usuario>?, response: Response<Usuario>?) {
                if (response?.isSuccessful == true) {

                    val usuario = response.body()

                    if (cbManterConectado.isChecked) {
                        manterConectado(usuario)
                    }

                    iniciaApp()

                } else {

                    // error case
                    when (response?.code()) {
                        404 -> Toast.makeText(
                            this@LoginActivity,
                            "Usuário ou senha inválidos",
                            Toast.LENGTH_SHORT
                        ).show()
                        500 -> Toast.makeText(
                            this@LoginActivity,
                            "Servidor fora do ar",
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> Toast.makeText(
                            this@LoginActivity,
                            "Erro desconhecido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        })

    }

    private fun iniciaApp() {
        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
    }

    private fun manterConectado(usuario: Usuario?) {
        val pref = getSharedPreferences(
            KEY_APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putString(KEY_EMAIL, usuario?.email)
        editor.commit()
    }

    private fun isConectado(): Boolean {
        val shared = getSharedPreferences(KEY_APP_PREFERENCES, Context.MODE_PRIVATE)
        val login = shared.getString(KEY_EMAIL, "")
        return (login != "")
    }

    private fun isValid(): Boolean {
        if (inputLoginEmail?.getValue().isNullOrEmpty()) return false
        if (inputLoginPassword?.getValue().isNullOrEmpty()) return false
        return true
    }

}
