package com.example.loomi.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loomi.R
import com.example.loomi.databinding.FragmentBottomSheetResultBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetResult : BottomSheetDialogFragment() {

    private var isSuccess: Boolean = false
    private var onDismissAction: (() -> Unit)? = null
    private var _binding: FragmentBottomSheetResultBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_IS_SUCCESS = "arg_is_success"

        fun newInstance(
            isSuccess: Boolean,
            onDismissAction: (() -> Unit)? = null
        ): BottomSheetResult {
            val fragment = BottomSheetResult()
            fragment.onDismissAction = onDismissAction
            val args = Bundle()
            args.putBoolean(ARG_IS_SUCCESS, isSuccess)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        isSuccess = arguments?.getBoolean(ARG_IS_SUCCESS) == true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetResultBinding.inflate(inflater, container, false)
        setupView(binding.root)
        return binding.root
    }

    private fun setupView(view: View) {
        with(binding) {
            if (isSuccess) {
                tvTitle.text = "Yeay, benar!"
                ivStatusIcon.setImageResource(R.drawable.ic_correct)
            } else {
                tvTitle.text = "Oops, masih salah"
                ivStatusIcon.setImageResource(R.drawable.ic_close)
            }

            txtContinue.setOnClickListener {
                dismiss()
                onDismissAction?.invoke()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}