package br.com.fiap.fiapgames.model

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.fiap.fiapgames.R
import br.com.fiap.fiapgames.home.HomeActivity
import br.com.fiap.fiapgames.register.RegisterActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_item.view.*

class ListaAdapter(

    val jogos: List<Jogo>,
    val context: Context,
    val listener: (Jogo) -> Unit
) :
    RecyclerView.Adapter<ListaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.game_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jogos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = jogos[position]
        holder?.let {
            it.bindView(pokemon, listener)
        }
    }


    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bindView(
            jogo: Jogo,
            listener: (Jogo) -> Unit
        ) = with(itemView) {

            tvJogo.text = jogo.titulo
            tvPlataforma.text = jogo.plataforma

            Picasso.get()
                .load(R.drawable.gamepad)
                .placeholder(R.drawable.searching)
                .error(R.drawable.notfound)
                .into(ivJogo)

            setOnClickListener {

                val intent = Intent(itemView.context, RegisterActivity::class.java)
                intent.putExtra("id", jogo.id)
                intent.putExtra("titulo", jogo.titulo)
                intent.putExtra("plataforma", jogo.plataforma)
                itemView.context.startActivity(intent)


            }

        }

    }


}