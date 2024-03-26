package com.example.w07

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

class ClassSelectionDialog : DialogFragment() {
    lateinit var listener: ClassSelectionDialogListener
    val classArray by lazy { resources.getStringArray(R.array.class_list) }
    interface ClassSelectionDialogListener {
        fun setClass(dialog: DialogFragment, selectedClass: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return activity?.let {
            val builder = android.app.AlertDialog.Builder(it)
            builder.setTitle("Select Class")
                .setItems(R.array.class_list) { _, index ->
                    Log.d("ClassSelection", "Selected: ${classArray[index]}")
                    listener.setClass(this, classArray[index])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = context as ClassSelectionDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface. Throw exception.
            throw ClassCastException((context.toString() +
                    " must implement ClassSelectionDialogListener"))
        }
    }
}