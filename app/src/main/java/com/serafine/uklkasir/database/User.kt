package com.serafine.uklkasir.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_user") var id_user: Int? = null,
    @ColumnInfo(name = "nama") var nama: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "role") var role: String
)