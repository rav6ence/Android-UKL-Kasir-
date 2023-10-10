package com.serafine.uklkasir

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.serafine.uklkasir.database.CafeDatabase
import com.serafine.uklkasir.database.DetailTransaksi
import com.serafine.uklkasir.database.Menu
import com.serafine.uklkasir.database.Transaksi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckOutActivity : AppCompatActivity() {
    lateinit var namaPelanggan: TextView
    lateinit var spinnerMeja: Spinner
    lateinit var checkoutButton: Button
    lateinit var dibayar: CheckBox

    lateinit var db: CafeDatabase

    var id_user: Int = 0
    var listIdMenu = arrayListOf<Int>()
    var listMenu = arrayListOf<Menu>()
    var addAgain: Boolean = false
    var id_transaksi: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        namaPelanggan = findViewById(R.id.namaPelanggan)
        spinnerMeja = findViewById(R.id.spinnerMeja)
        checkoutButton = findViewById(R.id.checkOut)
        dibayar = findViewById(R.id.dibayar)

        db = CafeDatabase.getInstance(applicationContext)
        id_user = intent.getIntExtra("id_user",0)
        id_transaksi = intent.getIntExtra("id_transaksi",0)
        listIdMenu = intent.getIntegerArrayListExtra("list")!!
        addAgain = intent.getBooleanExtra("addAgain",false)

        for (i in listIdMenu){
            var menu = db.cafeDao().getMenu(i)
            listMenu.add(menu)
        }

        setDataSpinner()
        var status = "Belum Bayar"

        if(addAgain == true){
            namaPelanggan.visibility = View.INVISIBLE
            namaPelanggan.isEnabled = false
            spinnerMeja.visibility = View.INVISIBLE
            spinnerMeja.isEnabled = false
        }

        checkoutButton.setOnClickListener{
            if(addAgain == true){
                for (i in listMenu){
                    db.cafeDao().insertDetailTransaksi(
                        DetailTransaksi(
                        null,
                        id_transaksi,
                        i.id_menu!!,
                        i.harga
                    )
                    )
                }
                finish()

            } else {
                if(namaPelanggan.text.isNotEmpty() && spinnerMeja.selectedItem != null){
                    var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val formatterWaktu = DateTimeFormatter.ofPattern("HH:mm")
                    var current = LocalDateTime.now().format(formatter)
                    val currentWaktu = LocalDateTime.now().format(formatterWaktu)
                    if(dibayar.isChecked){
                        status = "Dibayar"
                    }
                    var newTransaksi = Transaksi(null,
                        currentWaktu,
                        current,
                        id_user,
                        db.cafeDao().getIdMejaFromNama(spinnerMeja.selectedItem.toString()),
                        namaPelanggan.text.toString(),
                        status)
                    db.cafeDao().insertTransaksi(newTransaksi)
                    var idtransaksi = db.cafeDao().getIdTransaksiFromOther(
                        newTransaksi.tgl_transaksi,
                        newTransaksi.id_user,
                        newTransaksi.id_meja,
                        newTransaksi.nama_pelanggan,
                        newTransaksi.status)
                    var meja = db.cafeDao().getMeja(newTransaksi.id_meja)
                    if(dibayar.isChecked){
                        db.cafeDao().updateMeja(meja.nomor_meja, meja.id_meja!!, true)
                    }
                    for (i in listMenu){
                        db.cafeDao().insertDetailTransaksi(DetailTransaksi(
                            null,
                            idtransaksi,
                            i.id_menu!!,
                            i.harga
                        ))
                    }
                    finish()
                }
                else if(namaPelanggan.text.isEmpty()) {
                    namaPelanggan.setError("Nama harus diisi")
                }
            }
            val moveIntent = Intent(this@CheckOutActivity, ListTransaksiActivity::class.java)
            startActivity(moveIntent)
        }
    }

    private fun setDataSpinner(){
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, db.cafeDao().getAllNamaMeja())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMeja.adapter = adapter
    }
}