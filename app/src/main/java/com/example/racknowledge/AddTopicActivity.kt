package com.example.racknowledge

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddTopicActivity : AppCompatActivity() {

    private lateinit var etTopicName: EditText
    private lateinit var etTopicDescription: EditText
    private lateinit var btnAddTopic: Button

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)

        firestore = FirebaseFirestore.getInstance()

        // Initialize views
        etTopicName = findViewById(R.id.etTopicName)
        etTopicDescription = findViewById(R.id.etTopicDescription)
        btnAddTopic = findViewById(R.id.btnAddTopic)

        btnAddTopic.setOnClickListener {
            val topicName = etTopicName.text.toString()
            val topicDescription = etTopicDescription.text.toString()

            if (topicName.isNotEmpty() && topicDescription.isNotEmpty()) {
                addTopicToFirestore(topicName, topicDescription)
            } else {
                Toast.makeText(this, "Please enter topic name and description", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTopicToFirestore(name: String, description: String) {
        val topic = hashMapOf(
            "name" to name,
            "description" to description
            // Agrega más campos si es necesario
        )

        firestore.collection("topics")
            .add(topic)
            .addOnSuccessListener {
                Toast.makeText(this, "Topic added successfully", Toast.LENGTH_SHORT).show()
                finish() // Cierra esta actividad después de agregar el tema
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add topic: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
