package com.example.budgetbuddy.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Data.ExpenseFilter
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.GastoTipo
import com.example.budgetbuddy.Data.TipoGasto
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer

@Composable
fun Dashboards(appViewModel: AppViewModel, navController: NavController){
    var colors = mutableListOf(
        Color(0xffC4FDD2),
        Color(0xffC4FAFD),
        Color(0xffC4D3FD),
        Color(0xffDAC4FD),
        Color(0xffFDC4E9),
        Color(0xffFDF4C4),
    )
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.gasto_dia, appViewModel.escribirMesyAño()),
            Modifier.padding(16.dp))
        Barras(appViewModel)

        Divider()

        Text(
            text = stringResource(id = R.string.gasto_tipo),
            Modifier.padding(top=16.dp))
        val datosTipo = appViewModel.sacarDatosPorTipo()
        LeyendaColores(colors, datosTipo)
        Pastel(appViewModel, datosTipo, colors)
    }
}

@Composable
fun Barras(
    appViewModel: AppViewModel){
    val datos = appViewModel.sacarDatosMes()
    val datosMes = datos.sortedBy { it.fecha.dayOfMonth }
    var barras = ArrayList<BarChartData.Bar>()

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
            .padding(30.dp)
            .height(300.dp),
        labelDrawer = SimpleValueDrawer(
            drawLocation = SimpleValueDrawer.DrawLocation.XAxis
        )
    )
}

@Composable
fun Pastel(appViewModel: AppViewModel, datosTipo: MutableList<GastoTipo>, colors: List<Color>){
    var slices = ArrayList<PieChartData.Slice>()

    datosTipo.mapIndexed{index, gasto ->
        slices.add(PieChartData.Slice(
            value = gasto.cantidad.toFloat(),
            color = colors.get(index)
        ))
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

@Composable
fun LeyendaColores(colors: List<Color>, datosTipo: MutableList<GastoTipo>) {
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
                        modifier = Modifier.padding(4.dp).width(100.dp)
                    ) {
                        // Cuadrado de color
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(colors[index])
                        )
                        // Etiqueta
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = gasto.tipo.tipo)
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
                                    .background(colors[index+1])
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
