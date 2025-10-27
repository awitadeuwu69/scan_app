package com.example.myapplication

import android.Manifest
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
import androidx.fragment.app.viewModels
import com.example.myapplication.databinding.FragmentAloBinding
import com.example.myapplication.ui.WeatherViewModel
import java.io.File
import java.io.IOException

class in2 : Fragment() {


    private var _binding: FragmentAloBinding? = null
    private val binding get() = _binding!!

    private var photoUri: Uri? = null
    private var navigationListener: NavigationListener? = null

    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    private val weatherViewModel: WeatherViewModel by viewModels()

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
    ): View {

        _binding = FragmentAloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnEscanear.setOnClickListener {
            checkCameraPermissionAndOpen()
        }

        binding.btnAyuda.setOnClickListener {
            Toast.makeText(requireContext(), "no quiero", Toast.LENGTH_LONG).show()
        }


        setupWeatherObservers()
        weatherViewModel.fetchWeatherForCity("Santiago")
    }
    
    private fun setupWeatherObservers() {
        weatherViewModel.weatherData.observe(viewLifecycleOwner) { weatherResponse ->

            binding.tvCityName.text = weatherResponse.location.name
            binding.tvTemperature.text = "${weatherResponse.current.tempC}°C"
            binding.tvWeatherDescription.text = weatherResponse.current.condition.text
        }

        weatherViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
        }
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

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}