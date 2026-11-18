package modelo;

/**
 * Árbol rojo-negro clásico con centinela NIL negro compartido.
 * Búsqueda, inserción y eliminación: O(log n) en el peor caso.
 *
 * El modelo NO imprime nada, no pide datos y no conoce la vista.
 * No importa ninguna biblioteca externa ni colecciones del JDK.
 */
public class ArbolRojoNegro {
    private final Nodo nil;
    private Nodo raiz;
    private int cantidad;
    private final StringBuilder historial;
    private int numeroPaso;

    public ArbolRojoNegro() {
        nil = new Nodo(0, false);
        nil.izquierdo = nil;
        nil.derecho = nil;
        nil.padre = nil;
        raiz = nil;
        cantidad = 0;
        historial = new StringBuilder();
        numeroPaso = 0;
    }

    private void iniciarHistorial() {
        historial.setLength(0);
        numeroPaso = 0;
    }

    private void anotar(String texto) {
        historial.append(++numeroPaso).append(". ").append(texto).append('\n');
    }

    public String obtenerPasos() {
        if (historial.length() == 0) {
            return "No hay operaciones registradas todavía.";
        }
        return historial.toString();
    }

    public int cantidad() {
        return cantidad;
    }

    public void vaciar() {
        raiz = nil;
        nil.padre = nil;
        nil.rojo = false;
        cantidad = 0;
        iniciarHistorial();
        anotar("Árbol vaciado.");
    }

    // ------------------ BÚSQUEDA ------------------
    private Nodo encontrar(int valor) {
        Nodo actual = raiz;
        while (actual != nil) {
            if (valor == actual.valor) {
                return actual;
            }
            actual = valor < actual.valor ? actual.izquierdo : actual.derecho;
        }
        return nil;
    }

    public boolean buscar(int valor) {
        return encontrar(valor) != nil;
    }

    // ------------------ ROTACIONES ------------------
    private void rotarIzquierda(Nodo x) {
        Nodo y = x.derecho;
        if (y == nil) {
            throw new IllegalStateException("No hay hijo derecho para rotar.");
        }
        x.derecho = y.izquierdo;
        if (y.izquierdo != nil) {
            y.izquierdo.padre = x;
        }
        y.padre = x.padre;
        if (x.padre == nil) {
            raiz = y;
        } else if (x == x.padre.izquierdo) {
            x.padre.izquierdo = y;
        } else {
            x.padre.derecho = y;
        }
        y.izquierdo = x;
        x.padre = y;
        anotar("Rotación IZQUIERDA alrededor de " + x.valor + "; sube " + y.valor + ".");
    }

    private void rotarDerecha(Nodo x) {
        Nodo y = x.izquierdo;
        if (y == nil) {
            throw new IllegalStateException("No hay hijo izquierdo para rotar.");
        }
        x.izquierdo = y.derecho;
        if (y.derecho != nil) {
            y.derecho.padre = x;
        }
        y.padre = x.padre;
        if (x.padre == nil) {
            raiz = y;
        } else if (x == x.padre.derecho) {
            x.padre.derecho = y;
        } else {
            x.padre.izquierdo = y;
        }
        y.derecho = x;
        x.padre = y;
        anotar("Rotación DERECHA alrededor de " + x.valor + "; sube " + y.valor + ".");
    }

    // ------------------ INSERCIÓN ------------------
    public boolean insertar(int valor) {
        iniciarHistorial();
        Nodo padre = nil;
        Nodo actual = raiz;
        while (actual != nil) {
            padre = actual;
            if (valor == actual.valor) {
                anotar("El valor " + valor + " ya existe. No se admiten duplicados.");
                return false;
            }
            actual = valor < actual.valor ? actual.izquierdo : actual.derecho;
        }

        Nodo nuevo = new Nodo(valor, true);
        nuevo.padre = padre;
        nuevo.izquierdo = nil;
        nuevo.derecho = nil;
        if (padre == nil) {
            raiz = nuevo;
        } else if (valor < padre.valor) {
            padre.izquierdo = nuevo;
        } else {
            padre.derecho = nuevo;
        }
        cantidad++;
        anotar("Se inserta " + valor + " como nodo ROJO.");
        corregirInsercion(nuevo);
        return true;
    }

    private void corregirInsercion(Nodo nodo) {
        while (nodo.padre.rojo) {
            Nodo abuelo = nodo.padre.padre;
            if (nodo.padre == abuelo.izquierdo) {
                Nodo tio = abuelo.derecho;
                if (tio.rojo) {
                    anotar("Padre " + nodo.padre.valor + " y tío " + tio.valor
                            + " ROJOS: se recolorean y el conflicto sube al abuelo.");
                    nodo.padre.rojo = false;
                    tio.rojo = false;
                    abuelo.rojo = true;
                    nodo = abuelo;
                } else {
                    if (nodo == nodo.padre.derecho) {
                        anotar("Caso izquierda-derecha: primera parte de rotación doble.");
                        nodo = nodo.padre;
                        rotarIzquierda(nodo);
                    }
                    nodo.padre.rojo = false;
                    nodo.padre.padre.rojo = true;
                    rotarDerecha(nodo.padre.padre);
                }
            } else {
                Nodo tio = abuelo.izquierdo;
                if (tio.rojo) {
                    anotar("Padre " + nodo.padre.valor + " y tío " + tio.valor
                            + " ROJOS: se recolorean y el conflicto sube al abuelo.");
                    nodo.padre.rojo = false;
                    tio.rojo = false;
                    abuelo.rojo = true;
                    nodo = abuelo;
                } else {
                    if (nodo == nodo.padre.izquierdo) {
                        anotar("Caso derecha-izquierda: primera parte de rotación doble.");
                        nodo = nodo.padre;
                        rotarDerecha(nodo);
                    }
                    nodo.padre.rojo = false;
                    nodo.padre.padre.rojo = true;
                    rotarIzquierda(nodo.padre.padre);
                }
            }
        }
        if (raiz.rojo) {
            anotar("Se establece la raíz en NEGRO.");
        }
        raiz.rojo = false;
    }

    // ------------------ ELIMINACIÓN ------------------
    private void trasplantar(Nodo viejo, Nodo nuevo) {
        if (viejo.padre == nil) {
            raiz = nuevo;
        } else if (viejo == viejo.padre.izquierdo) {
            viejo.padre.izquierdo = nuevo;
        } else {
            viejo.padre.derecho = nuevo;
        }
        // También se asigna el padre al NIL: es necesario en el ajuste de borrado.
        nuevo.padre = viejo.padre;
    }

    private Nodo minimo(Nodo n) {
        while (n.izquierdo != nil) {
            n = n.izquierdo;
        }
        return n;
    }

    public boolean eliminar(int valor) {
        iniciarHistorial();
        Nodo z = encontrar(valor);
        if (z == nil) {
            anotar("El valor " + valor + " no existe: no se eliminó nada.");
            return false;
        }
        Nodo y = z;
        boolean colorOriginalRojo = y.rojo;
        Nodo x;

        if (z.izquierdo == nil) {
            x = z.derecho;
            trasplantar(z, z.derecho);
        } else if (z.derecho == nil) {
            x = z.izquierdo;
            trasplantar(z, z.izquierdo);
        } else {
            y = minimo(z.derecho); // sucesor en el recorrido inorden
            colorOriginalRojo = y.rojo;
            x = y.derecho;
            if (y.padre == z) {
                x.padre = y;
            } else {
                trasplantar(y, y.derecho);
                y.derecho = z.derecho;
                y.derecho.padre = y;
            }
            trasplantar(z, y);
            y.izquierdo = z.izquierdo;
            y.izquierdo.padre = y;
            y.rojo = z.rojo;
        }
        cantidad--;
        anotar("Se elimina el valor " + valor + ".");
        if (!colorOriginalRojo) {
            anotar("El nodo retirado de su posición era NEGRO: corregir altura negra.");
            corregirEliminacion(x);
        } else {
            anotar("El nodo retirado era ROJO: no altera la altura negra.");
        }
        raiz.rojo = false;
        nil.rojo = false;
        return true;
    }

    private void corregirEliminacion(Nodo x) {
        while (x != raiz && !x.rojo) {
            if (x == x.padre.izquierdo) {
                Nodo hermano = x.padre.derecho;
                if (hermano.rojo) {
                    anotar("Hermano derecho ROJO: recolorear y rotar a la izquierda.");
                    hermano.rojo = false;
                    x.padre.rojo = true;
                    rotarIzquierda(x.padre);
                    hermano = x.padre.derecho;
                }
                if (!hermano.izquierdo.rojo && !hermano.derecho.rojo) {
                    anotar("Hermano negro con hijos negros: trasladar déficit hacia arriba.");
                    if (hermano != nil) {
                        hermano.rojo = true;
                    }
                    x = x.padre;
                } else {
                    if (!hermano.derecho.rojo) {
                        anotar("Sobrino interior ROJO: rotación derecha del hermano.");
                        hermano.izquierdo.rojo = false;
                        hermano.rojo = true;
                        rotarDerecha(hermano);
                        hermano = x.padre.derecho;
                    }
                    hermano.rojo = x.padre.rojo;
                    x.padre.rojo = false;
                    hermano.derecho.rojo = false;
                    rotarIzquierda(x.padre);
                    x = raiz;
                }
            } else {
                Nodo hermano = x.padre.izquierdo;
                if (hermano.rojo) {
                    anotar("Hermano izquierdo ROJO: recolorear y rotar a la derecha.");
                    hermano.rojo = false;
                    x.padre.rojo = true;
                    rotarDerecha(x.padre);
                    hermano = x.padre.izquierdo;
                }
                if (!hermano.derecho.rojo && !hermano.izquierdo.rojo) {
                    anotar("Hermano negro con hijos negros: trasladar déficit hacia arriba.");
                    if (hermano != nil) {
                        hermano.rojo = true;
                    }
                    x = x.padre;
                } else {
                    if (!hermano.izquierdo.rojo) {
                        anotar("Sobrino interior ROJO: rotación izquierda del hermano.");
                        hermano.derecho.rojo = false;
                        hermano.rojo = true;
                        rotarIzquierda(hermano);
                        hermano = x.padre.izquierdo;
                    }
                    hermano.rojo = x.padre.rojo;
                    x.padre.rojo = false;
                    hermano.izquierdo.rojo = false;
                    rotarDerecha(x.padre);
                    x = raiz;
                }
            }
        }
        x.rojo = false;
    }

    // ------------------ RECORRIDO INORDEN ------------------
    public String inOrden() {
        StringBuilder salida = new StringBuilder("[");
        recorrerInOrden(raiz, salida);
        if (salida.length() > 1) {
            salida.setLength(salida.length() - 2);
        }
        salida.append(']');
        return salida.toString();
    }

    private void recorrerInOrden(Nodo nodo, StringBuilder salida) {
        if (nodo == nil) {
            return;
        }
        recorrerInOrden(nodo.izquierdo, salida);
        salida.append(nodo.valor).append(", ");
        recorrerInOrden(nodo.derecho, salida);
    }

    // ------------------ DIBUJO POR CONSOLA ------------------
    public String dibujar() {
        if (raiz == nil) {
            return "(árbol vacío)";
        }
        StringBuilder salida = new StringBuilder();
        salida.append("└── RAÍZ: ").append(raiz.valor).append(" [N]\n");
        dibujarHijos(raiz, "    ", salida);
        return salida.toString();
    }

    private void dibujarHijos(Nodo nodo, String prefijo, StringBuilder salida) {
        if (nodo.izquierdo != nil) {
            boolean otro = nodo.derecho != nil;
            dibujarRama(nodo.izquierdo, prefijo, otro ? "├── I: " : "└── I: ",
                    otro ? "│   " : "    ", salida);
        }
        if (nodo.derecho != nil) {
            dibujarRama(nodo.derecho, prefijo, "└── D: ", "    ", salida);
        }
    }

    private void dibujarRama(Nodo nodo, String prefijo, String etiqueta,
                             String continuacion, StringBuilder salida) {
        salida.append(prefijo).append(etiqueta).append(nodo.valor)
                .append(nodo.rojo ? " [R]" : " [N]").append('\n');
        dibujarHijos(nodo, prefijo + continuacion, salida);
    }

    // ------------------ VERIFICACIÓN DE LAS PROPIEDADES ------------------
    private static class Revision {
        final boolean valido;
        final int alturaNegra;
        final int cantidad;
        final String error;

        Revision(boolean valido, int alturaNegra, int cantidad, String error) {
            this.valido = valido;
            this.alturaNegra = alturaNegra;
            this.cantidad = cantidad;
            this.error = error;
        }
    }

    private Revision error(String mensaje) {
        return new Revision(false, 0, 0, mensaje);
    }

    private Revision revisarNodo(Nodo nodo, Nodo padreEsperado, long minimo, long maximo) {
        if (nodo == nil) {
            return new Revision(true, 1, 0, ""); // NIL es negro y cuenta 1
        }
        if (nodo == null || nodo.izquierdo == null || nodo.derecho == null) {
            return error("Hay referencias nulas en un nodo real.");
        }
        if (nodo.padre != padreEsperado) {
            return error("El enlace al padre es incorrecto en " + nodo.valor + ".");
        }
        if (nodo.valor <= minimo || nodo.valor >= maximo) {
            return error("Orden BST incorrecto en " + nodo.valor + ".");
        }
        if (nodo.rojo && (nodo.izquierdo.rojo || nodo.derecho.rojo)) {
            return error("Dos nodos rojos consecutivos cerca de " + nodo.valor + ".");
        }
        Revision izquierda = revisarNodo(nodo.izquierdo, nodo, minimo, nodo.valor);
        if (!izquierda.valido) {
            return izquierda;
        }
        Revision derecha = revisarNodo(nodo.derecho, nodo, nodo.valor, maximo);
        if (!derecha.valido) {
            return derecha;
        }
        if (izquierda.alturaNegra != derecha.alturaNegra) {
            return error("Altura negra diferente en los hijos de " + nodo.valor + ".");
        }
        return new Revision(true, izquierda.alturaNegra + (nodo.rojo ? 0 : 1),
                izquierda.cantidad + derecha.cantidad + 1, "");
    }

    public String verificarPropiedades() {
        if (nil.rojo || nil.izquierdo != nil || nil.derecho != nil) {
            return "INVÁLIDO: las hojas NIL deben ser negras.";
        }
        if (raiz == nil) {
            return cantidad == 0 ? "VÁLIDO: árbol vacío y NIL negro."
                    : "INVÁLIDO: cantidad incompatible con árbol vacío.";
        }
        if (raiz.rojo) {
            return "INVÁLIDO: la raíz debe ser negra.";
        }
        if (raiz.padre != nil) {
            return "INVÁLIDO: la raíz no debe tener padre real.";
        }
        Revision r = revisarNodo(raiz, nil, Long.MIN_VALUE, Long.MAX_VALUE);
        if (!r.valido) {
            return "INVÁLIDO: " + r.error;
        }
        if (r.cantidad != cantidad) {
            return "INVÁLIDO: cantidad de nodos inconsistente.";
        }
        return "VÁLIDO: cinco propiedades, orden BST y enlaces correctos. "
                + "Nodos: " + cantidad + ". Altura negra (incluye NIL): " + r.alturaNegra + ".";
    }

    public boolean esValido() {
        return verificarPropiedades().startsWith("VÁLIDO:");
    }
}
