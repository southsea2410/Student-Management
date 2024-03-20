package com.example.w07

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity
data class Student(
    @ColumnInfo val _name : String,
    @ColumnInfo val _gender : String,
    @ColumnInfo val _dob : String,
    @ColumnInfo val _class : String
){
    @PrimaryKey(autoGenerate = true)
    var _id : Int = 0;

    override fun toString(): String {
        return "$_name, $_gender, $_dob, $_class"
    }

    fun getView(context: Context): View {
        val layoutInflater = LayoutInflater.from(context)
        val template : View = layoutInflater.inflate(R.layout.student_list_item, null)
        template.findViewById<TextView>(R.id.templateName).text = _name
        template.findViewById<TextView>(R.id.templateClass).text = _class

        val genderDOB = when(_gender){
            "M" -> "Male"
            "F" -> "Female"
            "O" -> "Other"
            else -> ""
        } + " - $_dob"
        template.findViewById<TextView>(R.id.templateGenderDOB).text = genderDOB

        return template
    }
}

data class StudentID(
    val _id: Int
)

@Dao
interface StudentDAO {
    @Insert
    suspend fun insert(students: Student)

    @Query("SELECT * FROM Student")
    fun getAllStudents() : Flow<List<Student>>

    // Count students
    @Query("SELECT COUNT(*) FROM Student")
    suspend fun countStudents() : Int

    @Query("SELECT * FROM Student WHERE _id = :studentID")
    suspend fun getStudent(studentID: Int) : Student

    @Update
    suspend fun updateStudents(students: Student)

    @Delete(entity=Student::class)
    suspend fun deleteStudents(studentID: StudentID)

    @Query("DELETE FROM Student")
    fun deleteAll()
}