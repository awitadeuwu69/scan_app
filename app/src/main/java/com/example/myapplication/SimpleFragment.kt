package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var adapter: MessageAdapter
    private val messages = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foro, container, false)

        recyclerView = view.findViewById(R.id.recycler_messages)
        editMessage = view.findViewById(R.id.edit_message)
        btnSend = view.findViewById(R.id.btn_send)

        adapter = MessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        btnSend.setOnClickListener {
            val message = editMessage.text.toString().trim()

            if (message.isEmpty()) {
                editMessage.error = "Debes escribir un mensaje"
            } else {
                messages.add(message)
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                editMessage.text.clear()
            }
        }

        return view
    }
}