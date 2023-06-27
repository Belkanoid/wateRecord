package com.belkanoid.waterecord.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.belkanoid.waterecord.R
import com.belkanoid.waterecord.databinding.FragmentMainBinding
import com.belkanoid.waterecord.domain.entity.Record
import com.belkanoid.waterecord.presentation.Application
import com.belkanoid.waterecord.presentation.Assets
import com.belkanoid.waterecord.presentation.Assets.getTessDataPath
import com.belkanoid.waterecord.presentation.CropView
import com.belkanoid.waterecord.presentation.ViewModelFactory
import com.belkanoid.waterecord.presentation.afterMeasured
import com.belkanoid.waterecord.presentation.dialog.NewRecordDialog
import com.belkanoid.waterecord.presentation.recordList.RecordsListFragment
import com.belkanoid.waterecord.presentation.toBitmap
import com.belkanoid.waterecord.presentation.toByteArray
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.storage.FirebaseStorage
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject


private const val CAMERA_PERMISSION_CODE = 100
private const val STORAGE_PERMISSION_CODE = 101

class MainFragment : Fragment(), NewRecordDialog.OnSaveRecordListener {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var camera: Camera
    private lateinit var binding: FragmentMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels{ viewModelFactory }

    private val component by lazy {
        (requireActivity().application as Application).component
    }

    private lateinit var cropView: CropView

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

//        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
        }
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
        listListener()
    }

    private fun listListener() {
        binding.recordList.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.record_container, RecordsListFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun shootListener() {
        binding.recordCameraShoot.setOnClickListener {
            val previewBitmap = binding.recordCameraView.bitmap!!
            val rect = cropView.getCroppedRectangle()
            val croppedBitmap =
                Bitmap.createBitmap(previewBitmap, rect.left, rect.top, rect.width(), rect.height())
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
        var text = ""
        val dataPath = getTessDataPath(requireContext())
        Log.d("LOL", dataPath)
        val tess = TessBaseAPI()
        tess.init(dataPath, "eng")
        tess.setVariable("classify_bln_numeric_mode", "1")
        tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "0123456789")
        tess.pageSegMode = TessBaseAPI.PageSegMode.PSM_SINGLE_CHAR
        val bm1: Bitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.getWidth()/2, bitmap.getHeight()
        )
        val bm2: Bitmap = Bitmap.createBitmap(
            bitmap, bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight()
        )
        tess.setImage(bm1)
        text = tess.utF8Text
        tess.setImage(bm2)
        text += tess.utF8Text

        NewRecordDialog.newInstance(text, bitmap.toByteArray()).show(childFragmentManager, "new dialog")

    }

    override fun saveRecord(record: Record) {
        viewModel.addRecord(record)
        val storage = FirebaseStorage.getInstance();
        val storageReference = storage.getReference();
        val ref = storageReference.child(
            "images/" + UUID.randomUUID().toString()
            )
        ref.putBytes(record.image)
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

}