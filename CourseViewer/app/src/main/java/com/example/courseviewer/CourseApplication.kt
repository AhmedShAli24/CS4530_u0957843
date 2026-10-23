package com.example.courseviewer

import android.app.Application

class CourseApplication : Application() {

    val database by lazy { CourseDatabase.getDatabase(this) }

    val repository by lazy { CourseRepository(database.courseDao()) }
}