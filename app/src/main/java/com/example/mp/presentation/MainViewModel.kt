package com.example.mp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp.core.log
import com.example.mp.data.AppRepository
import com.example.mp.data.local.NoteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appRepository: AppRepository
): ViewModel() {

    private val notesData = MutableLiveData<List<NoteEntity>>()
    val notesLiveData: LiveData<List<NoteEntity>> = notesData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.getNotesFlow().collect {
                notesData.postValue(it)
            }
        }
    }

    fun insertNote(name: String, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.insertNote("Note# ${notesData.value?.size}", text)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.deleteNote(id)
        }
    }
}