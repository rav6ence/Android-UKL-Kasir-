package com.serafine.uklkasir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.serafine.uklkasir.R
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.Transaksi

class TransaksiAdapter (var items: List<Transaksi>): RecyclerView.Adapter<TransaksiAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var textNama: TextView
        var textTanggal: TextView
        var textStatus: TextView
        var textMeja: TextView
        var relative: RelativeLayout

        init{
            textNama = view.findViewById(R.id.namaPelangganList)
            textTanggal = view.findViewById(R.id.tglList)
            textStatus = view.findViewById(R.id.statusList)
            textMeja = view.findViewById(R.id.mejaList)
            relative = view.findViewById(R.id.relative)
        }
    }
    lateinit var db: CafeDatabase
    var onHolderClick: ((Transaksi) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        db = CafeDatabase.getInstance(parent.context)
        var view = LayoutInflater.from(parent.context).inflate(R.layout.transaksi_template, parent, false)
        return TransaksiAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textNama.text = items[position].nama_pelanggan
        holder.textTanggal.text = items[position].tgl_transaksi
        holder.textStatus.text = items[position].status
        holder.textMeja.text = db.cafeDao().getMeja(items[position].id_meja).nomor_meja
        holder.relative.setOnClickListener{
            onHolderClick?.invoke(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(position: Int): Transaksi{
        return items.get(position)
    }
}