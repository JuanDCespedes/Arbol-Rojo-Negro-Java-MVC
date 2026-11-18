package app;

import controlador.ControladorArbol;
import modelo.ArbolRojoNegro;
import vista.VistaConsola;

/** Punto de entrada: instancia y conecta Modelo, Vista y Controlador. */
public class Main {
    public static void main(String[] args) {
        ArbolRojoNegro modelo = new ArbolRojoNegro();
        VistaConsola vista = new VistaConsola();
        ControladorArbol controlador = new ControladorArbol(modelo, vista);
        controlador.ejecutar();
    }
}
