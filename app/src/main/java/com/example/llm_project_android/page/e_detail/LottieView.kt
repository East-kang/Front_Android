package com.example.llm_project_android.page.e_detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.llm_project_android.R
import com.example.llm_project_android.functions.getPassedExtras
import com.example.llm_project_android.functions.navigateTo

class LottieView : AppCompatActivity() {
    private lateinit var animationView: LottieAnimationView     // 애니메이션 진행 뷰
    private var progressAnimator: ValueAnimator ?= null         // 진행률 애니메이터

    private lateinit var data: Map<String, Any?>                // 기존, 비교 상품 정보

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.e_page_lottie_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 뒤로 가기 버튼 클릭 방지
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { }
        })
        
        data = getPassedExtras(
            listOf(
                "source" to String::class.java,
                "name" to String::class.java,       // 기존 상품명
                "name1" to String::class.java       // 비교 상품명
        ))

        animationView = findViewById(R.id.animationView)

        animationView.setAnimation(R.raw.loading)       // 애니메이션 파일 적용
        animationView.repeatCount = 0                   // 반복 재생x
        animationView.setMinAndMaxProgress(0f, 1f)      // 애니메이션 프레임(0~1)을 재생 대상으로 지정
        animationView.pauseAnimation()                  // 자동 재생 중단

        animationView.addLottieOnCompositionLoadedListener {
            startAnimation()                            // 로티 구성(Composition) 로딩 완료 후에만 애니메이션 시작
        }
    }

    /* 애니메이션 로직 */
    private fun startAnimation() {
        progressAnimator?.cancel()      // 이전 애니메이터 있으면 중단(중복 방지)
        progressAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3000     // 3초
            interpolator = LinearInterpolator() // 균일한 속도로 애니메이션 진행
            addUpdateListener { anim ->         // 매 프레임마다 로티의 진행도(progress)를 0→1로 업데이트
                animationView.progress = anim.animatedValue as Float
            }
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {  // 애니메이션 종료 후
                    if (!isFinishing) {
                        navigateTo(CompareViewActivity::class.java,
                            "source" to data["source"],
                            "source1" to "LottieView",
                            "name" to data["name"],             // 기존 상품명
                            "name1" to data["name1"]            // 비교 상품명
                        )
                        finish()
                    }
                }
            })
            start()     // 애니메이터 시작
        }
    }

    override fun onDestroy() {      // 수명 종료 시 애니메이터 해제(콜백/리소스 누수 방지)
        progressAnimator?.cancel()
        super.onDestroy()
    }
}