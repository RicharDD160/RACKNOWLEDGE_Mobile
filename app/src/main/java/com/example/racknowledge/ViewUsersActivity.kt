package com.example.racknowledge

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ViewUsersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_users)

        recyclerView = findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firestore = FirebaseFirestore.getInstance()

        fetchUsers()
    }

    private fun fetchUsers() {
        firestore.collection("users").get().addOnSuccessListener { result ->
            val userList = ArrayList<User>()
            for (document in result) {
                val user = document.toObject(User::class.java)
                userList.add(user)
            }
            userAdapter = UserAdapter(userList)
            recyclerView.adapter = userAdapter
        }.addOnFailureListener {
            Toast.makeText(this, "Error al recuperar usuarios", Toast.LENGTH_SHORT).show()
        }
    }
}
