package com.example.llm_project_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.chip.ChipGroup

class SignUpActivity4 : AppCompatActivity() {

    private lateinit var btn_back: ImageButton
    private lateinit var btn_clear: ImageButton
    private lateinit var btn_next: Button
    private lateinit var insurance_layout: ConstraintLayout
    private lateinit var insurance: RadioGroup
    private lateinit var insurance_Y: RadioButton
    private lateinit var insurance_N: RadioButton
    private lateinit var search_insurance: EditText
    private lateinit var tag_chip: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up_view4)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        btn_back: ImageButton
//        btn_clear: ImageButton
//        btn_next: Button
//        private lateinit var insurance: RadioGroup
//        private lateinit var insurance_Y: RadioButton
//        private lateinit var insurance_N: RadioButton
//        private lateinit var search_insurance: EditText
//        private lateinit var tag_chip: ChipGroup

        btn_back = findViewById<ImageButton>(R.id.backButton)
        btn_clear = findViewById<ImageButton>(R.id.text_clear)
        btn_next = findViewById<Button>(R.id.next_Button)
        insurance_layout = findViewById<ConstraintLayout>(R.id.insurance_layout)
        insurance = findViewById<RadioGroup>(R.id.radioInsurance)
        insurance_Y = findViewById<RadioButton>(R.id.insuranceYes)
        insurance_N = findViewById<RadioButton>(R.id.insuranceNo)
        search_insurance = findViewById<EditText>(R.id.search_insurance)
        tag_chip = findViewById<ChipGroup>(R.id.tagChipGroup)



    }
}