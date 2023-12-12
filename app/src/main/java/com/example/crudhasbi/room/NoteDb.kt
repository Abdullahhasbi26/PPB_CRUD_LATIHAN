package com.example.crudhasbi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.locks.Lock

@Database(
    entities = [Note::class],
    version = 1
)

abstract class NoteDb : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object{
        @Volatile private var instance:NoteDb? = null
        private val Lock = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(Lock){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }
        private fun buildDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            NoteDb::class.java,
            "0856474789.db"
        ).build()

        }
    }
