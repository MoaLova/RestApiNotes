package com.example.restapinotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import retrofit2.Callback
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.restapinotes.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AddNotes.setOnClickListener {
            val intent = Intent(this@MainActivity, NoteActivity::class.java)
            startActivity(intent)
        }

        binding.getNotes.setOnClickListener{
            getNotes()
        }

        binding.listItem.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, l ->
            val selectedNote = parent.getItemAtPosition(position) as Note
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra("note_id", selectedNote.id)
            intent.putExtra("note_headline", selectedNote.headline)
            intent.putExtra("note_text", selectedNote.note)
            startActivity(intent)
        }
    }

    fun getNotes() {
        val notesService = ServiceBuilder.buildService(NotesService::class.java)
        val requestCall = notesService.getNotes()

        requestCall.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    val notesList: List<Note> = response.body() ?: emptyList()
                    runOnUiThread {

                        // Initialize custom ArrayAdapter
                        val arrayAdapter = object : ArrayAdapter<Note>(
                            this@MainActivity,
                            R.layout.list_item,
                            notesList
                        ) {
                            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                                val view = convertView ?: layoutInflater.inflate(
                                    R.layout.list_item,
                                    parent,
                                    false
                                )

                                val note = getItem(position)

                                val headlineTextView = view.findViewById<TextView>(R.id.text_note_headline)
                                val idTextView = view.findViewById<TextView>(R.id.text_note_id)

                                headlineTextView.text = note?.headline
                                idTextView.text = note?.id.toString()

                                return view
                            }
                        }
                        binding.listItem.adapter = arrayAdapter
                        arrayAdapter.notifyDataSetChanged() // Notify adapter of data changes
                        println("Notes fetched successfully")
                    }
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                t.printStackTrace()
                runOnUiThread {
                    println("Failed to fetch notes")
                }
            }
        })
}

}
