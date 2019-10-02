package errors;

import asint.ast.tipos.Tipo;
import java_cup.runtime.Symbol;

public class GestionErroresNapol {

    public static boolean errorAmbitos = false;

    public static boolean errorTipos = false;

    private GestionErroresNapol() {
        throw new IllegalStateException("Clase no instanciable");
    }

    //========== ERRORES VARIOS ==========

    public static void errorExtensionArchivo() {
        System.out.println("ERROR EN LA EXTENSION DEL ARCHIVO. Introduzca un archivo con la extension '.napol'");
        System.exit(1);
    }

    public static void ficheroNoEncontrado() {
        System.out.println("ERROR, NO SE ENCUENTRA EL ARCHIVO. Introduzca un archivo existente.");
        System.exit(1);
    }

    //========== ERRORES LEXICOS ==========

    public static void errorLexico(int fila, int columna, String lexema) {
        System.out.println("ERROR LEXICO en fila " + fila + " y columna " + columna + ". Caracter inesperado: "
                + lexema);
        System.exit(1);
    }

    //========== ERRORES SINTACTICOS ==========

    public static void errorSintactico(Symbol sym) {
        System.out.println("ERROR SINTACTICO en fila " + sym.left + " y columna " + sym.right +
                ". Elemento inesperado: " + sym.value);
        System.exit(1);
    }

    //========== ERRORES DE AMBITOS ==========

    public static void errorAmbitosIdentificadorDuplicado(int fila, int columna, String id) {
        System.out.println("ERROR DE AMBITOS en fila " + fila + " y columna " + columna
                + ". Declaracion duplicada: " + id);
        errorAmbitos = true;
    }

    public static void errorAmbitosNoDeclaracion(int fila, int columna, String id) {
        System.out.println("ERROR DE AMBITOS en fila " + fila + " y columna " + columna
                + ". El siguiente identificador no ha sido declarado: " + id);
        errorAmbitos = true;
    }

    //========== ERRORES DE TIPOS ==========

    public static void errorTipos(int fila, int columna, Tipo t1, Tipo t2) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ". Se esperaba " + t1.getNombre() + " y se ha " +
                "recibido " + t2.getNombre());
        errorTipos = true;
    }

    public static void errorTiposDistintos(int fila, int columna) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ". No pueden ser tipos distintos.");
        errorTipos = true;
    }

    public static void errorNumDimensiones(int fila, int columna) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ". Numero de dimensiones no valido.");
        errorTipos = true;
    }

    public static void errorTamDimensiones(int fila, int columna) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ". El tamanyo de las dimensiones no coincide.");
        errorTipos = true;
    }

    public static void errorTiposDimensiones(int fila, int columna, Tipo t1,
                                             Tipo t2, int numDim1, int numDim2) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ". Se esperaba " + t1.getNombre() + " de " + numDim1 +
                " dimensiones y se ha recibido " + t2.getNombre() + " de " +
                numDim2 + " dimensiones.");
        errorTipos = true;
    }

    public static void errorAsignacionArray(int fila, int columna) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ". No se pueden asignar arrays.");
        errorTipos = true;
    }

    public static void errorTiposReturn(int fila, int columna, Tipo t1, Tipo t2) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ". Se esperaba como tipo devuelto " + t1.getNombre() +
                " y se ha recibido " + t2.getNombre() + ".");
        errorTipos = true;
    }

    public static void errorTiposReturnArray(int fila, int columna) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ".No se pueden devolver arrays" + ".");
        errorTipos = true;
    }

    public static void errorNumArgumentos(int fila, int columna, int
            numArgum1, int numArgum2) {
        System.out.println("ERROR DE TIPOS en fila " + fila + " y columna " +
                columna + ". Se esperaban " + numArgum1 + " argumentos y se " +
                "han recibido " + numArgum2 + ".");
        errorTipos = true;
    }
}
