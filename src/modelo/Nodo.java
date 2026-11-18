package modelo;

/**
 * Un nodo del árbol. Los enlaces y colores se gestionan desde el Modelo.
 * No utilizamos colecciones ni bibliotecas de árboles existentes.
 */
public class Nodo {
    final int valor;
    boolean rojo;
    Nodo izquierdo;
    Nodo derecho;
    Nodo padre;

    Nodo(int valor, boolean rojo) {
        this.valor = valor;
        this.rojo = rojo;
    }
}
