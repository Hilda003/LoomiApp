package com.example.loomi.ui.course.material.finish

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.loomi.R
import com.example.loomi.data.model.Section
import com.example.loomi.databinding.ActivityFinishBinding
import com.example.loomi.ui.course.material.content.ContentActivity
import com.example.loomi.utils.Constants
import com.example.loomi.utils.ContentParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding
    private lateinit var viewModel: FinishViewModel
    private lateinit var materialId: String
    private lateinit var currentSectionId: String
    private lateinit var userId: String
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FinishViewModel::class.java]

        currentSectionId = intent.getStringExtra(Constants.SECTION_ID) ?: ""
        materialId = intent.getStringExtra(Constants.MATERIAL_ID) ?: ""
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val currentTitle = intent.getStringExtra(Constants.CURRENT_TITLE) ?: ""
        val nextTitle = intent.getStringExtra(Constants.NEXT_TITLE)
        mediaPlayer = MediaPlayer.create(this, R.raw.cheer)
        mediaPlayer?.start()
        viewModel.markCurrentSectionCompleted(userId, materialId, currentSectionId)
        viewModel.fetchSections(materialId, currentSectionId, userId)
        binding.txtTitleMaterialNow.text = currentTitle
        if (nextTitle != null) {
            binding.txtNextMaterial.text = nextTitle
        }

        binding.icClose.setOnClickListener { finish() }

        animationConfetti()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.sectionData.observe(this) { (current, next) ->
            if (current != null) {
                updateUI(current, next)
            } else {
                Toast.makeText(this, getString(R.string.failed_to_load_material), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.contentToLoad.observe(this) { section ->
            val contentRef = FirebaseFirestore.getInstance().collection("materials")
                .document(materialId)
                .collection("sections")
                .document(section.docId)
                .collection("content")

            contentRef.get().addOnSuccessListener { snapshot ->
                val contents = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    ContentParser.parseContent(data)
                }

                val intent = Intent(this, ContentActivity::class.java).apply {
                    putExtra(Constants.SECTION_ID, section.docId)
                    putExtra(Constants.MATERIAL_ID, materialId)
                    putParcelableArrayListExtra(Constants.CONTENT_DATA, ArrayList(contents))
                }
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, getString(R.string.failed_to_load_content), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(current: Section, next: Section?) {
        binding.txtTitleMaterialNow.text = current.title
        binding.txtNextMaterial.text = next?.title ?: getString(R.string.no_continue_material)
        binding.btnNextMaterial.setOnClickListener {
            if (next != null) {
                viewModel.loadContent(next)
            } else {
                Toast.makeText(this, getString(R.string.no_continue_material), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun animationConfetti() {
        binding.viewConfetti.post {
            val konfetti = binding.viewConfetti
            val width = konfetti.width.toFloat()
            val height = konfetti.height.toFloat()
            val startY = height * 0.4f
            val endY = height * 0.6f
            val duration = 4000L

            MediaPlayer.create(this, R.raw.cheer).apply {
                start()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (isPlaying) {
                        stop()
                        release()
                    }
                }, duration)
            }

            konfetti.build()
                .addColors(Color.YELLOW, Color.MAGENTA, Color.CYAN)
                .setDirection(315.0)
                .setSpeed(6f, 9f)
                .setFadeOutEnabled(true)
                .setTimeToLive(duration)
                .addShapes(Shape.Circle, Shape.Square)
                .addSizes(Size(4), Size(6))
                .setPosition(0f, 0f, startY, endY)
                .streamFor(200, duration)

            konfetti.build()
                .addColors(Color.GREEN, Color.YELLOW, Color.BLUE)
                .setDirection(225.0)
                .setSpeed(6f, 9f)
                .setFadeOutEnabled(true)
                .setTimeToLive(duration)
                .addShapes(Shape.Circle, Shape.Square)
                .addSizes(Size(4), Size(6))
                .setPosition(width, width, startY, endY)
                .streamFor(200, duration)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
