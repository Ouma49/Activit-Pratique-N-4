package com.example.keynote.service;

import com.example.keynote.domain.Keynote;
import com.example.keynote.repository.KeynoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeynoteService {

    private final KeynoteRepository repository;

    public KeynoteService(KeynoteRepository repository) {
        this.repository = repository;
    }

    public Keynote save(Keynote k) { return repository.save(k); }
    public List<Keynote> findAll() { return repository.findAll(); }
    public Optional<Keynote> findById(Long id) { return repository.findById(id); }
    public void deleteById(Long id) { repository.deleteById(id); }
}
