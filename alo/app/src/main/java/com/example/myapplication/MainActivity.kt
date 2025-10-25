package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {





    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita la visualización de borde a borde

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajusta el padding para las barras del sistema (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets -> // Usar binding.main si 'main' es el ID del ConstraintLayout raíz en activity_main.xml
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Listener del BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.foro -> SimpleFragment()       // Instancia del Fragmento
                R.id.cuenta -> cuenta()   // Instancia del Fragmento
                R.id.ajustes -> ajustes() // Instancia del Fragmento
                else -> cuenta() // Fragmento por defecto o maneja el error
            }
            replaceFragment(selectedFragment)
            true // Indica que el evento ha sido manejado
        }

        // Establecer el fragmento inicial
        if (savedInstanceState == null) {
            replaceFragment(cuenta())
            // Marcar el item de inicio como seleccionado en el BottomNavigationView si es necesario
            // binding.bottomNavigationView.selectedItemId = R.id.inicio // Asumiendo que tienes un ID 'inicio' para el fragmento inicial
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        // Reemplaza el contenido del FrameLayout con el nuevo fragmento
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        // Opcional: añade la transacción al back stack si quieres que el usuario pueda volver al fragmento anterior con el botón de retroceso
        // fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit() // Aplica los cambios
    }
}








///package com.example.myapplication
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.fragment.app.Fragment
//import com.example.myapplication.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() // Habilita la visualización de borde a borde
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Ajusta el padding para las barras del sistema (status bar, navigation bar)
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets -> // Usar binding.main si 'main' es el ID del ConstraintLayout raíz en activity_main.xml
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        // Listener del BottomNavigationView
//        binding.bottomNavigationView.setOnItemSelectedListener { item ->
//            val selectedFragment: Fragment = when (item.itemId) {
//                R.id.foro -> foro()       // Instancia del Fragmento
//                R.id.cuenta -> cuenta()   // Instancia del Fragmento
//                R.id.ajustes -> ajustes() // Instancia del Fragmento
//                else -> inicio() // Fragmento por defecto o maneja el error
//            }
//            replaceFragment(selectedFragment)
//            true // Indica que el evento ha sido manejado
//        }
//
//
//    }
//
//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        // Reemplaza el contenido del FrameLayout con el nuevo fragmento
//        fragmentTransaction.replace(R.id.frame_layout, fragment)
//        // Opcional: añade la transacción al back stack si quieres que el usuario pueda volver al fragmento anterior con el botón de retroceso
//        // fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit() // Aplica los cambios
//    }
//}