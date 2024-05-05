package com.example.restapinotes

data class NoteInput(val headline: String, val note: String)

fun generateDummyNotes(): List<Note> {
    val dummyList = mutableListOf<Note>()
    for (i in 1..10) {
        dummyList.add(Note(i, "Headline $i", "Text for note $i"))
    }
    return dummyList
}
