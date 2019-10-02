package asint.ast;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.sentencias.Sentencia;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representa un bloque de sentencias Napol. Estara formado por una lista de
 * sentencias de nuestro programa. Esto sera, por ejemplo, lo que aparezca
 * dentro del cuerpo de un while, un if, o el conjunto de instrucciones de
 * una funcion.
 */
public class BloqueSentencias {

    /**
     * Lista de sentencias del bloque.
     */
    private LinkedList<Sentencia> sentencias;

    /**
     * Numero de decalraciones de variables locales de un bloque.
     */
    private int variablesLocales;


    public BloqueSentencias(LinkedList<Sentencia> sentencias) {
        this.sentencias = sentencias;
    }

    /**
     * Realiza la comprobacion de ambitos de un bloque de sentencias.
     * No abre ni cierra bloque de la tabla de simbolos.
     *
     * @param ts Tabla de simbolos con la que se realizara la identificacion
     *           de identificadores.
     */
    public void checkAmbitos(TablaSimbolos ts) {
        for (Sentencia s : sentencias) {
            s.checkAmbitos(ts);
        }
    }

    /**
     * Realiza la comprobacion estatica de tipos de un bloque de sentencias.
     */
    public void checkTipos() {
        for (Sentencia s : sentencias) {
            s.checkTipos();
        }
    }

    /**
     * Calcula la rho usada en la generacion de codigo y los niveles de uso y
     * declaracion.
     *
     * @param i     indice de la rho actual.
     * @param nivel nivel actual.
     * @return rho al terminar la sentencia
     */
    public int precalculo(int i, int nivel) {
        int x = i;
        variablesLocales = 0;
        for (Sentencia s : sentencias) {
            x = s.precalculo(x, nivel);
            variablesLocales += s.declarVarLocales();
        }
        return x;
    }

    /**
     * Longitud pila evaluacion expresiones.
     *
     * @return Longitud de la pila de evaluacion de expresiones.
     */
    public int longitudPilaEvaluacionExpresiones() {
        int max = 0;
        for (Sentencia s : sentencias) {
            max = Math.max(max, s.longitudPilaEvaluacionExpresiones());
        }
        return max;
    }

    /**
     * @return Numero de variables locales declaradas en el bloque.
     */
    public int getVariablesLocales() {
        return variablesLocales;
    }

    /**
     * Se encarga de generar el codigo del bloque de sentencias.
     *
     * @param codigo Objeto donde vamos construyendo el codigo
     */
    public void generaCodigo(ArrayList<String> codigo) {
        for (Sentencia s : sentencias) {
            s.generaCodigo(codigo);
        }

    }

    @Override
    public String toString() {
        return sentencias.toString();
    }
}
