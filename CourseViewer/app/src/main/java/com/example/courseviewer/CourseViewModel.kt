package com.example.courseviewer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CourseViewModel : ViewModel() {
    private val _courses = mutableStateListOf<Course>()

    val courses: List<Course>
        get() = _courses

    var selectedCourse by mutableStateOf<Course?>(null)
        private set
    var editingCourse by mutableStateOf<Course?>(null)
        private set

    private var nextId = 0

    fun addCourse(department: String, number: String, location: String) {
        _courses.add(
            Course(
                id = nextId++,
                department = department.trim().uppercase(),
                number = number.trim(),
                location = location.trim()
            )
        )
    }

    fun deleteCourse(course: Course) {
        _courses.removeAll { it.id == course.id }
        if (selectedCourse?.id == course.id) selectedCourse = null
        if (editingCourse?.id == course.id) editingCourse = null
    }

    fun selectCourse(course: Course?) {
        selectedCourse = course
    }


    // edits

    fun startEditing(course: Course) {
        editingCourse = course
        selectedCourse = null
    }

    fun cancelEditing() {
        editingCourse = null
    }

    fun updateCourse(department: String, number: String, location: String) {
        val current = editingCourse ?: return
        val index = _courses.indexOfFirst { it.id == current.id }
        if (index != -1) {
            _courses[index] = current.copy(
                department = department.trim().uppercase(),
                number = number.trim(),
                location = location.trim()
            )
        }
        editingCourse = null
    }
}