package com.example.equipoSiete.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Inventory(
    @PrimaryKey(autoGenerate = true)
    val codigo: Int = 0,
    val nombre: String,
    val precio: Int,
    val cantidad: Int): Serializable
