package com.challenge_literalura.axla.repository;

import com.challenge_literalura.axla.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
