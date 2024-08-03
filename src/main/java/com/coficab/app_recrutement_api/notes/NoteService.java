package com.coficab.app_recrutement_api.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note add(Note note) {
        return noteRepository.save(note);
    }

    public void saveAll(List<String> notes) {
        List<Note> noteList = notes.stream().map(content -> {
            Note note = new Note();
            note.setContent(content);
            return note;
        }).collect(Collectors.toList());
        noteRepository.saveAll(noteList);
    }

    public void update(Long id, String content) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(content);
        noteRepository.save(note);
    }

    public void delete(Long id) {
        noteRepository.deleteById(id);
    }
}
