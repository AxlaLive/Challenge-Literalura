package com.challenge_literalura.axla.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autores;
    private String idiomas;
    private Double numeroDeDescargas;
    public Libro() {}

    public Libro(DatosLibros d) {
        this.titulo = d.titulo();
        this.autores = d.autores().stream()
                .map(DatosAutor::name)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Desconocido");
        this.idiomas = String.join(", ", d.idiomas());
        this.numeroDeDescargas = d.numeroDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autores;
    }

    public void setAutor(String autor) {
        this.autores = autor;
    }

    public String getIdioma() {
        return idiomas;
    }

    public void setIdioma(String idioma) {
        this.idiomas = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", autor='" + autores + '\'' +
                ", idioma='" + idiomas + '\'' +
                ", descargas=" + numeroDeDescargas +
                '}';
    }
}
