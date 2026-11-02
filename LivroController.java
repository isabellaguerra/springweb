package com.exemplo.biblioteca.controller;

import com.exemplo.biblioteca.model.Livro;
import com.exemplo.biblioteca.repository.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroRepository repository;

    // Injecao de dependencia pelo construtor
    public LivroController(LivroRepository repository) {
        this.repository = repository;
    }

    // 1) CREATE - POST /livros
    @PostMapping
    public ResponseEntity<Livro> criar(@Valid @RequestBody Livro livro) {
        livro.setId(null); // garante que e um cadastro novo
        Livro salvo = repository.save(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // 2) READ (todos) - GET /livros
    @GetMapping
    public List<Livro> listar() {
        return repository.findAll();
    }

    // 3) READ (um) - GET /livros/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4) UPDATE - PUT /livros/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id,
                                           @Valid @RequestBody Livro dados) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setTitulo(dados.getTitulo());
                    existente.setAutor(dados.getAutor());
                    existente.setIsbn(dados.getIsbn());
                    existente.setAnoPublicacao(dados.getAnoPublicacao());
                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5) DELETE - DELETE /livros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
