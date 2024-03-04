package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.GastoDia
import com.example.budgetbuddy.Data.GastoTipo
import com.example.budgetbuddy.Data.obtenerTipoEnIdioma
import com.example.budgetbuddy.R
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun Dashboards(appViewModel: AppViewModel, idioma: String){

    var showCalendar by remember { mutableStateOf(false) }
    var colors = mutableListOf(
        Color(0xffC4FDD2),
        Color(0xffC4FAFD),
        Color(0xffC4D3FD),
        Color(0xffDAC4FD),
        Color(0xffFDC4E9),
        Color(0xffFDF4C4),
    )
    val fecha by appViewModel.fecha.collectAsState(initial = LocalDate.now())
    val onCalendarConfirm: (LocalDate) -> Unit = {
        showCalendar = false
        appViewModel.cambiarFecha(it)
    }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.gasto_dia, appViewModel.escribirMesyAño(fecha)),
            Modifier.padding(16.dp))
        Button(
            onClick = { showCalendar = true },
            Modifier.padding(10.dp)
        ) {
            Text(text = stringResource(id = R.string.date_pick))
        }
        Calendario(show = showCalendar, onCalendarConfirm)

        Barras(fecha, appViewModel)

        Divider()

        Text(
            text = stringResource(id = R.string.gasto_tipo),
            Modifier.padding(top=16.dp)
        )
        val datosTipo by appViewModel.listadoGastosTipo(fecha).collectAsState(emptyList())
        LeyendaColores(idioma, colors, datosTipo)
        Pastel(fecha, datosTipo, colors)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Barras(
    fecha: LocalDate,
    appViewModel: AppViewModel
){
    val datosMes by appViewModel.listadoGastosMes(fecha).collectAsState(emptyList())
    var barras = ArrayList<BarChartData.Bar>()

    when{
        datosMes.isEmpty() -> {
            Column (
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 10.dp)
                    .height(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(text = stringResource(id = R.string.no_data))
            }
        }else ->{
            datosMes.mapIndexed{ index, gasto ->
                barras.add(
                    BarChartData.Bar(
                        label = gasto.fecha.dayOfMonth.toString(),
                        value = gasto.cantidad.toFloat(),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
            BarChart(
                barChartData = BarChartData(barras),
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 10.dp)
                    .height(300.dp),
                labelDrawer = SimpleValueDrawer(
                    drawLocation = SimpleValueDrawer.DrawLocation.XAxis
                )
            )
        }
    }
}

@Composable
fun Pastel(fecha: LocalDate, datosTipo: List<GastoTipo>, colors: List<Color>){
    var slices = ArrayList<PieChartData.Slice>()
    when{
        datosTipo.isEmpty() -> {
            Column (
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 10.dp)
                    .height(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(text = stringResource(id = R.string.no_data))
            }
        }else -> {
            datosTipo.mapIndexed { index, gasto ->
                slices.add(
                    PieChartData.Slice(
                        value = gasto.cantidad.toFloat(),
                        color = colors.get(index)
                    )
                )
            }
            PieChart(
                pieChartData = PieChartData(
                    slices = slices
                ),
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .height(250.dp),
                sliceDrawer = SimpleSliceDrawer(
                    sliceThickness = 50f
                )
            )
        }
    }
}

@Composable
fun LeyendaColores(idioma: String,  colors: List<Color>, datosTipo: List<GastoTipo>) {
    when{
        datosTipo.isNotEmpty() -> {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                datosTipo.mapIndexed{index, gasto ->
                    if (index%2 == 0){
                        Row (horizontalArrangement = Arrangement.Center) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .width(150.dp)
                            ) {
                                // Cuadrado de color
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(colors[index])
                                )
                                // Etiqueta
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = obtenerTipoEnIdioma(gasto.tipo, idioma))
                            }
                            if (index+1<datosTipo.size){
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    // Cuadrado de color
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(colors[index + 1])
                                    )
                                    // Etiqueta
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = datosTipo[index+1].tipo.tipo)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}