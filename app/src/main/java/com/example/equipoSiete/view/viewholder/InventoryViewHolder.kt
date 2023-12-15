package com.example.equipoSiete.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.equipoSiete.R
import com.example.equipoSiete.databinding.ItemInventoryBinding
import com.example.equipoSiete.model.Inventory
import java.text.NumberFormat
import java.util.Locale

class InventoryViewHolder(binding: ItemInventoryBinding, navController: NavController) :
    RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding
    val navController = navController
    fun setItemInventory(inventory: Inventory) {
        val formattedPrice = convertToFormattedCurrency(inventory.precio.toDouble())
        bindingItem.articleName.text = inventory.nombre
        bindingItem.articlePrice.text = "$ ${inventory.precio}"
        bindingItem.articleId.text = "${inventory.codigo}"


        bindingItem.cvInventory.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", inventory)
            navController.navigate(R.id.action_homeInventoryFragment_to_itemDetailsFragment, bundle)
        }

    }
    private fun convertToFormattedCurrency(amount: Double): String {
        val currencyFormatter = NumberFormat.getNumberInstance(Locale("es", "ES"))
        currencyFormatter.minimumFractionDigits = 2
        currencyFormatter.maximumFractionDigits = 2
        return currencyFormatter.format(amount)
    }
}