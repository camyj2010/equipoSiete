package com.example.equipoSiete.repository
import android.widget.Toast
import com.example.equipoSiete.data.InventoryDao
import com.example.equipoSiete.model.Inventory
import com.example.equipoSiete.model.Product
import com.example.equipoSiete.webservice.ApiService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InventoryRepository  @Inject constructor(
    private val inventoryDao: InventoryDao,
    private val apiService: ApiService,
    private val db: FirebaseFirestore,
){

    suspend fun saveInventory(inventory:Inventory) {
        withContext(Dispatchers.IO) {
           try {
                    db.collection("articulo").document(inventory.codigo.toString()).set(
                        hashMapOf(
                            "codigo" to inventory.codigo,
                            "nombre" to inventory.nombre,
                            "precio" to inventory.precio,
                            "cantidad" to inventory.cantidad
                        )
                    ).await()

            } catch (e: Exception) {
               e.printStackTrace()
           }
        }
    }



    suspend fun getListInventory():MutableList<Inventory>{
        return withContext(Dispatchers.IO){
            try {
                val snapshot = db.collection("articulo").get().await()
                val inventoryList = mutableListOf<Inventory>()
                for (document in snapshot.documents) {
                    val codigo = document.getLong("codigo")?.toInt() ?: 0
                    val nombre = document.getString("nombre") ?: ""
                    val precio = document.getLong("precio")?.toInt() ?: 0
                    val cantidad = document.getLong("cantidad")?.toInt() ?: 0

                    val item = Inventory(codigo, nombre, precio, cantidad)
                    inventoryList.add(item)
                }

                inventoryList
            } catch (e: Exception) {
                e.printStackTrace()
                mutableListOf()
            }
        }
    }



    suspend fun deleteInventory(inventory: Inventory) {
        withContext(Dispatchers.IO) {
            try {
                // Aquí asumo que 'codigo' es un identificador único para Inventory
                val codigo = inventory.codigo

                // Obtener una referencia al documento que queremos eliminar
                val documentReference = db.collection("articulo").document(codigo.toString())

                // Eliminar el documento
                documentReference.delete().await()
            } catch (e: Exception) {
                e.printStackTrace()
                // Manejar la excepción según tus necesidades
            }
        }
    }

    suspend fun updateRepositoy(inventory: Inventory){
        withContext(Dispatchers.IO) {
            try {
                db.collection("articulo").document(inventory.codigo.toString()).update(
                    mapOf(
                        "nombre" to inventory.nombre,
                        "precio" to inventory.precio,
                        "cantidad" to inventory.cantidad
                    )
                ).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getProducts(): MutableList<Product> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProducts()
                response
            } catch (e: Exception) {

                e.printStackTrace()
                mutableListOf()
            }
        }
    }
}