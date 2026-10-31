package com.example.funfacts.data

import com.example.funfacts.data.local.FunFact
import com.example.funfacts.data.local.FunFactDao
import com.example.funfacts.data.remote.FunFactResponse
import com.example.funfacts.data.remote.toEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class FunFactRepository private constructor(
    private val dao: FunFactDao,
    private val client: HttpClient,
) {

    fun searchFacts(query: String): Flow<List<FunFact>> =
        dao.searchFacts(query.trim())


    suspend fun fetchAndSaveRandomFact(): Result<FunFact> = withContext(Dispatchers.IO) {
        runCatching {
            val response: HttpResponse = client.get("random.json") {
                parameter("language", "en")
            }
            check(response.status.isSuccess()) { "Server returned ${response.status}" }

            val fact = response.body<FunFactResponse>().toEntity()
            dao.insert(fact)
            fact
        }
    }

    suspend fun deleteFact(id: String) = dao.deleteById(id)

    suspend fun clearAll() = dao.deleteAll()

    companion object {
        @Volatile
        private var INSTANCE: FunFactRepository? = null

        fun getInstance(dao: FunFactDao, client: HttpClient): FunFactRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FunFactRepository(dao, client).also { INSTANCE = it }
            }
    }
}
