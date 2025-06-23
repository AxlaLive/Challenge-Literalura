package com.challenge_literalura.axla.principal;

import com.challenge_literalura.axla.model.Datos;
import com.challenge_literalura.axla.model.DatosLibros;
import com.challenge_literalura.axla.model.Libro;
import com.challenge_literalura.axla.repository.LibroRepository;
import com.challenge_literalura.axla.service.Consumo_API;
import com.challenge_literalura.axla.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    @Autowired
    private Consumo_API consumoAPI;
    @Autowired
    private ConvierteDatos conversor;
    @Autowired
    private LibroRepository libroRepository;
    private Scanner teclado = new Scanner(System.in);

    public void menu(){
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("""
                    \n *** Catalogo de Libros ***
                    1 - Buscar libros por titulo
                    2 - Mostrar top 10 libros mas descargados
                    3 - Guardar libros en la base de datos
                    4 - Mostrar todos los libros guardados
                    5 - Mostrar estadisticos 
                    0 - Salir
                    Elige una opcion:
                    """);
            opcion = Integer.parseInt(teclado.nextLine());
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    guardarLibro();
                    break;
                case 3:
                    mostrarLibros();
                    break;
                case 4:
                    mostrarTop10();
                    break;
                case 5:
                    mostrarEstadisticas();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
    private void buscarLibroPorTitulo() {
        System.out.print("Ingrese el título del libro: ");
        var titulo = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + titulo.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroEncontrado = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(titulo.toUpperCase()))
                .findFirst();

        if (libroEncontrado.isPresent()) {
            System.out.println("Libro encontrado:");
            System.out.println(libroEncontrado.get());
        } else {
            System.out.println("Libro no encontrado.");
        }
    }
    private void guardarLibro() {
        System.out.print("Ingrese el título del libro para guardar: ");
        var titulo = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + titulo.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroEncontrado = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(titulo.toUpperCase()))
                .findFirst();

        if (libroEncontrado.isPresent()) {
            var datosLibro = libroEncontrado.get();
            Libro libro = new Libro(datosLibro); // Debes tener constructor en Libro
            libroRepository.save(libro);
            System.out.println("Libro guardado en la base de datos.");
        } else {
            System.out.println("Libro no encontrado, no se guardó.");
        }
    }
    private void mostrarLibros() {
        var libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }
    private void mostrarTop10() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println("Top 10 libros más descargados:");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .forEach(l -> System.out.println(l.titulo() + " - " + l.numeroDeDescargas() + " descargas"));
    }
    private void mostrarEstadisticas() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);
        DoubleSummaryStatistics estadisticas = datos.resultados().stream()
                .filter(l -> l.numeroDeDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));

        System.out.println("Estadísticas de descargas:");
        System.out.println("Promedio: " + estadisticas.getAverage());
        System.out.println("Máximo: " + estadisticas.getMax());
        System.out.println("Mínimo: " + estadisticas.getMin());
        System.out.println("Cantidad de libros: " + estadisticas.getCount());
    }
}
