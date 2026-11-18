package controlador;

import modelo.ArbolRojoNegro;
import vista.VistaConsola;


public class ControladorArbol {
    private ArbolRojoNegro modelo;
    private final VistaConsola vista;

    public ControladorArbol(ArbolRojoNegro modelo, VistaConsola vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void ejecutar() {
        vista.bienvenida();
        while (true) {
            vista.mostrarMenu();
            Integer opcion = vista.leerEntero("Selecciona una opción: ");
            if (opcion == null) {
                vista.mensaje("Fin de la entrada. Programa cerrado.");
                return;
            }
            switch (opcion.intValue()) {
                case 1:
                    Integer nuevo = vista.leerEntero("Número a insertar: ");
                    if (nuevo != null) {
                        vista.mensaje(modelo.insertar(nuevo) ? "Valor insertado." : "El valor ya existe.");
                        mostrarEstado();
                    }
                    break;
                case 2:
                    Integer buscado = vista.leerEntero("Número a buscar: ");
                    if (buscado != null) {
                        vista.mensaje(modelo.buscar(buscado) ? "Valor encontrado." : "Valor no encontrado.");
                    }
                    break;
                case 3:
                    Integer eliminado = vista.leerEntero("Número a eliminar: ");
                    if (eliminado != null) {
                        vista.mensaje(modelo.eliminar(eliminado) ? "Valor eliminado." : "Valor no encontrado.");
                        mostrarEstado();
                    }
                    break;
                case 4:
                    vista.mostrarArbol(modelo.dibujar());
                    break;
                case 5:
                    vista.mensaje("Inorden: " + modelo.inOrden());
                    break;
                case 6:
                    vista.mensaje(modelo.verificarPropiedades());
                    break;
                case 7:
                    cargarEjemplo();
                    break;
                case 8:
                    vista.mostrarPasos(modelo.obtenerPasos());
                    break;
                case 9:
                    modelo.vaciar();
                    vista.mensaje("Árbol vacío.");
                    break;
                case 0:
                    vista.mensaje("Programa finalizado. ¡Hasta luego!");
                    return;
                default:
                    vista.mensaje("Opción incorrecta.");
            }
        }
    }

    private void mostrarEstado() {
        vista.mostrarArbol(modelo.dibujar());
        vista.mensaje(modelo.verificarPropiedades());
        vista.mostrarPasos(modelo.obtenerPasos());
    }

    private void cargarEjemplo() {
        vista.mostrarEjemplos();
        Integer opcion = vista.leerEntero("Elige un ejemplo (1-5): ");
        if (opcion == null) {
            return;
        }
        int[] numeros;
        String nombre;
        switch (opcion.intValue()) {
            case 1:
                nombre = "Rotación izquierda";
                numeros = new int[] {10, 20, 30};
                break;
            case 2:
                nombre = "Rotación derecha";
                numeros = new int[] {30, 20, 10};
                break;
            case 3:
                nombre = "Doble izquierda-derecha";
                numeros = new int[] {30, 10, 20};
                break;
            case 4:
                nombre = "Doble derecha-izquierda";
                numeros = new int[] {10, 30, 20};
                break;
            case 5:
                nombre = "Recoloreo";
                numeros = new int[] {10, 5, 15, 1};
                break;
            default:
                vista.mensaje("Ejemplo no válido.");
                return;
        }
        modelo = new ArbolRojoNegro();
        vista.mensaje("\nEjemplo seleccionado: " + nombre);
        for (int valor : numeros) {
            modelo.insertar(valor);
            vista.mensaje("\nDespués de insertar " + valor + ":");
            vista.mostrarArbol(modelo.dibujar());
            vista.mostrarPasos(modelo.obtenerPasos());
        }
        vista.mensaje(modelo.verificarPropiedades());
    }
}
