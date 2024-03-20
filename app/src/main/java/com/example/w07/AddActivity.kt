package com.example.w07

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity(R.layout.activity_add_student) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val spinner = findViewById<Spinner>(R.id.classSpinner)
        var _class = ""
        ArrayAdapter.createFromResource(this, R.array.class_list, android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
            }
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                _class = parent?.getItemAtPosition(position).toString()
            }
        }

        findViewById<Button>(R.id.addStudentSaveBtn).setOnClickListener {
            // Gender checked button id
            val _genderId = findViewById<RadioGroup>(R.id.radioGroupGender).checkedRadioButtonId

            val _name = findViewById<EditText>(R.id.editFullName).text.toString()
            val _dob = findViewById<EditText>(R.id.editDOB).text.toString()


            if (_name.isEmpty() || _dob.isEmpty() || _class.isEmpty() || _genderId == -1) {
                return@setOnClickListener
            }

            val _gender = findViewById<RadioButton>(_genderId).text.toString()[0].toString()

            // Reply Intent
            val replyIntent = Intent()
            replyIntent.putExtra("name", _name)
            replyIntent.putExtra("gender", _gender)
            replyIntent.putExtra("dob", _dob)
            replyIntent.putExtra("class", _class)
            setResult(RESULT_OK, replyIntent)
            finish()
        }
    }
}