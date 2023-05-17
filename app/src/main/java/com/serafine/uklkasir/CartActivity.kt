package com.serafine.uklkasir

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serafine.uklkasir.adapter.CartAdapter
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.Menu

class CartActivity : AppCompatActivity() {
    private var listCart = arrayListOf<Int?>()
    private var listMenu = arrayListOf<Menu>()

    lateinit var db: CafeDatabase

    lateinit var recycler: RecyclerView
    lateinit var checkoutButton: Button
    lateinit var cartAdapter: CartAdapter

    var id_user: Int = 0
    var addAgain: Boolean = false
    var id_transaksi: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        db = CafeDatabase.getInstance(applicationContext)
        listCart = intent.getIntegerArrayListExtra("CART")!!
        id_user = intent.getIntExtra("id_user",0)
        id_transaksi = intent.getIntExtra("id_transaksi",0)
        addAgain = intent.getBooleanExtra("addAgain", false)

        recycler = findViewById(R.id.recyclerCart)
        checkoutButton = findViewById(R.id.checkOut)

        for (i in listCart){
            var menu = db.cafeDao().getMenu(i!!)
            listMenu.add(menu)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        listMenu.sortBy { it.jenis }
        cartAdapter = CartAdapter(listMenu)
        recycler.adapter = cartAdapter

        var total: Int = 0
        for (i in listMenu){
            total += i.harga
        }
        checkoutButton.text = "Checkout (" + listMenu.size + ") " + "Rp." + total

        checkoutButton.setOnClickListener {
            val moveIntent = Intent(this@CartActivity, CheckOutActivity::class.java)
            moveIntent.putExtra("id_user", id_user)
            moveIntent.putExtra("id_transaksi", id_transaksi)
            moveIntent.putExtra("addAgain", addAgain)
            var listIdMenu = arrayListOf<Int?>()
            for(i in listMenu){
                listIdMenu.add(i.id_menu)
            }
            moveIntent.putIntegerArrayListExtra("list", listIdMenu)
            startActivity(moveIntent)
        }
    }
}