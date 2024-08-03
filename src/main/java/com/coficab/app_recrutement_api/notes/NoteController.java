package com.coficab.app_recrutement_api.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/get-note")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @PostMapping("/add-note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        Note savedNote = noteService.add(note);
        return ResponseEntity.ok(savedNote);
    }

    @PostMapping("/save-notes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saveNotes(@RequestBody List<String> notes) {
        noteService.saveAll(notes);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-note/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateNote(@PathVariable Long id, @RequestBody String content) {
        noteService.update(id, content);
    }

    @DeleteMapping("/delete-note/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteNote(@PathVariable Long id) {
        noteService.delete(id);
    }
}
