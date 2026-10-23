package com.example.courseviewer

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val department: String,
    val number: String,
    val location: String
) {
    val name: String
        get() = "$department $number"
}