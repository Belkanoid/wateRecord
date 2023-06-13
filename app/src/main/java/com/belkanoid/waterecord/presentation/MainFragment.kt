package com.belkanoid.waterecord.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.belkanoid.waterecord.databinding.FragmentMainBinding
import com.google.common.util.concurrent.ListenableFuture


class MainFragment : Fragment() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var camera: Camera
    private lateinit var binding: FragmentMainBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.addView(CropView(requireContext()))
    }
    @androidx.camera.core.ExperimentalGetImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())



        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @androidx.camera.core.ExperimentalGetImage
    @SuppressLint("RestrictedApi")
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder().build()
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()



        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)

            .build()
        preview.setSurfaceProvider(binding.recordCameraView.surfaceProvider)

        camera = cameraProvider
            .bindToLifecycle(this as LifecycleOwner, cameraSelector, imageCapture, preview)

        torchListener()
        focusListener()
        shootListener()
    }

    @androidx.camera.core.ExperimentalGetImage
    private fun shootListener() {
        binding.recordCameraShoot.setOnClickListener {
            binding.imageView.setImageBitmap(binding.recordCameraView.bitmap)
        }
    }

    private fun torchListener() {
        binding.recordCameraTorch.setOnClickListener {
            camera.cameraControl.enableTorch((camera.cameraInfo.torchState.value == 0))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun focusListener() {
        binding.recordCameraView.afterMeasured {
            binding.recordCameraView.setOnTouchListener { _, event ->
                return@setOnTouchListener viewModel.focusEvent(
                    event,
                    camera,
                    binding.recordCameraView.width,
                    binding.recordCameraView.height
                )
            }
        }
    }


}