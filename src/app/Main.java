package app;

import controlador.ControladorArbol;
import modelo.ArbolRojoNegro;
import vista.VistaConsola;


public class Main {
    public static void main(String[] args) {
        ArbolRojoNegro modelo = new ArbolRojoNegro();
        VistaConsola vista = new VistaConsola();
        ControladorArbol controlador = new ControladorArbol(modelo, vista);
        controlador.ejecutar();
    }
}
