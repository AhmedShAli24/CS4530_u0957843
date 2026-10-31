package com.example.funfacts.data.remote

import com.example.funfacts.data.local.FunFact
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FunFactResponse(
    val id: String,
    val text: String,
    val source: String? = null,
    @SerialName("source_url") val sourceUrl: String? = null,
    val language: String? = null,
    val permalink: String? = null,
)


fun FunFactResponse.toEntity(fetchedAt: Long = System.currentTimeMillis()): FunFact =
    FunFact(
        id = id,
        text = text.trim(),
        source = source,
        sourceUrl = sourceUrl,
        fetchedAt = fetchedAt,
    )
