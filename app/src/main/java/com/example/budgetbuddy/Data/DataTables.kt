package com.example.budgetbuddy.Data

import androidx.compose.ui.graphics.painter.Painter

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetbuddy.navigation.AppScreens
import java.time.LocalDate
import java.util.UUID


data class GastoDia(val cantidad: Double, val fecha: LocalDate)
data class GastoTipo(val cantidad: Double, val tipo: TipoGasto)
data class Diseño(val pantalla: AppScreens, val nombre: String, val icono: Painter)


@Entity
data class Gasto(
    var nombre: String,
    var cantidad: Double,
    var fecha: LocalDate,
    var tipo: TipoGasto,
    @PrimaryKey val id: String = UUID.randomUUID().toString()
){
    override fun toString(): String {
        return "${nombre} (${tipo.tipo}):\t\t${cantidad}€\n"
    }
}

