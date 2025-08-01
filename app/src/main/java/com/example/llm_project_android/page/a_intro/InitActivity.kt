package com.example.llm_project_android.page.a_intro

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.llm_project_android.R
import com.example.llm_project_android.db.Users.MyDatabase
import com.example.llm_project_android.functions.navigateTo
import com.example.llm_project_android.functions.registerExitDialogOnBackPressed
import com.example.llm_project_android.functions.resetUserTable
import com.example.llm_project_android.functions.saveUserInfo
import com.example.llm_project_android.page.b_signup.SignUpActivity1
import com.example.llm_project_android.page.c_product.MainViewActivity
import kotlinx.coroutines.launch

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.a_page_init_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_login = findViewById<Button>(R.id.login_Button)         // 로그인 버튼 선언
        val btn_sign_up = findViewById<Button>(R.id.sign_up_Button)     // 회원가입 버튼 선언
        val view = findViewById<ImageView>(R.id.image)


        view.setOnClickListener {
            lifecycleScope.launch {
                resetUserTable(this@InitActivity)
            }
        }

        // 로그인 버튼 클릭 이벤트
        btn_login.setOnClickListener {
            lifecycleScope.launch {
                saveUserInfo(
                    context = this@InitActivity,
                    userId = "jojojojo12",
                    email = "as@as.com",
                    name = "길동",
                    birthDate = "19991111",
                    phoneNumber = "010-2222-1111",
                    gender = "남성",
                    isMarried = true,
                    job = "오리"
                )
            }
            // navigateTo(LoginActivity::class.java)
            navigateTo(MainViewActivity::class.java)
        }

        // 회원가입 버튼 클릭 이벤트
        btn_sign_up.setOnClickListener {
            // 내부 DB 데이터 초기화
            lifecycleScope.launch {
                val dao = MyDatabase.getDatabase(this@InitActivity).getMyDao()
                dao.deleteAllUsers()
            }

            //화면 전환
            navigateTo(SignUpActivity1::class.java, "source" to "InitActivity")
        }

        // 뒤로가기 버튼 클릭 이벤트
        registerExitDialogOnBackPressed()
    }
}