package com.appmovil.loginfirestore.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appmovil.loginfirestore.R
import com.appmovil.loginfirestore.databinding.ArticleBinding
import com.appmovil.loginfirestore.model.Articulo

class ArticleAdapter (private val productList: List<Articulo>) :
    RecyclerView.Adapter<ArticleAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Aquí puedes inicializar las vistas de tu artículo.xml si es necesario
        val codigoTextView: TextView = itemView.findViewById(R.id.articleId)
        val nombreTextView: TextView = itemView.findViewById(R.id.articleName)
        val precioTextView: TextView = itemView.findViewById(R.id.articlePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        // Configura las vistas con los datos del producto actual
        holder.codigoTextView.text = "ID: ${product.codigo}"
        holder.nombreTextView.text = "Nombre: ${product.nombre}"
        holder.precioTextView.text = "Precio: ${product.precio}"
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}