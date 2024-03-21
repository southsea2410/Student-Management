package com.example.w07

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class StudentDeleteDialog : DialogFragment() {
    lateinit var listener: StudentDeleteDialogListener
    interface StudentDeleteDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Are you sure you want to delete this student?")
                .setPositiveButton("Yes") { _, _ ->
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton("No") {_, _ ->
                    // User cancelled the dialog
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    // Override the Fragment.onAttach() method to instantiate the
    // NoticeDialogListener.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = context as StudentDeleteDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface. Throw exception.
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }
}