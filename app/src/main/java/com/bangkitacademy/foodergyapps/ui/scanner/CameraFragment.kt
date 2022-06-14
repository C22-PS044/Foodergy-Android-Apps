package com.bangkitacademy.foodergyapps.ui.scanner

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bangkitacademy.foodergyapps.R
import com.bangkitacademy.foodergyapps.data.model.ModelExecutionResult

import com.bangkitacademy.foodergyapps.databinding.FragmentCameraBinding
import com.bangkitacademy.foodergyapps.rotateBitmap
import com.bangkitacademy.foodergyapps.ui.scanner.CameraActivity.Companion.CAMERA_X_RESULT
import com.bangkitacademy.foodergyapps.uriToFile
import com.bangkitacademy.foodergyapps.utils.OCRModelExecutor
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.util.concurrent.Executors


class CameraFragment : Fragment() {

    private lateinit var result: Bitmap
    private var getFile: File? = null

    private var _binding : FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CameraViewModel

    //
    private var useGPU = false
    private var ocrModel: OCRModelExecutor? = null
    private val inferenceThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val mainScope = MainScope()
    private val mutex = Mutex()

    //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CameraViewModel::class.java]
        viewModel.resultingBitmap.observe(
            viewLifecycleOwner, Observer{ resultImage ->
                if (resultImage != null) {
                    updateUIWithResults(resultImage)
                }
                enableControls(true)
            }
        )

        mainScope.async(inferenceThread) { createModelExecutor(useGPU) }

        val useGpuSwitch = binding.layBottomSheet.switchUseGpu

        useGpuSwitch.setOnCheckedChangeListener { _, isChecked ->
            useGPU = isChecked
            mainScope.async(inferenceThread) { createModelExecutor(useGPU) }
        }

        binding.previewImage.setOnClickListener { startCameraX() }
        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.buttonGaleri.setOnClickListener { startGallery() }

        binding.layBottomSheet.rerunButton.setOnClickListener { enableControls(false) }

        setChipsToLogView(HashMap())
        enableControls(true)
    }
    private fun enableControls(enable: Boolean) {
        binding.layBottomSheet.rerunButton.isEnabled = enable
    }

    private suspend fun createModelExecutor(useGPU: Boolean) {
        mutex.withLock {
            if (ocrModel != null) {
                ocrModel!!.close()
                ocrModel = null
            }
            try {
                ocrModel = OCRModelExecutor(requireActivity(),useGPU)
            } catch (e: Exception) {
               Log.e("CameraFragment","Error creating model executor",e)
            }
        }

    }
    private fun setChipsToLogView(itemsFound: Map<String, Int>) {
        binding.layBottomSheet.chipsGroup.removeAllViews()

        for ((word, color) in itemsFound) {
            val chip = Chip(requireContext())
            chip.text = word
            chip.chipBackgroundColor = getColorStateListForChip(color)
            chip.isClickable = false
            binding.layBottomSheet.chipsGroup.addView(chip)
        }
        val labelsFoundTextView: TextView = binding.layBottomSheet.tfeIsLabelsFound
        if (binding.layBottomSheet.chipsGroup.childCount == 0) {
            labelsFoundTextView.text = getString(R.string.tfe_ocr_no_text_found)
        } else {
            labelsFoundTextView.text = getString(R.string.tfe_ocr_texts_found)
        }
        binding.layBottomSheet.chipsGroup.parent.requestLayout()
    }

    private fun getColorStateListForChip(color: Int): ColorStateList {
        val states =
            arrayOf(
                intArrayOf(android.R.attr.state_enabled), // enabled
                intArrayOf(android.R.attr.state_pressed) // pressed
            )

        val colors = intArrayOf(color, color)
        return ColorStateList(states, colors)
    }
    private fun setImageView(imageView: ImageView, image: Bitmap) {
        Glide.with(Activity()).load(image).override(250, 250).fitCenter().into(imageView)
    }

    private fun updateUIWithResults(modelExecutionResult: ModelExecutionResult) {
        setImageView(binding.previewImage, modelExecutionResult.bitmapResult)
        val logText: TextView = binding.layBottomSheet.logView
        logText.text = modelExecutionResult.executionLog

        setChipsToLogView(modelExecutionResult.itemsFound)
        enableControls(true)
    }

    private fun startCameraX() {
        val intent = Intent(activity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.previewImage.setImageBitmap(result)
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireActivity())
            getFile = myFile
            binding.previewImage.setImageURI(selectedImg)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}