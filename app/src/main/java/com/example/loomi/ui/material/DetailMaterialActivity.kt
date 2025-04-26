package com.example.loomi.ui.material

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.data.model.Material
import com.example.loomi.databinding.ActivityDetailMaterialBinding
import com.example.loomi.ui.adapter.DetailMaterialAdapter


class DetailMaterialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMaterialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityDetailMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val material = intent.getParcelableExtra<Material>("MATERIAL_DATA") ?: return

        binding.tvTitle.text = material.title
        Glide.with(this).load(material.imgResId).into(binding.imgBanner)

        val adapter = DetailMaterialAdapter(material.id.toString(), material.sections)
        binding.rvMaterials.layoutManager = LinearLayoutManager(this)
        binding.rvMaterials.adapter = adapter

    }
}