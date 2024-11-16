package com.smallworldfs.moneytransferapp.presentation.account.documents.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smallworldfs.moneytransferapp.databinding.FragmentSourcePickerBottomSheetBinding

class SourcePickerBottomSheet(
    private val takePicture: () -> Unit,
    private val selectPicture: () -> Unit
) : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(takePicture: () -> Unit, selectPicture: () -> Unit) =
            SourcePickerBottomSheet(takePicture, selectPicture)
    }

    private var _binding: FragmentSourcePickerBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSourcePickerBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.openCamera.setOnClickListener {
            takePicture.invoke()
            dismiss()
        }

        binding.openGalery.setOnClickListener {
            selectPicture.invoke()
            dismiss()
        }
    }
}
