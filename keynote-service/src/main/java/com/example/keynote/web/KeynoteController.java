package com.example.keynote.web;

import com.example.keynote.domain.Keynote;
import com.example.keynote.dto.KeynoteDTO;
import com.example.keynote.mapper.KeynoteMapper;
import com.example.keynote.service.KeynoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/keynotes")
public class KeynoteController {

    private final KeynoteService service;

    public KeynoteController(KeynoteService service) { this.service = service; }

    @GetMapping
    public List<KeynoteDTO> list() {
        return service.findAll().stream().map(KeynoteMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<KeynoteDTO> create(@RequestBody KeynoteDTO dto) {
        Keynote k = KeynoteMapper.toEntity(dto);
        Keynote saved = service.save(k);
        return ResponseEntity.created(URI.create("/api/keynotes/" + saved.getId())).body(KeynoteMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeynoteDTO> get(@PathVariable("id") Long id) {
        return service.findById(id).map(k -> ResponseEntity.ok(KeynoteMapper.toDto(k))).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
