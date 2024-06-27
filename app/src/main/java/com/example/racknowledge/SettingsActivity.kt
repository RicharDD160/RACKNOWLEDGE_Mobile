package com.example.racknowledge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var radioLightTheme: RadioButton
    private lateinit var radioDarkTheme: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val uploadPhotoButton = findViewById<Button>(R.id.btn_upload_photo)
        val themeSelector = findViewById<RadioGroup>(R.id.theme_selector)
        radioLightTheme = findViewById(R.id.radio_light_theme)
        radioDarkTheme = findViewById(R.id.radio_dark_theme)

        uploadPhotoButton.setOnClickListener {
            openGalleryForImage()
        }

        themeSelector.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_light_theme -> setLightTheme()
                R.id.radio_dark_theme -> setDarkTheme()
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            // Aquí puedes manejar la URI de la imagen seleccionada
        }
    }

    private fun setLightTheme() {
        // Implementar lógica para aplicar tema claro
        // Ejemplo:
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setDarkTheme() {
        // Implementar lógica para aplicar tema oscuro
        // Ejemplo:
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 100
    }
}
