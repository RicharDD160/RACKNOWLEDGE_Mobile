package com.example.racknowledge

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserCrudActivity : AppCompatActivity(), UserAdapter.UserActionListener {

    private lateinit var btnAddUser: Button
    private lateinit var btnViewUsers: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_crud)

        btnAddUser = findViewById(R.id.btnAddUser)
        btnViewUsers = findViewById(R.id.btnViewUsers)
        recyclerView = findViewById(R.id.recyclerView)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(emptyList())
        recyclerView.adapter = userAdapter

        btnAddUser.setOnClickListener {
            showAddUserDialog()
        }

        btnViewUsers.setOnClickListener {
            fetchUsers()
        }
    }

    private fun showAddUserDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Agregar Usuario")

        val etUserName = dialogView.findViewById<EditText>(R.id.etDialogUserName)
        val etUserEmail = dialogView.findViewById<EditText>(R.id.etDialogUserEmail)
        val spinnerTipoDocumento = dialogView.findViewById<Spinner>(R.id.spinnerTipoDocumento)
        val spinnerRol = dialogView.findViewById<Spinner>(R.id.spinnerRol)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnDialogAddUser)

        val dialog = dialogBuilder.create()
        dialog.show()

        btnAdd.setOnClickListener {
            val name = etUserName.text.toString()
            val email = etUserEmail.text.toString()
            val tipoDocumento = spinnerTipoDocumento.selectedItem.toString()
            val rol = spinnerRol.selectedItem.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                addUser(name, email, tipoDocumento, rol)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUser(name: String, email: String, tipoDocumento: String, rol: String) {
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "tipoDocumento" to tipoDocumento,
            "rol" to rol
        )

        firebaseAuth.createUserWithEmailAndPassword(email, "defaultPassword").addOnCompleteListener {
            if (it.isSuccessful) {
                val userId = firebaseAuth.currentUser?.uid
                if (userId != null) {
                    firestore.collection("users").document(userId).set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Fallo al agregar usuario", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Fallo al registrar usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUsers() {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userList = mutableListOf<User>()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val email = document.getString("email") ?: ""
                    val tipoDocumento = document.getString("tipoDocumento") ?: ""
                    val rol = document.getString("rol") ?: ""

                    val user = User(name, email, tipoDocumento, rol)
                    userList.add(user)
                }
                userAdapter.setUserList(userList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Fallo al obtener usuarios: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onUpdateUser(user: User) {
        // Implementación de la actualización del usuario
    }

    override fun onDeleteUser(user: User) {
        // Implementación de la eliminación del usuario
    }
}
