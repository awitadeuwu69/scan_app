package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class cuenta : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private lateinit var profileCard: MaterialCardView
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        auth = FirebaseAuth.getInstance()
        setupAuthStateListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cuenta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileCard = view.findViewById(R.id.profileCard)
        nameText = view.findViewById(R.id.textView2)
        emailText = view.findViewById(R.id.textView5)


        val prefs = requireContext().getSharedPreferences("app_data", android.content.Context.MODE_PRIVATE)
        val savedMessages = prefs.getStringSet("forum_messages", emptySet()) ?: emptySet()

    }

    private fun setupAuthStateListener() {
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            updateUI(user)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            nameText.text = "Invitado"
            emailText.text = "Toca para iniciar sesión o registrarte"
            profileCard.setOnClickListener {
                LoginRegisterDialog().show(parentFragmentManager, "loginDialog")
            }
        } else {
            nameText.text = user.displayName ?: user.email?.substringBefore("@") ?: "Usuario"
            emailText.text = user.email
            profileCard.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Seguro que quieres cerrar sesión?")
                    .setPositiveButton("Sí") { _, _ ->
                        auth.signOut()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            cuenta().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
