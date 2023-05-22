package com.example.mp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.mp.core.log
import com.example.mp.data.local.NoteEntity
import com.example.mp.presentation.MainViewModel
import com.example.mp.ui.theme.MPTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private val REQUEST_CODE_SPEECH_INPUT = 1

    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeechEngine.language = Locale.UK
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MPTheme {
                var notes by remember {
                    mutableStateOf(listOf<NoteEntity>())
                }
                mainViewModel.notesLiveData.observe(LocalLifecycleOwner.current) {
                    notes = it
                }
                var note by remember {
                    mutableStateOf("")
                }
                // A surface container using the 'background' color from the theme
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center) {
                        androidx.compose.material3.Button(onClick = {
                            stt()
                        }) {
                            Text(text = "Add Note")
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    NoteList(notes = notes, mainViewModel::deleteNote, Modifier.padding(8.dp))
                }
            }
        }
    }

    private fun stt() {
        mainViewModel.viewModelScope.launch {
            val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            sttIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")

            try {
                startActivityForResult(sttIntent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
//                Toast.makeText(this, "Your device does not support STT.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    result?.let {
                        val recognizedText = it[0]
                        mainViewModel.insertNote("Note",recognizedText)
                    }
                }
            }
        }
    }

    override fun onPause() {
        textToSpeechEngine.stop()
        super.onPause()
    }

    override fun onDestroy() {
        textToSpeechEngine.shutdown()
        super.onDestroy()
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MPTheme {
    }
}

@Composable
fun NoteList(
    notes: List<NoteEntity>,
    delete: (id: Int) -> Unit,
    mModifier: Modifier = Modifier
) {
    LazyColumn(modifier = mModifier) {
        items(notes) { note ->
            Row {
//                Text(text = note.name, )
//                Spacer(modifier = Modifier.width(20.dp))
//                Text(text = note.note, modifier = Modifier.weight(1f), maxLines = 1,  overflow = TextOverflow.Ellipsis)
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(text = note.date, )
                Text(text = note.note, modifier = Modifier.weight(1f), maxLines = 1,  overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = note.date)
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { delete(note.id) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
            Divider(thickness = 2.dp, modifier = Modifier.padding(4.dp))
        }
    }
}