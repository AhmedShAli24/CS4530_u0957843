package com.example.funfacts

import com.example.funfacts.data.remote.FunFactResponse
import com.example.funfacts.data.remote.toEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class FunFactMappingTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun parsesApiJsonAndMapsToEntity() {
        val body = """
            {
              "id": "abc123",
              "text": "  Honey never spoils.  ",
              "source": "djtech.net",
              "source_url": "http://www.djtech.net/humor/useless_facts.htm",
              "language": "en",
              "permalink": "https://uselessfacts.jsph.pl/api/v2/facts/abc123",
              "someNewField": true
            }
        """.trimIndent()

        val response = json.decodeFromString<FunFactResponse>(body)
        val entity = response.toEntity(fetchedAt = 42L)

        assertEquals("abc123", entity.id)
        assertEquals("Honey never spoils.", entity.text)
        assertEquals("djtech.net", entity.source)
        assertEquals("http://www.djtech.net/humor/useless_facts.htm", entity.sourceUrl)
        assertEquals(42L, entity.fetchedAt)
    }
}
