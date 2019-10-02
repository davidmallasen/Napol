package asint.ast.sentencias;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;
import asint.ast.expresiones.EEnt;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Representa una sentencia de asignacion de una expresion a una variable.
 */
public class SAsig implements Sentencia {

    /**
     * Identificador de la variable.
     */
    private String id;

    /**
     * Indices del array.
     */
    private LinkedList<E> is;

    /**
     * Expresion a asignar.
     */
    private E e;

    /**
     * Puntero a la declaracion del id.
     */
    private SDeclar declar;

    /**
     * Fila en la que se encuentra la variable de la izquierda.
     */
    private final int filaIz;

    /**
     * Columna en la que se encuentra la variable de la izquierda.
     */
    private final int columnaIz;

    /**
     * Fila en la que se encuentra el igual.
     */
    private final int filaIgual;

    /**
     * Columna en la que se encuentra el igual.
     */
    private final int columnaIgual;

    /**
     * Nivel del uso.
     */
    private int nivel;

    public SAsig(String id, LinkedList<E> is, E e, int filaIz, int columnaIz,
                 int filaIgual, int columnaIgual) {
        this.id = id;
        this.is = is;
        this.e = e;
        this.filaIz = filaIz;
        this.columnaIz = columnaIz;
        this.filaIgual = filaIgual;
        this.columnaIgual = columnaIgual;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        Sentencia aux = ts.buscaId(id);
        if (aux == null || (aux.tipo() != TipoS.DECLAR)) {
            GestionErroresNapol.errorAmbitosNoDeclaracion(filaIz, columnaIz, id);
            return;
        }
        declar = (SDeclar) aux;
        for (E i : is) {
            i.checkAmbitos(ts);
        }
        e.checkAmbitos(ts);
    }

    @Override
    public void checkTipos() {
        Tipo tipoDeclar = declar.getTipo();
        TipoDato tipoDatoExpr = e.checkTipos();
        // Se comprueba que sea el mismo tipo basico
        if (tipoDeclar != tipoDatoExpr.getTipo()) {
            GestionErroresNapol.errorTipos(filaIgual, columnaIgual, tipoDeclar,
                    tipoDatoExpr.getTipo());
        }
        //Se comprueba que el numero de dimensiones de la izquierda sean las
        // correctas
        if (is.size() != declar.getNumDimensiones()) {
            GestionErroresNapol.errorNumDimensiones(filaIz, columnaIz);
        }
        // Se comprueba que estemos asignando un elemento del array (no se
        // permiten asignaciones multiples)
        if (tipoDatoExpr.getNumDimensiones() != 0) {
            GestionErroresNapol.errorAsignacionArray(filaIz, columnaIz);
        }
    }

    @Override
    public int precalculo(int i, int nivel) {
        this.nivel = nivel;
        for (E ind : is)
            ind.precalculo(nivel);
        e.precalculo(nivel);
        return i;
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        int max = 0;
        int i = 1;
        for (E ee : is) {
            max = Math.max(max, ee.longitudPilaEvaluacionExpresiones() + i);
            i++;
        }
        max = Math.max(max, 1 + e.longitudPilaEvaluacionExpresiones());
        return max;
    }

    @Override
    public int declarVarLocales() {
        return 0;
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        if (nivel == 0) {
            codigo.add("ldc " + declar.getRho() + ";\n");
        } else if (difNiveles() == 0 && declar.getEsArgum() && !is.isEmpty()) {
            //Unico caso en el que el argumento es por referencia
            codigo.add("lod 0 " + declar.getRho() + ";\n");
        } else {
            codigo.add("lda " + difNiveles() + " " + declar.getRho() + ";\n");
        }

        if (!is.isEmpty()) { //Si es un array
            ArrayList<Integer> dj = declar.getDj();
            LinkedList<EEnt> dim = declar.getTipoDato().getIs();
            Iterator<EEnt> it = dim.iterator();
            int i = 0;
            for (E ind : is) {
                ind.generaCodigo(codigo);
                codigo.add("chk 0 " + it.next().valE() + ";\n");
                codigo.add("ixa " + dj.get(i) + ";\n");
                i++;
            }
            codigo.add("dec 0;\n");    //Nuestros limites inferiores son siempre 0
        }

        e.generaCodigo(codigo);
        codigo.add("sto;\n");
    }

    private int difNiveles() {
        return nivel - declar.getNivel();
    }

    public Tipo getTipo() {
        return declar.getTipo();
    }

    public TipoDato getTipoDato() {
        return declar.getTipoDato();
    }

    @Override
    public TipoS tipo() {
        return TipoS.ASIG;
    }

    @Override
    public String toString() {
        if (is != null) {
            return "SAsig(" + id + ", " + is.toString() + ", " + e.toString()
                    + ")";
        } else {
            return "SAsig(" + id + ", " + e.toString() + ")";
        }
    }
}
