package com.example.mp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(

    val name: String,

    val note: String,

    val date: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)