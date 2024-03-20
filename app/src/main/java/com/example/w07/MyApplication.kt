package com.example.w07

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import android.app.Application

class MyApplication : Application() {
    // A Coroutine for entire app
    private val appCoroutineScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { AppDatabase.getInstance(this, appCoroutineScope) }
    val studentRepository by lazy { StudentRepository(database.studentDAO()) }
}