package com.example.llm_project_android

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up_view1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_back = findViewById<ImageButton>(R.id.backButton)
        val btn_eye = findViewById<ImageButton>(R.id.eyeButton1)
        val btn_eye_check = findViewById<ImageButton>(R.id.eyeButton2)
        val id_text = findViewById<EditText>(R.id.id_editText)
        val pw_text = findViewById<EditText>(R.id.password_editText)
        val pw_check = findViewById<EditText>(R.id.check_editText)
        val email_text = findViewById<EditText>(R.id.email_editText)
        val id_rule = findViewById<TextView>(R.id.id_rule)
        val pw_rule = findViewById<TextView>(R.id.pw_rule)
        val pw_check_text = findViewById<TextView>(R.id.pw_check_text)
        val email_check = findViewById<TextView>(R.id.email_check)

    }
}