package com.example.restapinotes

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesService {
  @GET("notes")
  fun getNotes(): Call<List<Note>>


  //slutade med att jag ej använde denna, functionen ligger med i gammal commit men raderades i nuvarande
  @GET( "notes/{id}")
  fun getNotesByID(@Path("id")id: Int): Call<Note>

  @POST("notes")
  fun addNotes(@Body newNotes: NoteInput): Call<Note>

  @FormUrlEncoded
  @PUT("notes/{id}")
  fun updateNotes(@Path("id") id: Int, @Field("headline") headline: String, @Field("note")note:String): Call<Note>

  @DELETE("notes/{id}")
  fun deleteNotes(@Path("id")Sid: Int): Call<Note>

}
