package com.example.crudhasbi

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudhasbi.databinding.ActivityMainBinding
import com.example.crudhasbi.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding
   lateinit var noteAdapter: NoteAdapter

   val db by lazy { NoteDb(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        setupRecylerView()
    }

    override fun onStart() {
        super.onStart()
        loadNote()
    }
    fun loadNote(){
        CoroutineScope(Dispatchers.IO).launch {
            val note = db.noteDao().getNote()
            Log.d("MainActivity","dbRespone: $note")
            withContext(Dispatchers.Main){
                noteAdapter.setData(note)
            }
        }
    }
    private fun setupListener(){
        binding.buttonCreate.setOnClickListener{
            intentEdit(0, Constatn.TYPE_CREATE)

        }
    }

    fun intentEdit(noteId: Int, intentType:Int){
        startActivity(
            Intent(applicationContext,EditActivity::class.java)
            .putExtra("intent_id", noteId)
                .putExtra("intent_type",intentType)

        )
    }
    private fun setupRecylerView(){
        noteAdapter = NoteAdapter(arrayListOf(), object : NoteAdapter.OnAdapterListener{
            override fun onRead(note: Note) {
                intentEdit(note.id, Constatn.TYPE_READ)
            }

            override fun onUpdate(note: Note) {
                intentEdit(note.id, Constatn.TYPE_UPDATE)
            }

            override fun onDelete(note: Note) {
                intentEdit(note.id, Constatn.TYPE_CREATE)
                deleteDialog(note)
            }
        })
        binding .ListNote.apply {
            layoutManager= LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }

    private fun deleteDialog(note: Note){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("KONFIRMASI")
            setMessage("YAKIN MAU HAPUS ${note.title}?")
            setNegativeButton("BATAL"){dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("HAPUS"){dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(note)
                    loadNote()
                }
            }
        }
        alertDialog.show()
    }

}