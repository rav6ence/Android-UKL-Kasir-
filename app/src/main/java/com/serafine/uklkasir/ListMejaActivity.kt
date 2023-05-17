package com.serafine.uklkasir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serafine.uklkasir.adapter.MejaAdapter
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.Meja

class ListMejaActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var addButton: ImageButton

    lateinit var adapter: MejaAdapter
    private var listMeja = mutableListOf<Meja>()

    lateinit var db: CafeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_meja)

        recycler = findViewById(R.id.recyclerMeja)
        addButton = findViewById(R.id.buttonAdd)

        db = CafeDatabase.getInstance(applicationContext)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = MejaAdapter(listMeja)
        recycler.adapter = adapter

        swipeToGesture(recycler)

        addButton.setOnClickListener{
            val moveIntent = Intent(this@ListMejaActivity, AddMejaActivity::class.java)
            startActivity(moveIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        getMeja()
    }

    fun getMeja(){
        listMeja.clear()
        listMeja.addAll(db.cafeDao().getAllMeja())
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
                            var adapter: MejaAdapter = itemRv.adapter as MejaAdapter
                            db.cafeDao().deleteMeja(adapter.getItem(position))
                            adapter.notifyItemRemoved(position)
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                        ItemTouchHelper.RIGHT -> {
                            val moveIntent = Intent(this@ListMejaActivity, EditMejaActivity::class.java)
                            var adapter: MejaAdapter = itemRv.adapter as MejaAdapter
                            var meja = adapter.getItem(position)
                            moveIntent.putExtra("ID", meja.id_meja)
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