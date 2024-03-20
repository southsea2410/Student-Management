package com.example.w07

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class StudentRepository(
    private val studentDAO: StudentDAO
) {
    val studentList : Flow<List<Student>> = studentDAO.getAllStudents()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(_name: String, _gender: String, _dob: String, _class: String ) {
        val student = Student(_name, _gender, _dob, _class)
        studentDAO.insert(student)
        return
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun count() : Int {
        return studentDAO.countStudents()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateStudent(_id: Int, _name: String, _gender: String, _dob: String, _class: String) {
        val newStudent = Student(_name, _gender, _dob, _class)
        newStudent._id = _id
        studentDAO.updateStudents(newStudent)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateStudent(student: Student) {
        studentDAO.updateStudents(student)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteStudent(_id: Int) {
        val student = StudentID(_id)
        studentDAO.deleteStudents(student)
    }
}