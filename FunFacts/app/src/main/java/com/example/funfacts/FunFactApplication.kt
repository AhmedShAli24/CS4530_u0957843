package com.example.funfacts

import android.app.Application
import com.example.funfacts.data.FunFactRepository
import com.example.funfacts.data.local.FunFactDatabase
import com.example.funfacts.data.remote.FunFactHttpClient
import io.ktor.client.HttpClient

class FunFactApplication : Application() {

    val database: FunFactDatabase by lazy { FunFactDatabase.build(this) }

    val httpClient: HttpClient by lazy { FunFactHttpClient.client }

    val repository: FunFactRepository by lazy {
        FunFactRepository.getInstance(database.funFactDao(), httpClient)
    }
}
