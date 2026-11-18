package modelo;


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
