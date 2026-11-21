package vista;


public class VistaConsola {
    public void bienvenida() {
        System.out.println("========================================");
        System.out.println("      ÁRBOL ROJO-NEGRO EN JAVA MVC      ");
        System.out.println("========================================");
        System.out.println("N = negro; R = rojo; I = izquierdo; D = derecho.");
    }

    public void mostrarMenu() {
        System.out.println("\n========== MENÚ PRINCIPAL ==========");
        System.out.println("1. Insertar número");
        System.out.println("2. Buscar número");
        System.out.println("3. Eliminar número");
        System.out.println("4. Mostrar árbol");
        System.out.println("5. Recorrido inorden");
        System.out.println("6. Verificar las cinco propiedades");
        System.out.println("7. Cargar ejemplo");
        System.out.println("8. Mostrar pasos de la última operación");
        System.out.println("9. Vaciar árbol");
        System.out.println("0. Salir");
    }

    public void mostrarEjemplos() {
        System.out.println("\n1. Rotación izquierda: 10, 20, 30");
        System.out.println("2. Rotación derecha: 30, 20, 10");
        System.out.println("3. Doble izquierda-derecha: 30, 10, 20");
        System.out.println("4. Doble derecha-izquierda: 10, 30, 20");
        System.out.println("5. Recoloreo: 10, 5, 15, 1");
    }

    public void mensaje(String texto) {
        System.out.println(texto);
    }

    public void mostrarArbol(String dibujo) {
        System.out.println("\n--- ESTRUCTURA DEL ÁRBOL ---");
        System.out.println(dibujo);
    }

    public void mostrarPasos(String pasos) {
        System.out.println("\n--- PASOS DEL ALGORITMO ---");
        System.out.print(pasos);
        if (!pasos.endsWith("\n")) {
            System.out.println();
        }
    }

    public String leerLinea(String texto) {
        System.out.print(texto);
        StringBuilder linea = new StringBuilder();
        while (true) {
            int caracter;
            try {
                caracter = System.in.read();
            } catch (Exception ex) {
                System.out.println("\nError leyendo la consola: " + ex.getMessage());
                return null;
            }
            if (caracter == -1) {
                return linea.length() == 0 ? null : linea.toString();
            }
            if (caracter == '\n') {
                return linea.toString();
            }
            if (caracter != '\r') {
                linea.append((char) caracter);
            }
        }
    }

    public Integer leerEntero(String texto) {
        while (true) {
            String entrada = leerLinea(texto);
            if (entrada == null) {
                return null;
            }
            try {
                return Integer.valueOf(entrada.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Escribe un número entero válido, por favor.");
            }
        }
    }
}
