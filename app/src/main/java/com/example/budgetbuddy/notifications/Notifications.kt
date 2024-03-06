package com.example.budgetbuddy.notifications

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.example.budgetbuddy.MainActivity
import com.example.budgetbuddy.R
import java.io.File

fun downloadNotification(context: Context,
                         titulo: String,
                         description: String,
                         id: Int
){
    val notificationManager = context.getSystemService(NotificationManager::class.java)

    var notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
        .setContentTitle(titulo)
        .setContentText(description)
        .setSmallIcon(R.drawable.download)
        .setAutoCancel(true)
        .setStyle(
            NotificationCompat.BigTextStyle()
            .bigText(description))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    notificationManager.notify(id, notification)
}

fun compartirContenido(
    context: Context,
    contenido: String,
    uri: String= "",
    asunto: String = ""
){
    if (uri=="" && asunto==""){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, contenido)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "BudgetBuddy")
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)

    }else{
        val destinatario = "budgetbuddy46@gmail.com"

        val uriString = "mailto:" + Uri.encode(destinatario) +
                "?subject=" + Uri.encode(asunto) +
                "&body=" + Uri.encode(contenido)
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        context.startActivity(intent)
    }

}

//private fun sendImage(context: Context){
//    val intent = Intent().apply {
//        action = Intent.ACTION_SEND
//        putExtra(Intent.EXTRA_STREAM, Uri.EMPTY)
//        type = "image/*"
//    }
//    context.startActivity(Intent.createChooser(intent, null))
//}