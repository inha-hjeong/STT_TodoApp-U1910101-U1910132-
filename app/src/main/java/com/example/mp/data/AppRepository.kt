package com.example.mp.data

import android.annotation.SuppressLint
import com.example.mp.core.currentTime
import com.example.mp.core.getHourAndMinutesFromDate
import com.example.mp.core.toDate
import com.example.mp.data.local.AppDao
import com.example.mp.data.local.NoteEntity
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat

class AppRepository (private val appDao: AppDao) {

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("d M yyyy HH:mm:ss")

    fun insertNote(name: String, text: String) {
        appDao.insertNote(NoteEntity(name, text, currentTime().toString()))
    }

    fun getNotesFlow() = appDao.getNotesFlow().map { notes ->
        notes.map {
            it.copy(date = getHourAndMinutesFromDate(
                toDate(
                    simpleDateFormat, it.date.toLong()
                )
            ))
        }
    }

    fun deleteNote(id: Int) {
        appDao.deleteNote(id)
    }
}