package com.example.loomi.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.loomi.R
import com.example.loomi.databinding.FragmentHomeBinding
import com.example.loomi.ui.article.ActivityArticle
import com.example.loomi.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.WHITE)

        displayUserInfo()
        val userId = auth.currentUser?.uid

        if (userId != null) {
            viewModel.fetchOverallProgress(userId)

        }

        viewModel.fetchLatestArticle()

        observeViewModel()
        binding.notification.setOnClickListener {
            Toast(requireContext()).apply {
                setText("Fitur masih dalam tahap pengembangan")

                show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.overallProgress.observe(viewLifecycleOwner) { progressInfo ->
            val progressBar = binding.progressBar2
            val progressText = binding.textProgressPercentage
            val circularProgress = binding.progressBar2

            if (progressInfo != null) {
                val (completed, total, percentage) = progressInfo
                progressBar.progress = percentage
                progressText.text = "$completed of $total"

                circularProgress.apply {
                    visibility = View.VISIBLE
                    max = 100
                    progress = percentage
                }

                progressBar.progressTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.green
                )

                if (percentage > 0) {
                    binding.textView14.text = getString(R.string.your_progress_is_amazing)
                    binding.textView15.text = getString(
                        R.string.keep_up_the_good_work)
                } else {
                    binding.textView14.text = "Belum ada progres"
                    binding.textView15.text = "Yuk mulai belajar dari awal!"
                }

                Log.d("HomeFragment", "Progress: $completed/$total sections = $percentage%")
            }
        }

        viewModel.latestArticle.observe(viewLifecycleOwner) { article ->
            if (article != null) {
                binding.itemArticle.tvTitleArticle.text = article.title
                Glide.with(this)
                    .load(article.coverImage)
                    .into(binding.itemArticle.courseImg)
                binding.tvSeeAll.setOnClickListener {
                    startActivity(Intent(requireContext(), ActivityArticle::class.java))
                }
            }
        }
    }

    private fun displayUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            binding.txtName.text = user.displayName ?: "No Name"
        } else {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}