package com.example.loomi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.loomi.databinding.ActivityMainBinding
import com.example.loomi.ui.course.FragmentCourse
import com.example.loomi.ui.home.HomeFragment
import com.example.loomi.ui.course.material.material.MaterialFragment
import com.example.loomi.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleNavigationIntent(intent)
        setFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_home -> setFragment(HomeFragment())
                R.id.bottom_course -> setFragment(FragmentCourse())
                R.id.bottom_profile -> setFragment(ProfileFragment())
            }
            true
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.let {
            handleNavigationIntent(it)
        }
    }

    private fun handleNavigationIntent(intent: Intent) {
        val navigateTo = intent.getStringExtra("NAVIGATE_TO")

        val fragment = when (navigateTo) {
            "MATERIAL_FRAGMENT" -> MaterialFragment()
            else -> HomeFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_fragment, fragment)
            .commit()
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_fragment, fragment)
            .commit()
    }
}
