package com.serafine.uklkasir

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serafine.uklkasir.adapter.DetailAdapter
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.DetailTransaksi

class ListDetailTransaksiActivity : AppCompatActivity() {
    lateinit var buttonAdd: ImageButton
    lateinit var recycler: RecyclerView
    lateinit var total: TextView

    lateinit var adapter: DetailAdapter
    private var listDetail = arrayListOf<DetailTransaksi>()
    private var id_transaksi = 0
    private var totalHarga = 0

    lateinit var db: CafeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail_transaksi)

        buttonAdd = findViewById(R.id.buttonAdd)
        recycler = findViewById(R.id.recyclerDetail)
        total = findViewById(R.id.total)

        id_transaksi = intent.getIntExtra("id_transaksi", 0)

        db = CafeDatabase.getInstance(applicationContext)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = DetailAdapter(listDetail)
        recycler.adapter = adapter

        if(db.cafeDao().getTransaksi(id_transaksi).status == "Dibayar"){
            buttonAdd.isEnabled = false
            buttonAdd.visibility = View.INVISIBLE
        }
        buttonAdd.setOnClickListener{
            val moveIntent = Intent(this@ListDetailTransaksiActivity, AddItemOnDetailActivity::class.java)
            moveIntent.putExtra("id_transaksi", id_transaksi)
            startActivity(moveIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        getDetail()
        totalHarga = 0
        for (i in listDetail){
            totalHarga += i.harga
        }
        total.text = "Rp." + totalHarga
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getDetail(){
        listDetail.clear()
        listDetail.addAll(db.cafeDao().getDetailTransaksi(id_transaksi))
        adapter.notifyDataSetChanged()
    }
}