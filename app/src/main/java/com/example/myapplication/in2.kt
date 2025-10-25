package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.IOException

class in2 : Fragment() {

    private lateinit var btnEscanear: MaterialButton
    private lateinit var btnAyuda: FloatingActionButton

    private var photoUri: Uri? = null
    private var navigationListener: NavigationListener? = null


    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationListener) {
            navigationListener = context
        } else {
            throw RuntimeException("$context must implement NavigationListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                try {

                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, photoUri)

                    navigationListener?.navigateToHistorial(photoUri.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error al procesar la imagen: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "Foto cancelada", Toast.LENGTH_SHORT).show()
                photoUri?.let { uri ->
                    try {
                        requireContext().contentResolver.delete(uri, null, null)
                    } catch (_: Exception) {}
                }
            }

            photoUri = null
        }

        // Launcher para permisos
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alo, container, false)

        btnEscanear = view.findViewById(R.id.btnEscanear)
        btnAyuda = view.findViewById(R.id.btnAyuda)

        btnEscanear.setOnClickListener {
            checkCameraPermissionAndOpen()
        }

        btnAyuda.setOnClickListener {
            Toast.makeText(requireContext(), "no quiero", Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun checkCameraPermissionAndOpen() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val imageFile = createImageFile()
        if (imageFile != null) {
            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                imageFile
            )
            takePictureLauncher.launch(photoUri)
        } else {
            Toast.makeText(requireContext(), "No se pudo crear el archivo temporal.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File? {
        val fileName = "temp_scan_${System.currentTimeMillis()}.png"
        val storageDir = requireContext().filesDir
        return try {
            File(storageDir, fileName).apply {
                createNewFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error I/O: ${e.message}", Toast.LENGTH_LONG).show()
            null
        }
    }
}