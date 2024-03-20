package com.example.w07

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDAO(): StudentDAO

//    private class AppDatabaseCallback(
//        private val scope: CoroutineScope
//    ) : RoomDatabase.Callback() {
//
//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//            instance?.let { appDatabase ->
//                scope.launch {
//                    populateDatabase(appDatabase.studentDAO())
//                }
//            }
//        }
//
//        suspend fun populateDatabase(studentDAO: StudentDAO) {
//            // Delete all content
//            studentDAO.deleteAll()
//
//            // Add sample Student
//            var student1 = Student("Hai Nam", "M", "24/10/2003", "21KTPM3")
//            var student2 = Student("Tran Minh Anh", "F", "17/12/2003", "21KTPM2")
//            studentDAO.insert(student1)
//            studentDAO.insert(student2)
//        }
//    }

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            // If the instance is not null, return it
            // Otherwise, create a new instance and return it
            return instance ?: synchronized(this) {
                val dbFile = File(context.filesDir, "student.db")
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    dbFile.absolutePath
                ).build()
                instance!!
            }
        }
    }
}

