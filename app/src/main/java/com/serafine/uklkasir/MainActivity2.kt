package com.serafine.uklkasir

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serafine.uklkasir.adapter.ItemAdapter
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.Menu


class MainActivity2 : AppCompatActivity() {
    lateinit var recyclerMakanan: RecyclerView
    lateinit var recyclerMinuman: RecyclerView

    lateinit var adapterMakanan: ItemAdapter
    lateinit var adapterMinuman: ItemAdapter

    lateinit var db: CafeDatabase
    lateinit var addButton: ImageButton
    lateinit var mejaButton: ImageButton
    lateinit var transaksiButton: ImageButton
    lateinit var checkoutButton: Button

    private var listMakanan = mutableListOf<Menu>()
    private var listMinuman = mutableListOf<Menu>()

    private var listCart = arrayListOf<Int?>()

    var nama: String = ""
    var role: String = ""
    var id_user: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        recyclerMakanan = findViewById(R.id.recyclerMakanan)
        recyclerMinuman = findViewById(R.id.recyclerMinuman)
        addButton = findViewById(R.id.buttonAdd)
        mejaButton = findViewById(R.id.buttonMeja)
        transaksiButton = findViewById(R.id.buttonTransaksi)
        checkoutButton = findViewById(R.id.checkOut)

        db = CafeDatabase.getInstance(applicationContext)
        adapterMakanan = ItemAdapter(listMakanan)
        adapterMakanan.onAddClick = {
            listCart.add(it.id_menu)
            checkoutButton.isEnabled = true
            checkoutButton.visibility = View.VISIBLE
            checkoutButton.text = "Checkout (" + listCart.size + ")"
        }
        adapterMinuman = ItemAdapter(listMinuman)
        adapterMinuman.onAddClick = {
            listCart.add(it.id_menu)
            checkoutButton.isEnabled = true
            checkoutButton.visibility = View.VISIBLE
            checkoutButton.text = "Checkout (" + listCart.size + ")"
        }
        adapterMakanan.onSubstractClick = {
            listCart.remove(it.id_menu)
            if(listCart.size == 0){
                checkoutButton.isEnabled = false
                checkoutButton.visibility = View.INVISIBLE
            }
            checkoutButton.text = "Checkout (" + listCart.size + ")"
        }
        adapterMinuman.onSubstractClick = {
            listCart.remove(it.id_menu)
            if(listCart.size == 0){
                checkoutButton.isEnabled = false
                checkoutButton.visibility = View.INVISIBLE
            }
            checkoutButton.text = "Checkout (" + listCart.size + ")"
        }

        recyclerMakanan.adapter = adapterMakanan
        recyclerMakanan.layoutManager = LinearLayoutManager(this)
        recyclerMinuman.adapter = adapterMinuman
        recyclerMinuman.layoutManager = LinearLayoutManager(this)

        swipeToGesture(recyclerMakanan)
        swipeToGesture(recyclerMinuman)

        nama = intent.getStringExtra("name")!!
        role = intent.getStringExtra("role")!!
        id_user = intent.getIntExtra("id_user", 0)
        Toast.makeText(applicationContext, "Logged in as " + nama, Toast.LENGTH_SHORT).show()

        if(role != "Admin"){
            addButton.isEnabled = false
            addButton.visibility = View.INVISIBLE
        }
        if(listCart.size == 0){
            checkoutButton.isEnabled = false
            checkoutButton.visibility = View.INVISIBLE
        }

        addButton.setOnClickListener{
            val moveIntent = Intent(this@MainActivity2, AddItemActivity::class.java)
            startActivity(moveIntent)
        }
        mejaButton.setOnClickListener{
            val moveIntent = Intent(this@MainActivity2, ListMejaActivity::class.java)
            startActivity(moveIntent)
        }
        transaksiButton.setOnClickListener{
            val moveIntent = Intent(this@MainActivity2, ListTransaksiActivity::class.java)
            startActivity(moveIntent)
        }
        checkoutButton.setOnClickListener{
            val moveIntent = Intent(this@MainActivity2, CartActivity::class.java)
            moveIntent.putIntegerArrayListExtra("CART", listCart)
            moveIntent.putExtra("id_user", id_user)
            startActivity(moveIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        getMenu()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getMenu() {
        listMakanan.clear()
        listMinuman.clear()
        listMakanan.addAll(db.cafeDao().getMenuFilterJenis("Makanan"))
        listMinuman.addAll(db.cafeDao().getMenuFilterJenis("Minuman"))
        adapterMakanan.notifyDataSetChanged()
        adapterMinuman.notifyDataSetChanged()
    }
    private fun swipeToGesture(itemRv: RecyclerView){
        val swipeGesture = object : SwipeGesture(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val actionBtnTapped = false

                try{
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            var adapter: ItemAdapter = itemRv.adapter as ItemAdapter
                            db.cafeDao().deleteMenu(adapter.getItem(position))
                            adapter.notifyItemRemoved(position)
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                        ItemTouchHelper.RIGHT -> {
                            val moveIntent = Intent(this@MainActivity2, EditItemActivity::class.java)
                            var adapter: ItemAdapter = itemRv.adapter as ItemAdapter
                            var menu = adapter.getItem(position)
                            moveIntent.putExtra("ID", menu.id_menu)
                            moveIntent.putExtra("nama_menu", menu.nama_menu)
                            moveIntent.putExtra("harga_menu", menu.harga)
                            moveIntent.putExtra("jenis", menu.jenis)
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