package com.example.loomi.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.loomi.MainActivity
import com.example.loomi.R
import com.example.loomi.data.model.OnBoarding
import com.example.loomi.databinding.ActivityOnBoardingBinding
import com.example.loomi.ui.adapter.OnBoardingAdapter
import kotlin.jvm.java

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private val viewModel: OnBoardingViewModel by viewModels()

    private lateinit var adapter: OnBoardingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.onboardingLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.onBoardingItems.observe(this, Observer { items ->
            adapter = OnBoardingAdapter(items)
            binding.viewPager.adapter = adapter
        })

        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem < adapter.itemCount - 1) {
                binding.viewPager.currentItem += 1
            } else {
                goMainPage()
            }
        }

        binding.btnSkip.setOnClickListener {
            goMainPage()
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text =
                    if (position == adapter.itemCount - 1) "Mulai Belajar" else "Selanjutnya"
            }
        })
    }

    private fun goMainPage() {
//        Toast.makeText(this, "Onboarding selesai!", Toast.LENGTH_SHORT).show()
         startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}