package com.example.courseviewer

import kotlinx.coroutines.flow.Flow

class CourseRepository(private val courseDao: CourseDao) {

    val allCourses: Flow<List<Course>> = courseDao.getAllCourses()

    suspend fun insert(course: Course) {
        courseDao.insert(course)
    }

    suspend fun delete(course: Course) {
        courseDao.delete(course)
    }

    suspend fun update(course: Course) {
        courseDao.update(course)
    }
}