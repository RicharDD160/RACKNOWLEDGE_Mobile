package com.example.racknowledge

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class RegisterActivity : AppCompatActivity() {

    private lateinit var nombreInput: EditText
    private lateinit var apellidoInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var telefonoInput: EditText
    private lateinit var fechaNacimientoInput: EditText
    private lateinit var tipoDocumentoSpinner: Spinner
    private lateinit var numeroDocumentoInput: EditText
    private lateinit var rolSpinner: Spinner
    private lateinit var passwordInput: EditText
    private lateinit var registrarBtn: Button
    private lateinit var loginLink: TextView

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicialización de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Inicialización de vistas
        nombreInput = findViewById(R.id.name_input)
        apellidoInput = findViewById(R.id.surname_input)
        emailInput = findViewById(R.id.email_input)
        telefonoInput = findViewById(R.id.phone_input)
        fechaNacimientoInput = findViewById(R.id.dob_input)
        tipoDocumentoSpinner = findViewById(R.id.document_type_spinner)
        numeroDocumentoInput = findViewById(R.id.document_number_input)
        rolSpinner = findViewById(R.id.role_spinner)
        passwordInput = findViewById(R.id.password_input)
        registrarBtn = findViewById(R.id.register_button)
        loginLink = findViewById(R.id.login_link)

        // Configuración de adaptadores para spinners
        val tiposDocumento = resources.getStringArray(R.array.tipo_documento_options)
        val roles = resources.getStringArray(R.array.rol_options)

        val adapterTipoDocumento = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposDocumento)
        adapterTipoDocumento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoDocumentoSpinner.adapter = adapterTipoDocumento

        val adapterRol = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rolSpinner.adapter = adapterRol

        // Configuración del botón de registro
        registrarBtn.setOnClickListener {
            // Obtener valores de los campos de entrada y spinner
            val nombre = nombreInput.text.toString()
            val apellido = apellidoInput.text.toString()
            val email = emailInput.text.toString()
            val telefono = telefonoInput.text.toString()
            val fechaNacimiento = fechaNacimientoInput.text.toString()
            val tipoDocumento = tipoDocumentoSpinner.selectedItem.toString()
            val numeroDocumento = numeroDocumentoInput.text.toString()
            val rol = rolSpinner.selectedItem.toString()
            val password = passwordInput.text.toString()

            // Validar y procesar datos de registro aquí
            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || fechaNacimiento.isEmpty() || numeroDocumento.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Registrar usuario en Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Registro exitoso
                            val userId = firebaseAuth.currentUser?.uid
                            if (userId != null) {
                                // Guardar datos adicionales en Firebase Realtime Database
                                val usuario = Usuarios(nombre, apellido, email, telefono, fechaNacimiento, tipoDocumento, numeroDocumento, rol)
                                FirebaseDatabase.getInstance().reference.child("usuarios").child(userId).setValue(usuario)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            // Datos guardados exitosamente
                                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                            // Iniciar sesión automáticamente después del registro
                                            loginUser(email, password)
                                        } else {
                                            // Error al guardar datos en Firebase Database
                                            val dbException = dbTask.exception
                                            Toast.makeText(this, "Error al registrar usuario: ${dbException?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        } else {
                            // Error en la creación del usuario en Firebase Authentication
                            val authException = task.exception as? FirebaseAuthException
                            Toast.makeText(this, "Error al registrar usuario: ${authException?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Configuración del enlace "Volver al inicio de sesión"
        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, redirigir a la actividad principal u otra actividad deseada
                    navigateToMainActivity()
                } else {
                    // Error en el inicio de sesión
                    val authException = task.exception as? FirebaseAuthException
                    Toast.makeText(this, "Error al iniciar sesión: ${authException?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza esta actividad para que no se pueda volver atrás con el botón "Atrás"
    }
}

