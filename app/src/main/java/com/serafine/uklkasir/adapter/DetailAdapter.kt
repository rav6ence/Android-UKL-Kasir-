package com.serafine.uklkasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.serafine.uklkasir.R
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.DetailTransaksi


class DetailAdapter (var items: List<DetailTransaksi>): RecyclerView.Adapter<DetailAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var namaMenu: TextView
        var hargaMenu: TextView

        init {
            namaMenu = view.findViewById(R.id.namaMenu)
            hargaMenu = view.findViewById(R.id.hargaMenu)
        }
    }

    lateinit var db: CafeDatabase

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        db = CafeDatabase.getInstance(parent.context)
        var view = LayoutInflater.from(parent.context).inflate(R.layout.detail_template, parent, false)
        return DetailAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaMenu.text = db.cafeDao().getMenu(items.get(position).id_menu).nama_menu
        holder.hargaMenu.text = "Rp." + db.cafeDao().getMenu(items.get(position).id_menu).harga.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}