package com.belkanoid.waterecord.presentation.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.belkanoid.waterecord.databinding.FragmentMainBinding
import com.belkanoid.waterecord.domain.entity.Record
import com.belkanoid.waterecord.presentation.Application
import com.belkanoid.waterecord.presentation.Assets
import com.belkanoid.waterecord.presentation.Assets.getTessDataPath
import com.belkanoid.waterecord.presentation.CropView
import com.belkanoid.waterecord.presentation.ViewModelFactory
import com.belkanoid.waterecord.presentation.afterMeasured
import com.google.common.util.concurrent.ListenableFuture
import com.googlecode.tesseract.android.TessBaseAPI
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import java.util.Calendar
import javax.inject.Inject


class MainFragment : Fragment() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var camera: Camera
    private lateinit var binding: FragmentMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }
    private val component by lazy {
        (requireActivity().application as Application).component
    }
    private lateinit var cropView: CropView

    private val analyzer = MLAnalyzerFactory.getInstance().localTextAnalyzer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        component.inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cropView = CropView(requireContext())
        binding.root.addView(cropView)
    }

    @androidx.camera.core.ExperimentalGetImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Assets.extractAssets(requireContext())


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

    @ExperimentalGetImage
    @androidx.annotation.OptIn(androidx.camera.camera2.interop.ExperimentalCamera2Interop::class)
    private fun shootListener() {
        binding.recordCameraShoot.setOnClickListener {
            viewModel.addRecord(Record("sdfasdf", Calendar.getInstance().time.time, false))
            viewModel.list.observe(viewLifecycleOwner) {
                Log.d("LOL", it.toString())
            }
            val previewBitmap = binding.recordCameraView.bitmap!!
            val rect = cropView.getCroppedRectangle()
            val croppedBitmap =
                Bitmap.createBitmap(previewBitmap, rect.left, rect.top, rect.width(), rect.height())
            binding.imageView.setImageBitmap(croppedBitmap)
            fetchRecordValue(croppedBitmap)
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

    private fun fetchRecordValue(bitmap: Bitmap) {
        val dataPath = getTessDataPath(requireContext())
        val tess = TessBaseAPI()
        tess.init(dataPath, "digits")
        val bm1: Bitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.getWidth()/2, bitmap.getHeight()
        )
        val bm2: Bitmap = Bitmap.createBitmap(
            bitmap, bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight()
        )
        tess.setImage(bitmap)
        binding.textView.text = tess.utF8Text
//        tess.setImage(bm2)
//        binding.textView2.text = tess.utF8Text


//        val frame1 = MLFrame.fromBitmap(bm1)
//        val frame2 = MLFrame.fromBitmap(bm2)
//        val task1 = analyzer.asyncAnalyseFrame(frame1)
//        task1.addOnSuccessListener {it1 ->
//            Log.d("LOL1", it1.stringValue)
//            binding.textView.text = it1.stringValue
//        }
//        val task2 = analyzer.asyncAnalyseFrame(frame2)
//        task2.addOnSuccessListener {it2 ->
//            Log.d("LOL2", it2.stringValue)
//            binding.textView2.text = it2.stringValue
//        }



    }

}