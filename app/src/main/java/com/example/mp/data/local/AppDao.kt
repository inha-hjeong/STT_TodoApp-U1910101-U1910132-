package com.example.mp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteEntity)

    @Query("DELETE FROM note_table WHERE id = :id")
    fun deleteNote(id: Int)

    @Query("SELECT * FROM note_table")
    fun getNotesFlow(): Flow<List<NoteEntity>>
}