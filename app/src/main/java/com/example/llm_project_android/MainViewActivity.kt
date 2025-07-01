package com.example.llm_project_android

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.llm_project_android.databinding.MainViewBinding

class MainViewActivity : AppCompatActivity() {
    private lateinit var binding: MainViewBinding
    private val sliderHandler = android.os.Handler(android.os.Looper.getMainLooper())
    private lateinit var sliderRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 바인딩 초기화
        binding = MainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bannerList = listOf(
            R.drawable.resize_main_icon,
            R.drawable.sample_image,
            R.drawable.image_name_icon
        )

        val adapter  = ViewPageAdapter(bannerList)
        binding.bannerViewPager.adapter = adapter

        // 배너 슬라이딩 구현
        auto_slide_banner(bannerList)


    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    // 배너 슬라이딩 함수
    fun auto_slide_banner(bannerList: List<Int>) {
        sliderRunnable = object : Runnable {
            override fun run() {
                val currentItem = binding.bannerViewPager.currentItem
                val nextItem = if (currentItem > 0) currentItem -1
                               else bannerList.lastIndex

                binding.bannerViewPager.setCurrentItem(nextItem, true)
                sliderHandler.postDelayed(sliderRunnable, 3000) // 3초마다 슬라이드
            }
        }
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }
}