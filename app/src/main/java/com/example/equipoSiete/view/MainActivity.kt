
package com.example.equipoSiete.view
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.equipoSiete.R
import dagger.hilt.android.AndroidEntryPoint
import com.example.equipoSiete.view.fragment.HomeInventoryFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}