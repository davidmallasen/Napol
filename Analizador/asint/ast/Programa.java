package asint.ast;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.sentencias.Sentencia;
import asint.ast.sentencias.TipoS;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representa un programa Napol. Estara compuesto por una lista de sentencias.
 * Estas seran las unidades independientes que aparezcan en un archivo Napol.
 * P.ej:Declaracion de varibles globales, procedimientos, funciones...
 */
public class Programa {

    /**
     * Lista de sentencias del programa.
     */
    private LinkedList<Sentencia> sentencias;

    public Programa(LinkedList<Sentencia> sentencias, Sentencia proc) {
        this.sentencias = sentencias;
        this.sentencias.add(proc);
    }

    /**
     * Metodo encargado de la fase de identificacion de identificadores.
     */
    public void checkAmbitos() {
        TablaSimbolos ts = new TablaSimbolos();
        ts.abreBloque();
        for (Sentencia s : sentencias) {
            s.checkAmbitos(ts);
        }
        ts.cierraBloque();
    }

    /**
     * Metodo encargado de la comprobacion estatica de tipos.
     */
    public void checkTipos() {
        for (Sentencia s : sentencias) {
            s.checkTipos();
        }
    }

    /**
     * Calcula la rho usada en la generacion de codigo y los niveles de uso y
     * declaracion.
     */
    public void precalculo() {
        int x = 0;
        for (Sentencia s : sentencias) {
            x = s.precalculo(x, 0); //El nivel 0 es el "global"
        }
    }

    /**
     * Longitud pila evaluacion expresiones en el programa global.
     *
     * @return Longitud de la pila de evaluacion de expresiones.
     */
    private int longitudPilaEvaluacionExpresiones() {
        int max = 0;
        for (Sentencia s : sentencias) {
            if (!(s.tipo() == TipoS.FUND || s.tipo() == TipoS.PROCD)) {
                max = Math.max(max, s.longitudPilaEvaluacionExpresiones());
            }
        }
        return max;
    }

    /**
     * @return Numero de variables globales (declaradas fuera de cualquier
     * procedimiento o funcion).
     */
    private int declarVarLocales() {
        int numVarLocales = 0;
        for (Sentencia s : sentencias) {
            if (!(s.tipo() == TipoS.FUND || s.tipo() == TipoS.PROCD)) {
                numVarLocales += s.declarVarLocales();
            }
        }
        return numVarLocales;
    }

    /**
     * Genera el codigo del programa y lo devuelve.
     *
     * @return Codigo del programa
     */
    public ArrayList<String> generaCodigo() {
        ArrayList<String> codigo = new ArrayList<>();

        codigo.add("ssp " + Math.max(1, declarVarLocales()) + ";\n");
        codigo.add("sep " + longitudPilaEvaluacionExpresiones() + ";\n");
        for (Sentencia s : sentencias) {
            s.generaCodigo(codigo);
        }
        codigo.add("stp;\n");
        return codigo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Sentencia s : sentencias) {
            sb.append(s.toString());
            sb.append('\n');
        }
        return sb.toString();
    }
}
