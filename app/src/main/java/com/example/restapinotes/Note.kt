package com.example.restapinotes
data class Note(val id: Int, val headline: String, val note: String){

    override fun toString(): String {
        return "Notes(id= '$id', headline='$headline', note='$note')"
    }
}