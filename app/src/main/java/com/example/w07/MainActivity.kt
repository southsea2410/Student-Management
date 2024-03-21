package com.example.w07

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    // Main Recycler View
    lateinit var studentListContainer: RecyclerView

    // Student View Model
    private val studentViewModel : StudentViewModel by viewModels {
        StudentViewFactory((application as MyApplication).studentRepository)
    }

    // Intent Launchers
    private val addStudentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val data = it.data
                val _name = data?.getStringExtra("name")
                val _gender = data?.getStringExtra("gender")
                val _dob = data?.getStringExtra("dob")
                val _class = data?.getStringExtra("class")
                studentViewModel.insert(_name!!, _gender!!, _dob!!, _class!!)
            }
        }
    private val editStudentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val data = it.data
                val _id = data?.getIntExtra("id", -1)
                val _name = data?.getStringExtra("name")
                val _gender = data?.getStringExtra("gender")
                val _dob = data?.getStringExtra("dob")
                val _class = data?.getStringExtra("class")
                studentViewModel.update(_id!!, _name!!, _gender!!, _dob!!, _class!!)
            }
            if (it.resultCode == RESULT_FIRST_USER) {
                // Snackbar
                Snackbar.make(findViewById(R.id.textView), "Student Deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Undo"){}
                    .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (event != DISMISS_EVENT_ACTION) {
                                val data = it.data
                                val _id = data?.getIntExtra("id", -1)
                                studentViewModel.delete(_id!!)
                            }
                        }
                    })
                    .show()
            }
        }
    private fun dirLog() {
        Log.d(
            "MainActivity", """
    filesDir: $filesDir
    dataDir: $dataDir
    externalFilesDir: ${getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}
    externalStorageDirectory: ${Environment.getExternalStorageDirectory()}
    externalStoragePublicDirectory: ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)}
        """.trimIndent()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dirLog()

        // Attach adapter to recycler view
        val itemClickHandler = { student: Student, index: Int ->
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("id", student._id)
            intent.putExtra("name", student._name)
            intent.putExtra("gender", student._gender)
            intent.putExtra("dob", student._dob)
            intent.putExtra("class", student._class)
            editStudentLauncher.launch(intent)
        }

        val adapter = StudentAdapter(itemClickHandler)
        // Set up recycler view
        studentListContainer = findViewById(R.id.studentListContainer)
        studentListContainer.adapter = adapter
        studentListContainer.layoutManager = LinearLayoutManager(this)


        // Observer for data changes
        studentViewModel.studentList.observe(this) { students ->
            // Update the cached in the adapter
            Log.d("MainActivity", "Student list changed: ${students.size} students found")
            students?.let { adapter.submitList(it) }
        }

        // Add student button
        findViewById<Button>(R.id.addStudentBtn).setOnClickListener {
            val addStudentIntent = Intent(this, AddActivity::class.java)
            addStudentLauncher.launch(addStudentIntent)
        }

        // View mode buttons
        findViewById<Button>(R.id.listModeBtn).setOnClickListener {
            studentListContainer.layoutManager = LinearLayoutManager(this)
        }
        findViewById<Button>(R.id.gridModeBtn).setOnClickListener {
            studentListContainer.layoutManager = GridLayoutManager(this, 2)
        }

        // AutocompleteTextView Searching
        val searchAutocomplete = findViewById<AutoCompleteTextView>(R.id.searchStudent)
        searchAutocomplete.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                // Observer for data changes
                studentViewModel.searchByName(s.toString()).observe(this@MainActivity) { students ->
                    Log.d("MainActivity", "Query: ${students.size} students found")
                    // Update the cached in the adapter
                    students?.let { adapter.submitList(it) }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not yet implemented
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not yet implemented
            }
        })
    }
}

