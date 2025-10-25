package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SimpleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val textView = TextView(activity)
        textView.text = "Fragmento Simple de Prueba"
        textView.textSize = 24f // Hacer el texto más grande
        textView.gravity = android.view.Gravity.CENTER // Centrar el texto
        // Puedes cambiar el color para que contraste bien con tu tema
        textView.setBackgroundColor(android.graphics.Color.LTGRAY)
        textView.setTextColor(android.graphics.Color.BLACK)
        return textView
    }
}