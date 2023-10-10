package com.serafine.uklkasir.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DetailTransaksi(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_detail_transaksi") var id_detail_transaksi: Int?,
    @ColumnInfo(name = "id_transaksi") var id_transaksi: Int?,
    @ColumnInfo(name = "id_menu") var id_menu: Int,
    @ColumnInfo(name = "harga") var harga: Int
)