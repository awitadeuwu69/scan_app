package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class LoginRegisterDialog : DialogFragment() {

    private lateinit var auth: FirebaseAuth
    private var isRegisterMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_login_register, null)
        builder.setView(view)

        auth = FirebaseAuth.getInstance()

        val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
        val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val verifyPasswordEditText = view.findViewById<EditText>(R.id.verifyPasswordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            if (isRegisterMode) {
                isRegisterMode = false
                titleTextView.text = "Iniciar Sesión / Registrarse"
                usernameEditText.visibility = View.GONE
                verifyPasswordEditText.visibility = View.GONE
                loginButton.text = "Iniciar sesión"
                registerButton.text = "Registrarse"
            } else {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                dismiss()
                            } else {
                                Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerButton.setOnClickListener {
            if (!isRegisterMode) {
                isRegisterMode = true
                titleTextView.text = "Registrarse"
                usernameEditText.visibility = View.VISIBLE
                verifyPasswordEditText.visibility = View.VISIBLE
                loginButton.text = "Cancelar"
                registerButton.text = "Confirmar registro"
            } else {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                val username = usernameEditText.text.toString()
                val verifyPassword = verifyPasswordEditText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty() && verifyPassword.isNotEmpty()) {
                    if (password == verifyPassword) {
                        if (isPasswordValid(password)) {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build()

                                        user?.updateProfile(profileUpdates)
                                            ?.addOnCompleteListener { profileTask ->
                                                if (profileTask.isSuccessful) {
                                                    Toast.makeText(requireContext(), "Registro exitoso y perfil actualizado", Toast.LENGTH_SHORT).show()
                                                    dismiss()
                                                } else {
                                                    Toast.makeText(requireContext(), "Registro exitoso, pero no se pudo actualizar el perfil: ${profileTask.exception?.message}", Toast.LENGTH_LONG).show()
                                                    dismiss()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(requireContext(), "Error en el registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(requireContext(), "La contraseña debe contener al menos un dígito y un carácter especial.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Completa todos los campos.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return builder.create()
    }

    private fun isPasswordValid(password: String): Boolean {
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val isLongEnough = password.length >= 6
        return hasDigit && hasSpecialChar && isLongEnough
    }
}
