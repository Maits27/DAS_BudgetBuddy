package com.example.budgetbuddy

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.Idioma
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.security.MessageDigest
import kotlin.random.Random

class AppViewModel: ViewModel() {

    private val _forceRefresh = MutableStateFlow(false)
    val forceRefresh: StateFlow<Boolean>
        get() = _forceRefresh


    var idioma by mutableStateOf(Idioma.ES)
        private set
    var listadoGastos: MutableList<Gasto> = mutableListOf()
        private set

    var facturaActual by mutableStateOf("")
        private set


    init {
        // Código a ejecutar al iniciar el ViewModel
        for (cantidad in 1..10){
            añadirGasto("Gasto Inicial $cantidad", 1.0*cantidad)
        }
        Log.d("appviewmodel", listadoGastos.toString())
    }
    fun añadirGasto(nombre: String, cantidad: Double){
        if (nombre != ""){
            if (cantidad>0.0){
                listadoGastos.add(Gasto(nombre, cantidad))
            }
        }
    }

    fun borrarGasto(gasto: Gasto){
        listadoGastos.remove(gasto)
        this.triggerRefresh()
    }

    fun gastoTotal(): Double{
        var gasto: Double = 0.0
        if (!listadoGastos.isEmpty()){
            for (g in listadoGastos){
                gasto = gasto + g.cantidad
            }
        }
        return gasto
    }
    fun cambiarFactura(factura: String){
        facturaActual = factura
    }

    fun cambiarIdioma(code:String){
        for (i in Idioma.entries){
            if (code == i.code) {
                idioma = i
            }
        }
    }

    fun hashString(input: String, algorithm: String = "SHA-256"): String {
        val bytes = input.toByteArray()
        val digest = MessageDigest.getInstance(algorithm)
        val hashedBytes = digest.digest(bytes)

        return bytesToHex(hashedBytes)
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
            hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
        }
        return String(hexChars)
    }

    private fun triggerRefresh() {
        _forceRefresh.value = true
    }

    // Esta función se llama cuando se ha completado la actualización
    fun refreshComplete() {
        _forceRefresh.value = false
    }

}

