package com.serafine.uklkasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.serafine.uklkasir.R
import com.serafine.uklkasir.database.Menu

class CartAdapter (var items: List<Menu>): RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var txtNamaMenu: TextView
        var txtHargaMenu: TextView

        init {
            txtNamaMenu = view.findViewById(R.id.namaItem)
            txtHargaMenu = view.findViewById(R.id.hargaItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.cart_template, parent, false)
        return CartAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = items[position]
        holder.txtNamaMenu.text = items[position].nama_menu
        holder.txtHargaMenu.text = "Rp." + items[position].harga
    }

    override fun getItemCount(): Int {
        return items.size
    }
}