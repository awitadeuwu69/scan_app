package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.FileProvider

class Historial : Fragment() {


    private var currentImageUri: Uri? = null


    private lateinit var imageView: ImageView
    private lateinit var textView: TextView


    private val STATE_IMAGE_URI = "state_image_uri"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.getString(STATE_IMAGE_URI)?.let {
            currentImageUri = Uri.parse(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)


        imageView = view.findViewById(R.id.imageee)
        textView = view.findViewById(R.id.teto)


        currentImageUri?.let { uri ->
            displayImage(uri)
        } ?: run {
            textView.text = "Historial de Scaneos (Vacío). Escanea un documento."
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(STATE_IMAGE_URI, currentImageUri.toString())
    }


    fun loadImage(imageUriString: String) {
        val uri = Uri.parse(imageUriString)
        currentImageUri = uri


        if (::imageView.isInitialized) {
            displayImage(uri)
        }

    }


    private fun displayImage(uri: Uri) {
        try {

            imageView.setImageURI(null)
            imageView.setImageURI(uri)


            textView.text = "Escaneo Exitoso,Foto lista para analizar."


            textView.bringToFront()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al mostrar la imagen: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


}