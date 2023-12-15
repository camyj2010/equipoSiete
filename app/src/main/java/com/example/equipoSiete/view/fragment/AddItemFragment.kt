package com.example.equipoSiete.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.equipoSiete.R
import com.example.equipoSiete.databinding.FragmentAddItemBinding
import com.example.equipoSiete.model.Inventory
import com.example.equipoSiete.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        goHome()

    }

    private fun saveInvetory(){
        val codigo = binding.addTextProductCode.text.toString().toInt()
        val nombre = binding.addArticleName.text.toString()
        val precio = binding.addPrice.text.toString().toInt()
        val cantidad = binding.addQuantity.text.toString().toInt()
        val inventory = Inventory(codigo = codigo, nombre = nombre, precio = precio, cantidad = cantidad)
        inventoryViewModel.saveInventory(inventory)
        Log.d("test",inventory.toString())
        Toast.makeText(context,"Artículo guardado", Toast.LENGTH_SHORT).show()
        limpiarCampos()

    }

    private fun goHome(){
        binding.backArrowAdd.setOnClickListener{
            findNavController().navigate(R.id.action_addItemFragment_to_homeInventoryFragment)
        }
    }

    private fun setupButton() {
        // Configura el botón (Criterio 6 y 7)
        binding.saveButton.setOnClickListener {
            if (camposEstanLlenos()) {
                // Lógica para guardar en Firestore y mostrar en la lista de productos (Criterio 8)
                saveInvetory()
            }
        }

        // Observador de cambios en los campos para habilitar/deshabilitar el botón
        binding.addTextProductCode.addTextChangedListener(textWatcher)
        binding.addArticleName.addTextChangedListener(textWatcher)
        binding.addPrice.addTextChangedListener(textWatcher)
        binding.addQuantity.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            // Verificar si todos los campos están llenos y habilitar/deshabilitar el botón (Criterios 6 y 7)
            binding.saveButton.isEnabled = camposEstanLlenos()
            // Cambiar el color y estilo del texto del botón si está habilitado (Criterio 7)
            binding.saveButton.setTextColor(if (binding.saveButton.isEnabled) resources.getColor(android.R.color.white) else resources.getColor(android.R.color.black))
            binding.saveButton.setTypeface(null, if (binding.saveButton.isEnabled) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    private fun camposEstanLlenos(): Boolean {
        val codigo = binding.addTextProductCode.text.toString()
        val nombre = binding.addArticleName.text.toString()
        val precio = binding.addPrice.text.toString()
        val cantidad = binding.addQuantity.text.toString()

        return codigo.isNotEmpty() && nombre.isNotEmpty() && precio.isNotEmpty() && cantidad.isNotEmpty()
    }

    private fun limpiarCampos() {
        binding.addArticleName.setText("")
        binding.addTextProductCode.setText("")
        binding.addPrice.setText("")
        binding.addQuantity.setText("")
    }


}