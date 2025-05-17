package com.example.loomi.ui.course.material.material

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loomi.databinding.FragmentMaterialBinding
import com.example.loomi.ui.adapter.MaterialAdapter
import com.example.loomi.ui.home.UserProgressViewModel
import com.example.loomi.ui.course.material.detail.DetailMaterialActivity
import com.example.loomi.utils.ProgressManager
import com.google.firebase.auth.FirebaseAuth

class MaterialFragment : Fragment() {

    private var _binding: FragmentMaterialBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MaterialViewModel>()
    private val userProgressViewModel by viewModels<UserProgressViewModel>()
    private lateinit var adapter: MaterialAdapter

    companion object {
        private const val TAG = "MaterialFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModels()
        showLoading(true)
    }

    private fun setupRecyclerView() {
        adapter = MaterialAdapter(
            materials = emptyList(),
            onItemClick = { material ->
                val intent = Intent(requireContext(), DetailMaterialActivity::class.java)
                intent.putExtra("MATERIAL_DATA", material)
                intent.putExtra("USER_ID", FirebaseAuth.getInstance().currentUser?.uid)
                intent.putParcelableArrayListExtra(
                    "USER_PROGRESS_LIST",
                    ArrayList(adapter.getUserProgressForMaterial(material.docId))
                )
                startActivity(intent)
            },
            userProgress = emptyList()
        )

        binding.rvCourse.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MaterialFragment.adapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
        }
    }

    private fun observeViewModels() {
        viewModel.materials.observe(viewLifecycleOwner) { materials ->
            showLoading(false)
            if (materials == null) {
                return@observe
            } else if (materials.isEmpty()) {
                return@observe
            } else {
                Log.d(TAG, "Materials fetched: ${materials.size}")
            }

            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser == null) {
                adapter.updateData(materials)
                return@observe
            }

            val userId = firebaseUser.uid
            adapter.updateData(materials)

            if (materials.isNotEmpty()) {
                ProgressManager.fetchUserProgress(userId, materials.first().docId) { allProgress ->
                    requireActivity().runOnUiThread {
                        adapter.updateUserProgress(allProgress)
                    }
                }
            }
        }
        userProgressViewModel.userProgress.observe(viewLifecycleOwner) { progress ->
            if (progress != null && progress.isNotEmpty()) {
                adapter.updateUserProgress(progress)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            userProgressViewModel.fetchUserProgress(firebaseUser.uid)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}