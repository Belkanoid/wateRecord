package com.belkanoid.waterecord.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.belkanoid.waterecord.R
import com.belkanoid.waterecord.domain.entity.Record
import com.belkanoid.waterecord.presentation.toBitmap
import com.google.android.material.switchmaterial.SwitchMaterial

class NewRecordDialog: DialogFragment() {

    interface OnSaveRecordListener {
        fun saveRecord(record: Record)
    }
    private lateinit var saveListener: OnSaveRecordListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        saveListener = parentFragment as OnSaveRecordListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_record_dialog_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            val image = arguments?.getByteArray(KEY_IMAGE)!!

            val editText = findViewById<EditText>(R.id.new_record_dialog_value).apply {
                setText(arguments?.getString(KEY_VALUE))
            }
            val switcher = findViewById<SwitchMaterial>(R.id.new_record_dialog_switch)
            findViewById<ImageView>(R.id.new_record_dialog_image).setImageBitmap(image.toBitmap())

            findViewById<AppCompatButton>(R.id.new_record_dialog_save_btn).setOnClickListener {
                val record = Record(editText.text.toString(), System.currentTimeMillis(), switcher.isChecked, image)
                saveListener.saveRecord(record)
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
        private const val KEY_VALUE = "KEY_VALUE"
        private const val KEY_IMAGE = "KEY_IMAGE"

        fun newInstance(value: String, image: ByteArray): NewRecordDialog {
            val args = Bundle()
            args.putString(KEY_VALUE, value)
            args.putByteArray(KEY_IMAGE, image)
            val fragment = NewRecordDialog()
            fragment.arguments = args
            return fragment
        }

    }
}