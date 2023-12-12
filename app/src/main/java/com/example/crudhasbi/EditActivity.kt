package com.example.crudhasbi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.crudhasbi.databinding.ActivityEditBinding
import com.example.crudhasbi.room.Constatn
import com.example.crudhasbi.room.Note
import com.example.crudhasbi.room.NoteDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    val db by lazy {NoteDb(this) }
    private var noteId: Int = 0

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type",0)
        when (intentType){
            Constatn.TYPE_CREATE -> {
                binding.buttonUpdate.visibility = View.GONE
            }
            Constatn.TYPE_READ -> {
                binding.buttonSimpan.visibility = View.GONE
                binding.buttonUpdate.visibility = View.GONE
                getNote()
            }
           Constatn.TYPE_UPDATE -> {
                binding.buttonSimpan.visibility = View.GONE
                getNote()
            }
        }
    }

    private fun setupListener(){
        binding.buttonSimpan.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().addNote(
                    Note(0,binding.editTitle.text.toString(),
                        binding.editNote.text.toString()
                    )
                )
                finish()
            }
        }
        binding.buttonUpdate.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().updateNote(
                    Note(0, binding.editTitle.text.toString(),
                        binding.editNote.text.toString()
                    )
                )
                finish()
            }
        }
    }

    fun getNote(){
        noteId = intent.getIntExtra("intent_id",0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNote(noteId)[0]
            binding.editTitle.setText(notes.title)
            binding.editNote.setText(notes.note)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}