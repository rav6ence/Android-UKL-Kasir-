package com.serafine.uklkasir.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_transaksi") var id_transaksi: Int?,
    @ColumnInfo(name = "waktu_transaksi") var waktu_transaksi: String,
    @ColumnInfo(name = "tgl_transaksi") var tgl_transaksi: String,
    @ColumnInfo(name = "id_user") var id_user: Int,
    @ColumnInfo(name = "id_meja") var id_meja: Int,
    @ColumnInfo(name = "nama_pelanggan") var nama_pelanggan: String,
    @ColumnInfo(name = "status") var status: String
)
