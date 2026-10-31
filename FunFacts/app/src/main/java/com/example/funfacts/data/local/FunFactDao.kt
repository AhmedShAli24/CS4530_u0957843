package com.example.funfacts.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FunFactDao {


    @Query(
        """
        SELECT * FROM fun_facts
        WHERE text LIKE '%' || :query || '%'
        ORDER BY fetchedAt DESC
        """
    )
    fun searchFacts(query: String): Flow<List<FunFact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fact: FunFact)

    @Query("DELETE FROM fun_facts WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM fun_facts")
    suspend fun deleteAll()
}
