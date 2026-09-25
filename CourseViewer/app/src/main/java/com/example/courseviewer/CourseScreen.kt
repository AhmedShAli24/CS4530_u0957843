package com.example.courseviewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// The whole screen: the ONLY composable that talks to the ViewModel
@Composable
fun CourseScreen(
    modifier: Modifier = Modifier,
    vm: CourseViewModel = viewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CourseForm(
            initialCourse = vm.editingCourse,
            onSave = { dept, num, loc ->
                if (vm.editingCourse == null) vm.addCourse(dept, num, loc)
                else vm.updateCourse(dept, num, loc)
            },
            onCancel = if (vm.editingCourse != null) { { vm.cancelEditing() } } else null
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Courses", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        CourseList(
            courses = vm.courses,
            onCourseClick = { vm.selectCourse(it) },
            onDelete = { vm.deleteCourse(it) },
            modifier = Modifier.weight(1f)
        )
    }

    vm.selectedCourse?.let { course ->
        CourseDetailDialog(
            course = course,
            onDismiss = { vm.selectCourse(null) },
            onEdit = { vm.startEditing(course) }
        )
    }
}

// Add / Edit form
@Composable
fun CourseForm(
    initialCourse: Course?,
    onSave: (String, String, String) -> Unit,
    onCancel: (() -> Unit)?
) {
    var department by remember(initialCourse) { mutableStateOf(initialCourse?.department ?: "") }
    var number by remember(initialCourse) { mutableStateOf(initialCourse?.number ?: "") }
    var location by remember(initialCourse) { mutableStateOf(initialCourse?.location ?: "") }

    val canSave = department.isNotBlank() && number.isNotBlank() && location.isNotBlank()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = if (initialCourse == null) "Add a Course" else "Edit ${initialCourse.name}",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("Department") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("Course Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            Button(
                onClick = {
                    onSave(department, number, location)
                    department = ""; number = ""; location = ""
                },
                enabled = canSave
            ) {
                Text(if (initialCourse == null) "Add Course" else "Save Changes")
            }
            if (onCancel != null) {
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = onCancel) { Text("Cancel") }
            }
        }
    }
}

@Composable
fun CourseList(
    courses: List<Course>,
    onCourseClick: (Course) -> Unit,
    onDelete: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    if (courses.isEmpty()) {
        Text("No course added.", modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(courses, key = { it.id }) { course ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCourseClick(course) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = course.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { onDelete(course) }) { Text("Delete") }
                }
            }
        }
    }
}

@Composable
fun CourseDetailDialog(
    course: Course,
    onDismiss: () -> Unit,
    onEdit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(course.name) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Department: ${course.department}")
                Text("Number: ${course.number}")
                Text("Location: ${course.location}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        dismissButton = {
            TextButton(onClick = onEdit) { Text("Edit") }
        }
    )
}