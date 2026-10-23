package com.example.courseviewer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {

    val courses: StateFlow<List<Course>> = repository.allCourses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var selectedCourse by mutableStateOf<Course?>(null)
        private set
    var editingCourse by mutableStateOf<Course?>(null)
        private set

    fun addCourse(department: String, number: String, location: String) {
        val course = Course(
            department = department.trim().uppercase(),
            number = number.trim(),
            location = location.trim()
        )
        viewModelScope.launch {
            repository.insert(course)
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            repository.delete(course)
        }
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
        val updated = current.copy(
            department = department.trim().uppercase(),
            number = number.trim(),
            location = location.trim()
        )
        viewModelScope.launch {
            repository.update(updated)
        }
        editingCourse = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as CourseApplication
                CourseViewModel(app.repository)
            }
        }
    }
}