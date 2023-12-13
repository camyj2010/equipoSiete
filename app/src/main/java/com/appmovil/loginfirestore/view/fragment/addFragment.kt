import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.appmovil.loginfirestore.databinding.FragmentAddProductBinding

class addFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButton()
        binding.saveButton.isEnabled = false
        // Configura los oyentes y la lógica según sea necesario.
    }



    private fun setupButton() {
        // Configura el botón (Criterio 6 y 7)
        binding.saveButton.setOnClickListener {
            if (camposEstanLlenos()) {
                // Lógica para guardar en Firestore y mostrar en la lista de productos (Criterio 8)
                guardarProductoEnFirestore()
            }
        }

        // Observador de cambios en los campos para habilitar/deshabilitar el botón
        binding.editTextProductCode.addTextChangedListener(textWatcher)
        binding.editArticleName.addTextChangedListener(textWatcher)
        binding.editPrice.addTextChangedListener(textWatcher)
        binding.editQuantity.addTextChangedListener(textWatcher)
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
        val codigo = binding.editTextProductCode.text.toString()
        val nombre = binding.editArticleName.text.toString()
        val precio = binding.editPrice.text.toString()
        val cantidad = binding.editQuantity.text.toString()

        return codigo.isNotEmpty() && nombre.isNotEmpty() && precio.isNotEmpty() && cantidad.isNotEmpty()
    }

    private fun guardarProductoEnFirestore() {
        val codigo = binding.editTextProductCode.text.toString()
        val nombre = binding.editArticleName.text.toString()
        val precio = binding.editPrice.text.toString()
        val cantidad = binding.editQuantity.text.toString()

        // Aquí deberías implementar la lógica para guardar en Firestore
        // por ejemplo, creando un objeto Producto y agregándolo a la colección correspondiente.
    }
}
