package br.com.fiap.fiapgames.register

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import br.com.fiap.fiapgames.R
import br.com.fiap.fiapgames.api.JogoApi
import br.com.fiap.fiapgames.extensions.getValue
import br.com.fiap.fiapgames.home.HomeActivity
import br.com.fiap.fiapgames.list.ListActivity
import br.com.fiap.fiapgames.model.Jogo
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val titulo: String? = intent.getStringExtra("titulo")
        val plataforma: String? = intent.getStringExtra("plataforma")

        if (!titulo.isNullOrEmpty()) {
            etGame.setText(titulo, TextView.BufferType.EDITABLE)
        }

        val adapter = ArrayAdapter.createFromResource(
            this, R.array.plataforms_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPlataforms.setAdapter(adapter)

        if (!plataforma.isNullOrEmpty()) {
            val spinnerPosition = adapter.getPosition(plataforma)
            spPlataforms.setSelection(spinnerPosition)
        }




        btSave.setOnClickListener {

            if (!isValid()) {

                Toast.makeText(
                    this@RegisterActivity,
                    "Todos os campos são obrigatórios",
                    Toast.LENGTH_SHORT
                ).show()

            }
            save()
        }
    }

    private fun save() {
        val okHttp = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://fiapjogos-api.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()

        val jogoApi = retrofit.create(JogoApi::class.java)

        var jogo = Jogo()
        jogo?.id = intent.getStringExtra("id")
        jogo?.titulo = etGame.getValue()
        jogo?.plataforma = spPlataforms.selectedItem.toString()

        jogoApi.save(jogo).enqueue(object : Callback<Jogo> {
            override fun onFailure(call: Call<Jogo>?, t: Throwable?) {
                Toast.makeText(
                    this@RegisterActivity,
                    t?.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<Jogo>?, response: Response<Jogo>?) {
                if (response?.isSuccessful == true) {

                    startActivityForResult(Intent(this@RegisterActivity, ListActivity::class.java), 1)

                } else {

                    // error case
                    when (response?.code()) {
                        500 -> Toast.makeText(
                            this@RegisterActivity,
                            "Servidor fora do ar",
                            Toast.LENGTH_SHORT
                        ).show()
                        else -> Toast.makeText(
                            this@RegisterActivity,
                            "Erro desconhecido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }
        })

    }

    private fun isValid(): Boolean {
        if (etGame?.getValue().isNullOrEmpty()) return false
        return true
    }


}
