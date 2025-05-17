package com.example.loomi.ui.course.material.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.data.model.Material
import com.example.loomi.databinding.ActivityDetailMaterialBinding
import com.example.loomi.ui.adapter.DetailMaterialAdapter
import com.example.loomi.utils.Constants
import com.example.loomi.utils.ProgressManager
import com.google.firebase.auth.FirebaseAuth

class DetailMaterialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMaterialBinding
    private lateinit var adapter: DetailMaterialAdapter
    private lateinit var material : Material
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        material = intent.getParcelableExtra<Material>("MATERIAL_DATA") ?: return
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return


        binding.tvTitle.text = material.title
        Glide.with(this).load(material.imgResId).into(binding.imgBanner)

        adapter = DetailMaterialAdapter(
            material.docId,
            material.sections,
            emptyList(),
            userId
        )
        binding.rvMaterials.layoutManager = LinearLayoutManager(this)
        binding.rvMaterials.adapter = adapter

        loadProgress(userId, material.docId)

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun loadProgress(userId: String, materialId: String) {
        ProgressManager.fetchUserProgress(userId, materialId) { progress ->
            adapter.updateUserProgress(progress)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized && ::material.isInitialized && ::userId.isInitialized) {
            loadProgress(userId, material.docId)
        }
    }

}