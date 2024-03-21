package com.example.w07

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class EditActivity : AppCompatActivity(R.layout.activity_edit_student), StudentDeleteDialog.StudentDeleteDialogListener
{
    private val dobDialog by lazy { DatePickerDialog(this) }
    private val nameInput by lazy { findViewById<EditText>(R.id.editFullName_Edit) }
    private val dobInput by lazy { findViewById<EditText>(R.id.editDOB_Edit) }
    private val spinner by lazy { findViewById<Spinner>(R.id.classSpinner_Edit) }
    private val genderGroup by lazy { findViewById<RadioGroup>(R.id.radioGroupGender_Edit) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Class Spinner initial setup
        var _class = ""
        ArrayAdapter.createFromResource(this, R.array.class_list, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            intent.getStringExtra("class").toString().also {
                // on below line we are getting the position of the item by the item name in our adapter.
                val spinnerPosition: Int = adapter.getPosition(it)
                // on below line we are setting selection for our spinner to spinner position.
                spinner.setSelection(spinnerPosition)
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO("Not yet implemented")
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                _class = parent?.getItemAtPosition(position).toString()
            }
        }

        // Set initial values for other fields
        nameInput.setText(intent.getStringExtra("name").toString())
        dobInput.setText(intent.getStringExtra("dob").toString())

        when(intent.getStringExtra("gender").toString()) {
            "M" -> genderGroup.check(R.id.radioBtnMale_Edit)
            "F" -> genderGroup.check(R.id.radioBtnFemale_Edit)
            "O" -> genderGroup.check(R.id.radioBtnOther_Edit)
            else -> genderGroup.clearCheck()
        }

        // DOB EditText
        dobInput.isFocusable = false
        dobInput.keyListener = null
        dobDialog.setOnDateSetListener { _, year, month, day ->
            dobInput.setText("${if (day < 10) '0' + day.toString() else day}/${if (month < 9) '0' + (month + 1).toString() else month + 1}/$year")
        }
        dobInput.setOnClickListener {
            val dob = stringToCalendar(dobInput.text.toString())
            dobDialog.updateDate(dob["year"]!!, dob["month"]!! - 1, dob["day"]!!)
            dobDialog.show()
        }

        // Save button
        findViewById<Button>(R.id.addStudentSaveBtn_Edit).setOnClickListener {
            // Gender checked button id
            val _genderId = genderGroup.checkedRadioButtonId

            val _name = nameInput.text.toString()
            val _dob = dobInput.text.toString()

            if (_name.isEmpty() || _dob.isEmpty() || _class.isEmpty() || _genderId == -1) {
                return@setOnClickListener
            }

            val _gender = findViewById<RadioButton>(_genderId).text.toString()[0].toString()

            // Reply Intent
            val replyIntent = Intent()
            replyIntent.putExtra("id", intent.getIntExtra("id", -1))
            replyIntent.putExtra("name", _name)
            replyIntent.putExtra("gender", _gender)
            replyIntent.putExtra("dob", _dob)
            replyIntent.putExtra("class", _class)
            setResult(RESULT_OK, replyIntent)
            finish()
        }

        // Delete button
        findViewById<Button>(R.id.addStudentDeleteBtn_Edit).setOnClickListener{
            val dialog = StudentDeleteDialog()
            dialog.show(supportFragmentManager, "DeleteStudent")
        }
    }

    private fun stringToCalendar(date: String) : Map<String, Int>{
        val calendar = Calendar.getInstance()
        val dateParts = date.split("/")
        return mapOf(
            "day" to dateParts[0].toInt(),
            "month" to dateParts[1].toInt(),
            "year" to dateParts[2].toInt()
        )
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val replyIntent = Intent()
        replyIntent.putExtra("id", intent.getIntExtra("id", -1))
        setResult(RESULT_FIRST_USER, replyIntent)
        Log.d("EditActivity", "Student Delete Confirmed")
        finish()
    }
}