package com.example.equipoSiete.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.equipoSiete.R
import com.example.equipoSiete.databinding.FragmentHomeInventoryBinding
import com.example.equipoSiete.view.LoginActivity
import com.example.equipoSiete.view.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.example.equipoSiete.view.adapter.InventoryAdapter
import com.example.equipoSiete.viewmodel.InventoryViewModel
import com.example.equipoSiete.widget.widget
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeInventoryFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentHomeInventoryBinding
    private val inventoryViewModel: InventoryViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeInventoryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        dataLogin()
        controladores()
        observadorViewModel()
        setup()

    }

    private fun controladores() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeInventoryFragment_to_addItemFragment)
        }

    }

    private fun observadorViewModel(){
        observerListInventory()
        observerProgress()

    }

    private fun observerListInventory(){

        inventoryViewModel.getListInventory()
        inventoryViewModel.listInventory.observe(viewLifecycleOwner){ listInventory ->
            val recycler = binding.recyclerview
            val layoutManager =LinearLayoutManager(context)
            recycler.layoutManager = layoutManager
            val adapter = InventoryAdapter(listInventory, findNavController())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

        }

    }


    private fun observerProgress(){
        inventoryViewModel.progresState.observe(viewLifecycleOwner){status ->
            binding.progress.isVisible = status
        }
    }
    private fun dataLogin() {
        val bundle = requireActivity().intent.extras
        val email = bundle?.getString("email")
        sharedPreferences.edit().putString("email",email).apply()
    }

    private fun setup() {
        binding.toolbarinclude.btnLogOut.setOnClickListener {
            logOut()
        }

    }
    private fun logOut() {
        sharedPreferences.edit().clear().apply()
        FirebaseAuth.getInstance().signOut()
        (requireActivity() as MainActivity).apply {
            val widgetIntent = Intent(this, widget::class.java)
            widgetIntent.action = "LOGOFF_SUCCESSFUL"
            sendBroadcast(widgetIntent)
            (requireActivity() as MainActivity).apply {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

}