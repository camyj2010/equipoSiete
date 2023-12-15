package com.example.equipoSiete.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.example.equipoSiete.R
import com.example.equipoSiete.databinding.FragmentItemDetailsBinding
import com.example.equipoSiete.model.Inventory
import com.example.equipoSiete.view.LoginActivity
import com.example.equipoSiete.view.MainActivity
import com.example.equipoSiete.viewmodel.InventoryViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {
    private lateinit var binding: FragmentItemDetailsBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private lateinit var receivedInventory: Inventory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInventory()
        controladores()
        goHome()
    }

    private fun controladores() {
        binding.btnDelete.setOnClickListener {
            deleteInventory()
        }

        binding.fbEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataInventory", receivedInventory)
            findNavController().navigate(R.id.action_itemDetailsFragment_to_itemEditFragment, bundle)
        }


    }

    private fun dataInventory() {
        val receivedBundle = arguments
        receivedInventory = receivedBundle?.getSerializable("clave") as Inventory

        binding.tvItem.text = "${receivedInventory.nombre}"
        binding.tvPrice.text = "$ ${receivedInventory.precio}"
        binding.tvQuantity.text = "${receivedInventory.cantidad}"
        binding.txtTotal.text = "$ ${
            inventoryViewModel.totalProducto(
                receivedInventory.precio,
                receivedInventory.cantidad
            )
        }"
    }

    private fun deleteInventory(){
        inventoryViewModel.deleteInventory(receivedInventory)
        inventoryViewModel.getListInventory()
        findNavController().popBackStack()
    }

    private fun goHome(){
        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}