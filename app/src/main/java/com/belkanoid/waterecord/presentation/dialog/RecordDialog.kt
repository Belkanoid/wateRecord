package com.belkanoid.waterecord.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.belkanoid.waterecord.R
import com.belkanoid.waterecord.domain.entity.Record
import com.belkanoid.waterecord.presentation.recordList.RecordListViewModel
import com.belkanoid.waterecord.presentation.toBitmap
import com.google.android.material.switchmaterial.SwitchMaterial

class RecordDialog: DialogFragment() {


    private val viewModel: RecordListViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.record_dialog_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            val image = arguments?.getByteArray(KEY_IMAGE)!!
            val isHot = arguments?.getBoolean(KEY_TYPE)!!
            val id = arguments?.getInt(KEY_ID)!!
            val editText = findViewById<EditText>(R.id.record_dialog_value).apply {
                setText(arguments?.getString(KEY_VALUE))
            }
            val switcher = findViewById<SwitchMaterial>(R.id.record_dialog_switch).apply {
                isChecked = isHot
            }
            findViewById<ImageView>(R.id.record_dialog_image).setImageBitmap(image.toBitmap())

            findViewById<AppCompatButton>(R.id.record_dialog_save_btn).setOnClickListener {
                val record = Record(editText.text.toString(), System.currentTimeMillis(), switcher.isChecked, image, id)
                viewModel.editRecord(record)
                dismiss()
            }
            findViewById<AppCompatButton>(R.id.record_dialog_delete_btn).setOnClickListener {
                viewModel.deleteRecord(id)
                dismiss()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        private const val KEY_ID = "KEY_ID"
        private const val KEY_VALUE = "KEY_VALUE"
        private const val KEY_IMAGE = "KEY_IMAGE"
        private const val KEY_TYPE = "KEY_TYPE"

        fun newInstance(id: Int, title: String, image: ByteArray, isHot: Boolean): RecordDialog {
            val args = Bundle()
            args.putInt(KEY_ID, id)
            args.putString(KEY_VALUE, title)
            args.putByteArray(KEY_IMAGE, image)
            args.putBoolean(KEY_TYPE, isHot)
            val fragment = RecordDialog()
            fragment.arguments = args
            return fragment
        }

    }
}