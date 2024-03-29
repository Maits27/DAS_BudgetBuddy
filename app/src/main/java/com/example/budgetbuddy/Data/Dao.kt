package com.example.budgetbuddy.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.budgetbuddy.Data.Enumeration.TipoGasto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


/**
 * La DAO define la API de acceso a la base de datos de Room,
 * sin necesidad de escribir las consultas SQL (salvo los query).
 * */
@Dao
interface GastoDao {

    /////////////// Funciones Insert ///////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasto(gasto: Gasto)

    suspend fun insertGastos(gastos: List<Gasto>) = gastos.map { insertGasto(it) }

    /////////////// Funciones Delete ///////////////
    @Delete
    suspend fun deleteGasto(gasto: Gasto): Int

    /////////////// Funciones Select ///////////////
    @Transaction
    @Query("SELECT * FROM Gasto ORDER BY fecha")
    fun todosLosGastos(): Flow<List<Gasto>>
    @Transaction
    @Query("SELECT * FROM Gasto WHERE fecha=:fecha")
    fun elementosFecha(fecha: LocalDate): Flow<List<Gasto>>

    /////////////// Funciones de cálculo ///////////////
    /**
     * En caso de no existir los datos que se están buscando,
     * con la función de [IFNULL] se define el valor por defecto
     * al devolver, evitando así errores de SQL Exception.
     */

    @Transaction
    @Query("SELECT ROUND(IFNULL(SUM(cantidad), 0.0), 2) FROM Gasto")
    fun gastoTotal(): Flow<Double>
    @Transaction
    @Query("SELECT ROUND(IFNULL(SUM(cantidad), 0.0), 2) FROM Gasto WHERE fecha=:fecha")
    fun gastoTotalDia(fecha: LocalDate): Flow<Double>
    @Transaction
    @Query("SELECT IFNULL(COUNT(*), 0) FROM Gasto")
    fun cuantosGastos(): Int
    @Transaction
    @Query("SELECT IFNULL(COUNT(*), 0) FROM Gasto WHERE fecha=:fecha")
    fun cuantosDeDia(fecha: LocalDate): Int
    @Transaction
    @Query("SELECT IFNULL(COUNT(*), 0) FROM Gasto WHERE tipo=:tipo")
    fun cuantosDeTipo(tipo: TipoGasto): Int

    /////////////// Funciones Update ///////////////
    @Update
    fun editarGasto(gasto: Gasto): Int
}
