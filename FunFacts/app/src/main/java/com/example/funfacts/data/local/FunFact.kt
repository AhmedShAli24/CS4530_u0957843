package com.example.funfacts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "fun_facts")
data class FunFact(
    @PrimaryKey val id: String,
    val text: String,
    val source: String?,
    val sourceUrl: String?,

    val fetchedAt: Long,
)
