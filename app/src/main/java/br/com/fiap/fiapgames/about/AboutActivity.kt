package br.com.fiap.fiapgames.about

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.fiap.fiapgames.R
import br.com.fiap.fiapgames.home.HomeActivity
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        ibHome.setOnClickListener {
            startActivityForResult(Intent(this, HomeActivity::class.java), 1)
        }
    }




}
