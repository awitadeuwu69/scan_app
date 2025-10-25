package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding
// Asegúrate de que todas las clases de fragmentos estén importadas si no están en el mismo paquete,
// o si quieres ser explícito. Si están en el mismo paquete com.example.myapplication,
// las importaciones directas no son estrictamente necesarias pero no hacen daño.
import com.example.myapplication.AjustesFragment // Importación añadida para claridad
import com.example.myapplication.Historial
import com.example.myapplication.in2
import com.example.myapplication.cuenta
import com.example.myapplication.ForoFragment


class MainActivity : AppCompatActivity(), NavigationListener {

    private lateinit var binding: ActivityMainBinding


    private val historialFragment = Historial()
    private val in2Fragment = in2()


    private val cuentaFragment = cuenta()
    private val ajustesFragment = AjustesFragment()
    private val foroFragment = ForoFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.foro -> foroFragment
                R.id.cuenta -> cuentaFragment
                R.id.ajustes -> ajustesFragment
                R.id.inicio -> in2Fragment
                R.id.historial -> historialFragment
                else -> cuentaFragment
            }
            replaceFragment(selectedFragment)
            true
        }


        if (savedInstanceState == null) {
            replaceFragment(in2Fragment)

            binding.bottomNavigationView.selectedItemId = R.id.inicio
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }


    override fun navigateToHistorial(imageUriString: String) {


        historialFragment.loadImage(imageUriString)


        binding.bottomNavigationView.selectedItemId = R.id.historial
    }
}
