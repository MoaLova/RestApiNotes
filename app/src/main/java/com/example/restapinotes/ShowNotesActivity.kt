package com.example.restapinotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restapinotes.databinding.ShowNotesBinding
import retrofit2.Call
import retrofit2.Response

class ShowNotesActivity : AppCompatActivity() {
    lateinit var binding: ShowNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notesId = intent.getIntExtra("item_id", 0)

        loadDetails(notesId)

    }

    private fun loadDetails(notesId: Int) {
        val notesService = ServiceBuilder.buildService(NotesService::class.java)
        val requestCall = notesService.getNotesByID(notesId)

        requestCall.enqueue(object : retrofit2.Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if(response.isSuccessful){
                    val notes = response.body()
                    notes?.let {
                       binding.notetv.setText(notes.note)
                        binding.headlinetv.setText(notes.headline)

                    }
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }
}