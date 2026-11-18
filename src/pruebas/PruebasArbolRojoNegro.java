package pruebas;

import modelo.ArbolRojoNegro;


public class PruebasArbolRojoNegro {
    private static int verificaciones = 0;

    private static void comprobar(boolean condicion, String mensaje) {
        verificaciones++;
        if (!condicion) {
            throw new AssertionError("FALLO: " + mensaje);
        }
    }

    private static void ejemplo(String nombre, int[] numeros) {
        ArbolRojoNegro arbol = new ArbolRojoNegro();
        for (int numero : numeros) {
            comprobar(arbol.insertar(numero), nombre + ": no se insertó " + numero);
            comprobar(arbol.esValido(), nombre + ": " + arbol.verificarPropiedades());
        }
        comprobar(arbol.dibujar().startsWith("└── RAÍZ: 20 [N]"),
                nombre + ": se esperaba raíz negra 20");
        comprobar(arbol.inOrden().equals("[10, 20, 30]"), nombre + ": inorden incorrecto");
        System.out.println("OK: " + nombre);
    }

    private static void ejemplos() {
        ejemplo("rotación izquierda", new int[] {10, 20, 30});
        ejemplo("rotación derecha", new int[] {30, 20, 10});
        ejemplo("doble izquierda-derecha", new int[] {30, 10, 20});
        ejemplo("doble derecha-izquierda", new int[] {10, 30, 20});

        ArbolRojoNegro recoloreo = new ArbolRojoNegro();
        for (int numero : new int[] {10, 5, 15, 1}) {
            comprobar(recoloreo.insertar(numero), "insertar recoloreo " + numero);
            comprobar(recoloreo.esValido(), "recoloreo debe ser válido");
        }
        comprobar(recoloreo.dibujar().contains("5 [N]"), "padre debe quedar negro");
        comprobar(recoloreo.dibujar().contains("15 [N]"), "tío debe quedar negro");
        System.out.println("OK: recoloreo");
    }

    private static void eliminaciones() {
        ArbolRojoNegro arbol = new ArbolRojoNegro();
        int[] valores = {11, 2, 14, 1, 7, 15, 5, 8, 4, -5, 0, 17, 6, 9};
        for (int valor : valores) {
            comprobar(arbol.insertar(valor), "insertar " + valor);
            comprobar(arbol.esValido(), "invariante tras insertar " + valor);
        }
        comprobar(!arbol.insertar(11), "no debe permitir duplicados");
        comprobar(!arbol.eliminar(500), "no debe eliminar inexistentes");
        comprobar(arbol.esValido(), "invariante tras eliminar inexistentes");
        for (int valor : valores) {
            comprobar(arbol.buscar(valor), "debe encontrar " + valor);
            comprobar(arbol.eliminar(valor), "eliminar " + valor);
            comprobar(!arbol.buscar(valor), "ya no debe encontrarse " + valor);
            comprobar(arbol.esValido(), "invariante tras eliminar " + valor + ": "
                    + arbol.verificarPropiedades());
        }
        comprobar(arbol.cantidad() == 0, "debe estar vacío");
        comprobar(arbol.inOrden().equals("[]"), "inorden vacío");
        comprobar(arbol.esValido(), "árbol vacío válido");
        arbol.insertar(Integer.MIN_VALUE);
        arbol.insertar(Integer.MAX_VALUE);
        comprobar(arbol.esValido(), "enteros extremos");
        arbol.vaciar();
        comprobar(arbol.esValido(), "vaciado");
        System.out.println("OK: eliminación de hojas, raíz, 1-2 hijos, extremos y vaciado");
    }

    private static String referenciaInOrden(boolean[] presentes) {
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < presentes.length; i++) {
            if (presentes[i]) {
                if (texto.length() > 1) {
                    texto.append(", ");
                }
                texto.append(i - 200);
            }
        }
        return texto.append(']').toString();
    }

    private static void pruebaAleatoriaDeterminista() {
        ArbolRojoNegro arbol = new ArbolRojoNegro();
        boolean[] presentes = new boolean[401];
        int cuenta = 0;
        long semilla = 123456789L;
        for (int i = 0; i < 6000; i++) {
            semilla = (semilla * 1664525L + 1013904223L) & 0xFFFFFFFFL;
            int posicion = (int) (semilla % presentes.length);
            int valor = posicion - 200;
            boolean insertar = ((semilla >>> 12) & 1L) == 0L;
            if (insertar) {
                comprobar(arbol.insertar(valor) == !presentes[posicion],
                        "resultado de insertar en paso " + i);
                if (!presentes[posicion]) {
                    presentes[posicion] = true;
                    cuenta++;
                }
            } else {
                comprobar(arbol.eliminar(valor) == presentes[posicion],
                        "resultado de eliminar en paso " + i);
                if (presentes[posicion]) {
                    presentes[posicion] = false;
                    cuenta--;
                }
            }
            comprobar(arbol.cantidad() == cuenta, "cantidad en paso " + i);
            comprobar(arbol.esValido(), "invariante en paso " + i + ": "
                    + arbol.verificarPropiedades());
            comprobar(arbol.inOrden().equals(referenciaInOrden(presentes)),
                    "orden tras paso " + i);
        }
        for (int i = 0; i < presentes.length; i++) {
            int valor = i - 200;
            comprobar(arbol.buscar(valor) == presentes[i], "búsqueda en " + valor);
            if (presentes[i]) {
                comprobar(arbol.eliminar(valor), "vaciado al final: " + valor);
                comprobar(arbol.esValido(), "corrección en vaciado final");
            }
        }
        comprobar(arbol.cantidad() == 0 && arbol.esValido(), "vaciado final completo");
        System.out.println("OK: 6000 operaciones deterministas + vaciado final");
    }

    public static void main(String[] args) {
        ejemplos();
        eliminaciones();
        pruebaAleatoriaDeterminista();
        System.out.println("\nTODAS LAS PRUEBAS PASARON (" + verificaciones + " verificaciones).");
    }
}
