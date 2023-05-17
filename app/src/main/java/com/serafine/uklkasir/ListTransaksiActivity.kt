package com.serafine.uklkasir

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serafine.uklkasir.adapter.TransaksiAdapter
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.Transaksi

class ListTransaksiActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var adapter: TransaksiAdapter
    lateinit var db: CafeDatabase

    private var listTransaksi = arrayListOf<Transaksi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_transaksi)

        recycler = findViewById(R.id.recyclerTransaksi)
        db = CafeDatabase.getInstance(applicationContext)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = TransaksiAdapter(listTransaksi)
        recycler.adapter = adapter
        adapter.onHolderClick = {
            val moveIntent = Intent(this@ListTransaksiActivity, ListDetailTransaksiActivity::class.java)
            moveIntent.putExtra("id_transaksi", it.id_transaksi)
            startActivity(moveIntent)
        }

        swipeToGesture(recycler)
    }

    override fun onResume() {
        super.onResume()
        getTransaksi()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun getTransaksi(){
        listTransaksi.clear()
        listTransaksi.addAll(db.cafeDao().getAllTransaksi())
        adapter.notifyDataSetChanged()
    }

    private fun swipeToGesture(itemRv: RecyclerView){
        val swipeGesture = object : SwipeGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val actionBtnTapped = false

                try{
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            var adapter: TransaksiAdapter = itemRv.adapter as TransaksiAdapter
                            db.cafeDao().deleteTransaksi(adapter.getItem(position))
                            adapter.notifyItemRemoved(position)
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                        ItemTouchHelper.RIGHT -> {
                            val moveIntent = Intent(this@ListTransaksiActivity, EditTransaksiActivity::class.java)
                            var adapter: TransaksiAdapter = itemRv.adapter as TransaksiAdapter
                            var transaksi = adapter.getItem(position)
                            moveIntent.putExtra("ID", transaksi.id_transaksi)
                            startActivity(moveIntent)
                        }
                    }
                }
                catch (e: Exception){
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(itemRv)
    }
}