package com.example.budgetbuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.budgetbuddy.Data.Idioma

@Composable
fun Informacion(show: Boolean, onConfirm: () -> Unit) {
    if(show){
        AlertDialog(
            onDismissRequest = {},
            confirmButton = { TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(id = R.string.ok))
            }
            },
            title = { Text(text = stringResource(id = R.string.app_name)) },
            text = {
                Text(text = stringResource(id = R.string.app_description))
            }
        )
    }
}

@Composable
fun Idiomas(show: Boolean, appViewModel: AppViewModel, onConfirm: () -> Unit) {
    if(show){
        AlertDialog(
            onDismissRequest = {},
            confirmButton = { TextButton(onClick = { onConfirm() }){
                Text(text = stringResource(id = R.string.ok))
            }
            },
            title = { Text(text = stringResource(id = R.string.change_lang)) },
            text = {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    for (idioma in Idioma.entries){
                        Button(
                            onClick = {
                                onConfirm()
                                appViewModel.cambiarIdioma(idioma.code)},
                            Modifier.fillMaxWidth()
                        ) {
                            Text(text = idioma.language)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ErrorDeInsert(show: Boolean, mensaje: String, onConfirm: () -> Unit) {
    if(show){
        AlertDialog(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            onDismissRequest = {},
            confirmButton = { TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(id = R.string.ok))
            }
            },
            title = { Text(text = stringResource(id = R.string.error), color = MaterialTheme.colorScheme.onError) },
            text = {
                Text(text = mensaje, color = MaterialTheme.colorScheme.onPrimary)
            }
        )
    }
}