package com.example.equipoSiete.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.equipoSiete.R
import com.example.equipoSiete.view.LoginActivity
import com.example.equipoSiete.viewmodel.InventoryViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.equipoSiete.view.MainActivity
import com.example.equipoSiete.view.fragment.HomeInventoryFragment
import com.google.firebase.firestore.FirebaseFirestore


/**
 * Implementation of App Widget functionality.
 */
class widget() : AppWidgetProvider() {
    companion object {

        var saldoVisible: Boolean = false
        var total = 0L

    }

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

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent!!.action?: "") {
            "ICON_CLICKED_ACTION" -> {
                println(isUserLoggedIn())
                if (isUserLoggedIn()) {
                    updateSaldoWidget(context!!)
                } else {
                    val loginIntent = Intent(context, LoginActivity::class.java)
                    context?.sendBroadcast(loginIntent)
                    loginIntent.putExtra("fromWidget", true)
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(loginIntent)
                }
            }
            "LOGIN_SUCCESSFUL" -> {
                updateTextWidget(context!!, "$ 2000")
            }
            "LOGOFF_SUCCESSFUL" -> {
                userLogoff(context!!)
            }
            "CONFIG_CLICKED_ACTION" -> {
                println(isUserLoggedIn())
                if (isUserLoggedIn()) {
                    val homeIntent = Intent(context, MainActivity::class.java)
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(homeIntent)
                } else {
                    val loginIntent = Intent(context, LoginActivity::class.java)
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(loginIntent)
                }
            }
            "TEXT_GESTIONAR_CLICKED_ACTION" ->{
                if (isUserLoggedIn()) {
                    val homeIntent = Intent(context, MainActivity::class.java)
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(homeIntent)
                } else {
                    val loginIntent = Intent(context, LoginActivity::class.java)
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context?.startActivity(loginIntent)
                }
            }
        }
    }
    private fun updateTextWidget(context: Context, newText: String?) {
        totalInventario { newTotal ->
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget = ComponentName(context.packageName, javaClass.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)

            val views = RemoteViews(context.packageName, R.layout.widget)
            views.setImageViewResource(R.id.iconImageView, R.drawable.visibilidad_off)
            views.setTextViewText(R.id.text_saldo, "$ $newTotal")

            saldoVisible = true

            appWidgetManager.updateAppWidget(appWidgetIds, views)
        }
    }

    private fun isUserLoggedIn(): Boolean {
        // Verificar si el usuario está autenticado en Firebase
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.currentUser != null
    }

    private fun updateSaldoWidget(context: Context) {
        totalInventario { newTotal ->
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget = ComponentName(context.packageName, javaClass.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
            val views = RemoteViews(context.packageName, R.layout.widget)
            if (saldoVisible) {
                views.setImageViewResource(R.id.iconImageView, R.drawable.visibility_image)
                views.setTextViewText(R.id.text_saldo, "$ * * * *")
                saldoVisible = false
            } else {
                views.setImageViewResource(R.id.iconImageView, R.drawable.visibilidad_off)
                views.setTextViewText(R.id.text_saldo, "$ $newTotal")
                saldoVisible = true
            }

            // Actualizar todos los widgets
            appWidgetManager.updateAppWidget(appWidgetIds, views)
        }
    }

    private fun userLogoff(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget = ComponentName(context.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)

        val views = RemoteViews(context.packageName, R.layout.widget)
        views.setImageViewResource(R.id.iconImageView, R.drawable.visibility_image)
        views.setTextViewText(R.id.text_saldo, "$ * * * *")

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    private fun totalInventario(callback: (Long) -> Unit) {
        var total: Long = 0
        val db = FirebaseFirestore.getInstance()

        db.collection("articulo")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val price = document.get("precio") as Long
                    val quantity = document.get("cantidad") as Long

                    println("Cantidad: ${quantity.toInt()}")
                    println("Precio: ${price.toInt()}")

                    total += price * quantity
                }

                callback(total)
            }
            .addOnFailureListener { exception ->
                println("Error al obtener datos: $exception")
                // Manejar el error según sea necesario
            }
    }

    private fun pendingIntent(
        context: Context?,
        action:String
    ): PendingIntent? {
        val intent = Intent(context, javaClass)
        intent.action = action

        return PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val widgetText = context.getString(R.string.appwidget_text)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.widget)
        views.setOnClickPendingIntent(R.id.iconImageView, pendingIntent(context, "ICON_CLICKED_ACTION"))
        views.setOnClickPendingIntent(R.id.config_image, pendingIntent(context, "CONFIG_CLICKED_ACTION"))
        views.setOnClickPendingIntent(R.id.text_gestionarinventario, pendingIntent(context, "TEXT_GESTIONAR_CLICKED_ACTION"))


        //views.setTextViewText(R.id.appwidget_text, widgetText)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}


