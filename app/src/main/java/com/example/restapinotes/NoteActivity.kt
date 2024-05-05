package com.example.restapinotes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restapinotes.databinding.ActivityNoteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteActivity : AppCompatActivity() {
private lateinit var binding: ActivityNoteBinding
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve extras
        val noteId = intent.getIntExtra("note_id", 0)
        val noteHeadline = intent.getStringExtra("note_headline") ?: ""
        val noteText = intent.getStringExtra("note_text") ?: ""

        // Populate UI fields
        binding.IDEt.setText(noteId.toString())
        binding.headlineEt.setText(noteHeadline)
        binding.noteEt.setText(noteText)

        // Set click listeners
        binding.AddNotes.setOnClickListener {
            addNotes()
        }

        binding.DeleteNotes.setOnClickListener {
            deleteNotes()
        }

        binding.EditNotes.setOnClickListener {
            updateNote()
        }
    }


    private fun updateNote() {
        val headline = binding.headlineEt.text.toString()
        val note = binding.noteEt.text.toString()
        val id = Integer.parseInt(binding.IDEt.text.toString())

        val notesService = ServiceBuilder.buildService(NotesService::class.java)
        val requestCall = notesService.updateNotes(id,headline,note)

        requestCall.enqueue(object : retrofit2.Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if(response.isSuccessful){
                    finish()
                } else
                    Toast.makeText(this@NoteActivity, "failed to create notes", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun deleteNotes() {
        val id = Integer.parseInt(binding.IDEt.text.toString())
        val notesService = ServiceBuilder.buildService(NotesService::class.java)
        val requestCall = notesService.deleteNotes(id)

        requestCall.enqueue(object : retrofit2.Callback<Note>{
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful){
                    Toast.makeText(this@NoteActivity, "delete notes", Toast.LENGTH_SHORT).show()
                    finish()
                }else Toast.makeText(this@NoteActivity, "failed to delete note", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun addNotes() {
        val headline = binding.headlineEt.text.toString()
        val note = binding.noteEt.text.toString()
        val newNote = NoteInput(headline, note)
        val notesService = ServiceBuilder.buildService(NotesService::class.java)
        val requestCall = notesService.addNotes(newNote)

        requestCall.enqueue(object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    println("Note added successfully")
                    Toast.makeText(this@NoteActivity, "Note added successfully", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    println("Failed to add note")
                    Toast.makeText(this@NoteActivity, "Failed to add note", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace()
                println("Failed to add note due to an error: ${t.message}")
                Toast.makeText(
                    this@NoteActivity,
                    "Failed to add note due to an error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }}
