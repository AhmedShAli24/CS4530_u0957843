package com.example.courseviewer

data class Course(
    val id: Int,
    val department: String,
    val number: String,
    val location: String
) {
    val name: String
        get() = "$department $number"
}