package br.com.fiap.fiapgames.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.fiap.fiapgames.R
import br.com.fiap.fiapgames.about.AboutActivity
import br.com.fiap.fiapgames.list.ListActivity
import br.com.fiap.fiapgames.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btRegister.setOnClickListener {
            startActivityForResult(Intent(this, RegisterActivity::class.java), 1)
        }

        btList.setOnClickListener {
            startActivityForResult(Intent(this, ListActivity::class.java), 1)
        }

        btAbout.setOnClickListener {
            startActivityForResult(Intent(this, AboutActivity::class.java), 1)
        }

    }
}
