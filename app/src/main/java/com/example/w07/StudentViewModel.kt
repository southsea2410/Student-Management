package com.example.w07

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel(){
    val studentList : LiveData<List<Student>> = repository.studentList.asLiveData()

    fun insert(_name: String, _gender: String, _dob: String, _class: String) = viewModelScope.launch {
        repository.insert(_name, _gender, _dob, _class)
    }

    fun update(id: Int, _name: String, _gender: String, _dob: String, _class: String) = viewModelScope.launch {
        repository.updateStudent(id, _name, _gender, _dob, _class)
    }

    fun update(student: Student) = viewModelScope.launch {
        repository.updateStudent(student)
    }

    fun delete(studentID: Int) = viewModelScope.launch {
        repository.deleteStudent(studentID)
    }

}

class StudentViewFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}