/*
Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.amazonaws.mobile.samples.awsomenotes.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.amazonaws.mobile.samples.awsomenotes.models.Note
import com.amazonaws.mobile.samples.awsomenotes.repository.NotesRepository

class NoteDetailViewModel(private val notesRepository: NotesRepository) : ViewModel() {
    /**
     * Observable note
     */
    private val mNote: MutableLiveData<Note> = MutableLiveData()
    val note: LiveData<Note> = mNote

    /**
     * The fields for data binding
     */
    val title: MutableLiveData<String> = MutableLiveData()
    val content: MutableLiveData<String> = MutableLiveData()

    /**
     * Set the note ID for the current note to load the note
     */
    fun setNoteId(noteId: String) {
        check(noteId.isNotBlank()) { "Note ID cannot be blank" }
        notesRepository.get(noteId) {
            val note = it ?: Note(noteId)
            mNote.postValue(note)
            title.postValue(note.title)
            content.postValue(note.content)
        }
    }

    /**
     * Save the note to the backing store
     */
    fun saveNote(note: Note) {
        check(note.noteId.isNotBlank()) { "Note ID cannot be blank" }
        notesRepository.save(note) { mNote.postValue(note) }
    }
}
