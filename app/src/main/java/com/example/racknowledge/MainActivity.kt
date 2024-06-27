package com.example.racknowledge

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var loginBtn: Button
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerTextView: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Initialize views
        loginBtn = findViewById(R.id.login_btn)
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        registerTextView = findViewById(R.id.register_textview)

        // Setup login button click listener
        loginBtn.setOnClickListener {
            val email = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                showAlert("Error", "Please enter email and password.")
            }
        }

        // Setup register text view click listener
        registerTextView.setOnClickListener {
            openRegisterActivity()
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to StudentActivity
                    navigateToStudentActivity()
                } else {
                    // Login failed, display error message to user
                    showAlert("Error", "Incorrect credentials.")
                }
            }
    }

    private fun navigateToStudentActivity() {
        val intent = Intent(this, StudentActivity::class.java)
        startActivity(intent)
        finish() // Finish this activity so user cannot go back with the back button
    }

    private fun openRegisterActivity() {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
}
