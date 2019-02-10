package br.com.fiap.fiapgames.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import br.com.fiap.fiapgames.R
import br.com.fiap.fiapgames.api.JogoApi
import br.com.fiap.fiapgames.home.HomeActivity
import br.com.fiap.fiapgames.login.SignUpActivity
import br.com.fiap.fiapgames.model.Jogo
import br.com.fiap.fiapgames.model.ListaAdapter
import br.com.fiap.fiapgames.register.RegisterActivity
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_list.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        ibCadastrar.setOnClickListener {
            startActivityForResult(Intent(this, RegisterActivity::class.java), 1)
        }

        ibHome.setOnClickListener {
            startActivityForResult(Intent(this, HomeActivity::class.java), 1)
        }

        list()
    }


    private fun list() {
        val okHttp = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://fiapjogos-api.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()

        val jogoApi = retrofit.create(JogoApi::class.java)

        var jogos: List<Jogo> = arrayListOf()

        jogoApi.searchAll().enqueue(object : Callback<List<Jogo>> {
            override fun onFailure(call: Call<List<Jogo>>?, t: Throwable?) {
                Toast.makeText(
                    this@ListActivity,
                    t?.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<List<Jogo>>?, response: Response<List<Jogo>>?) {
                if (response?.isSuccessful == true) {

                    jogos = response.body()!!

                    rvGames.adapter = ListaAdapter(jogos, this@ListActivity, { })
                    rvGames.layoutManager = LinearLayoutManager(this@ListActivity)


                } else {

                    // error case
                    when (response?.code()) {
                        500 -> Toast.makeText(
                            this@ListActivity,
                            "Servidor fora do ar",
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> Toast.makeText(
                            this@ListActivity,
                            "Erro desconhecido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }
        })


    }
}
