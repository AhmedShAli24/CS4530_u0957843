package com.example.funfacts.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FunFact::class], version = 1, exportSchema = false)
abstract class FunFactDatabase : RoomDatabase() {

    abstract fun funFactDao(): FunFactDao

    companion object {
        private const val DB_NAME = "fun_facts.db"

        /** Called once from FunFactApplication, which keeps the single instance. */
        fun build(context: Context): FunFactDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                FunFactDatabase::class.java,
                DB_NAME
            ).build()
    }
}
