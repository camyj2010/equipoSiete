package com.example.equiposiete.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.equiposiete.R

/**
 * Implementation of App Widget functionality.
 */
class Widget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if ("ACTION_IMAGE_CLICK" == intent.action) {
            // Si se hace clic en el ImageView, imprime la palabra "clic"
            Log.d("TuAppWidget", "Clic")
        } else if ("ACTION_BUTTON_CLICK" == intent.action) {
            // Si se hace clic en el Button, cambia el texto del TextView
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, Widget::class.java))

            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget)
                updateTextSaldo(context, views)
            }
        }
    }
}

private fun updateTextSaldo(context: Context, views: RemoteViews) {
    views.setTextViewText(R.id.text_saldo, "$ 1,000")
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
)
{
    val views = RemoteViews(context.packageName, R.layout.widget)

    // Configura un PendingIntent para el clic en el ImageView
    val intentImageClick = Intent(context, Widget::class.java)
    intentImageClick.action = "ACTION_IMAGE_CLICK"
    val pendingIntentImageClick =
        PendingIntent.getBroadcast(context, 0, intentImageClick, PendingIntent.FLAG_UPDATE_CURRENT)

    // Establece el PendingIntent en el ImageView
    views.setOnClickPendingIntent(R.id.iconImageView, pendingIntentImageClick)

    // Configura un PendingIntent para el clic en el Button
    val intentButtonClick = Intent(context, Widget::class.java)
    intentButtonClick.action = "ACTION_BUTTON_CLICK"
    val pendingIntentButtonClick =
        PendingIntent.getBroadcast(context, 0, intentButtonClick, PendingIntent.FLAG_UPDATE_CURRENT)

    // Establece el PendingIntent en el Button
    views.setOnClickPendingIntent(R.id.ButtonRandom, pendingIntentButtonClick)

    // Llama a la función para actualizar el texto del saldo
    updateTextSaldo(context, views)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
