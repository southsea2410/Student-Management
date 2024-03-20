package com.example.w07

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

class EditActivity : AppCompatActivity(R.layout.activity_edit_student)
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inputs
        val nameInput = findViewById<EditText>(R.id.editFullName_Edit)
        val dobInput = findViewById<EditText>(R.id.editDOB_Edit)
        val spinner = findViewById<Spinner>(R.id.classSpinner_Edit)
        val genderGroup = findViewById<RadioGroup>(R.id.radioGroupGender_Edit)

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

        findViewById<Button>(R.id.addStudentDeleteBtn_Edit).setOnClickListener{
            val replyIntent = Intent()
            replyIntent.putExtra("id", intent.getIntExtra("id", -1))
            setResult(RESULT_FIRST_USER, replyIntent)
            finish()
        }
    }
}