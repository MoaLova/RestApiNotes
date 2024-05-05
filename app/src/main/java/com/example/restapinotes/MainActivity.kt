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

        val dummyList = listOf(
            "Dummy Note 1",
            "Dummy Note 2",
            "Dummy Note 3"
        )


        binding.AddNotes.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNote::class.java)
            startActivity(intent)
        }

        binding.getNotes.setOnClickListener{
            getNotes()
        }

        binding.listItem.onItemClickListener = AdapterView.OnItemClickListener{
            parent, view, position, l ->
            val selectedNotes =parent.getItemAtPosition(position) as Note
            val intent = Intent(this, ShowNotesActivity:: class.java)
            intent.putExtra("item_id", selectedNotes.id)
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
