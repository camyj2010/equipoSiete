package com.example.equipoSiete.view.fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.equipoSiete.R
import com.example.equipoSiete.databinding.FragmentItemEditBinding
import com.example.equipoSiete.model.Inventory
import com.example.equipoSiete.view.MainActivity
import com.example.equipoSiete.viewmodel.InventoryViewModel
import com.example.equipoSiete.widget.widget
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemEditFragment : Fragment() {
    private lateinit var binding: FragmentItemEditBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedInventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemEditBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInventory()
        setupButton()
        goHome()
    }

    private fun setupButton() {
        // Configura el botón (Criterio 6 y 7)
        binding.editButton.setOnClickListener {
            if (camposEstanLlenos()) {
                // Lógica para guardar en Firestore y mostrar en la lista de productos (Criterio 8)
                updateInventory()
            }
        }

        // Observador de cambios en los campos para habilitar/deshabilitar el botón
        binding.editArticleName.addTextChangedListener(textWatcher)
        binding.editPrice.addTextChangedListener(textWatcher)
        binding.editQuantity.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            // Verificar si todos los campos están llenos y habilitar/deshabilitar el botón (Criterios 6 y 7)
            binding.editButton.isEnabled = camposEstanLlenos()
            // Cambiar el color y estilo del texto del botón si está habilitado (Criterio 7)
            binding.editButton.setTextColor(if (binding.editButton.isEnabled) resources.getColor(android.R.color.white) else resources.getColor(android.R.color.black))
            binding.editButton.setTypeface(null, if (binding.editButton.isEnabled) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    private fun camposEstanLlenos(): Boolean {
        val nombre = binding.editArticleName.text.toString()
        val precio = binding.editPrice.text.toString()
        val cantidad = binding.editQuantity.text.toString()
        return nombre.isNotEmpty() && precio.isNotEmpty() && cantidad.isNotEmpty()
    }

    private fun dataInventory(){
        val receivedBundle = arguments
        receivedInventory = receivedBundle?.getSerializable("dataInventory") as Inventory
        binding.textProductCode.setText("Id: "+receivedInventory.codigo)
        binding.editArticleName.setText(receivedInventory.nombre)
        binding.editPrice.setText(receivedInventory.precio.toString())
        binding.editQuantity.setText(receivedInventory.cantidad.toString())

    }

    private fun updateInventory(){
        val name = binding.editArticleName.text.toString()
        val price = binding.editPrice.text.toString().toInt()
        val quantity = binding.editQuantity.text.toString().toInt()
        val inventory = Inventory(receivedInventory.codigo, name,price,quantity)
        inventoryViewModel.updateInventory(inventory)
        Toast.makeText(context,"Artículo editado con exito", Toast.LENGTH_SHORT).show()
        (requireActivity() as MainActivity).apply {
            val widgetIntent = Intent(this, widget::class.java)
            widgetIntent.action = "UPDATE_TOTAL"
            sendBroadcast(widgetIntent)
        }
        val bundle = Bundle()
        bundle.putSerializable("clave", inventory)
        //findNavController().navigate(R.id.action_itemEditFragment_to_itemDetailsFragment,bundle)

    }

    private fun goHome(){
        binding.backArrowEdit.setOnClickListener{
            findNavController().navigateUp()
        }
    }

}