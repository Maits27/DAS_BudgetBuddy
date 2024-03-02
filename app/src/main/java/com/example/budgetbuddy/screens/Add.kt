package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Data.TipoGasto
import com.example.budgetbuddy.R
import java.time.LocalDate
import java.time.format.DateTimeParseException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Add(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
){
    var nombre by rememberSaveable { mutableStateOf("") }
    var euros by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var error_message by remember { mutableStateOf("") }

    var isTextFieldFocused by remember { mutableStateOf(-1) }
    var showError by rememberSaveable { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(TipoGasto.Otros) }

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = stringResource(id = R.string.add_element),
            Modifier.padding(16.dp)
        )
        Divider()

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(id = R.string.name_element)) },
            keyboardActions = KeyboardActions(
                onDone = {
                    isTextFieldFocused = -1
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        isTextFieldFocused = 0
                    }
                }
        )

        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true }),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedOption.tipo,
                        modifier = Modifier.padding(16.dp)
                    )
                    Row (
                        horizontalArrangement = Arrangement.End
                    ){
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }

                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight().background(color = MaterialTheme.colors.background),
                // Set maxHeight to ContentHeight.Intrinsic to adjust height dynamically
            ) {
                TipoGasto.entries.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = option
                            expanded = false
                        },
                        modifier = Modifier.background(color = MaterialTheme.colors.background)
                    ) {
                        Text(text = option.tipo, Modifier.background(color = MaterialTheme.colors.background))
                    }
                }
            }
        }


        OutlinedTextField(
            value = euros,
            onValueChange = { euros = it },
            label = { Text(stringResource(id = R.string.price_element)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    isTextFieldFocused = -1
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        isTextFieldFocused = 1
                    }
                }
        )
        OutlinedTextField(
            value = fecha.toString(),
            onValueChange = {
                fecha = try {
                    LocalDate.parse(it)
                } catch (e: DateTimeParseException) {
                    // Manejo de errores o asigna un valor predeterminado
                    LocalDate.now()
                }

            },
            label = { Text(stringResource(id = R.string.date_pick)) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        isTextFieldFocused = 2 // Cuando el campo de texto está enfocado
                    } else {
                        isTextFieldFocused = -1 // Cuando el campo de texto pierde el enfoque
                    }
                },
            keyboardOptions = KeyboardOptions.Default.copy(showKeyboardOnFocus = false)
        )


        Calendario(show = (isTextFieldFocused == 2), appViewModel = appViewModel) {
            fecha = it
            isTextFieldFocused = -1
            appViewModel.cambiarFecha(fecha)
        }



        val error_double = stringResource(id = R.string.error_double)
        val error_insert = stringResource(id = R.string.error_insert)

        Button(
            onClick = {
                if (nombre!="" && euros!=""){
                    if (euros.toDoubleOrNull() != null){
                        appViewModel.añadirGasto(nombre, euros.toDouble(), fecha, selectedOption)
                    }else{
                        showError = true
                        error_message = error_double
                    }
                }else{
                    showError = true
                    error_message = error_insert
                }
                if(!showError){
                    navController.navigateUp()
                }
            },
            Modifier
                .padding(8.dp, 16.dp)
        ) {
            Text(text = stringResource(id = R.string.add))
        }

        ErrorDeInsert(show = showError, mensaje = error_message) { showError = false }
    }
}
