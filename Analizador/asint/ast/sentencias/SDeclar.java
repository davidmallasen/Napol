package asint.ast.sentencias;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;

/**
 * Representa una sentencia de declaracion de variables.
 */
public class SDeclar implements Sentencia {

    /**
     * Tipo de la variable a declarar.
     */
    private TipoDato t;

    /**
     * Nombre de la variable a declarar.
     */
    private String id;

    /**
     * Fila en la que se encuentra.
     */
    private final int fila;

    /**
     * Columna en la que se encuentra.
     */
    private final int columna;

    /**
     * Valor de la rho de la declaracion. Usada para la generacion de codigo.
     */
    private int rho;

    /**
     * Nivel de la declaracion.
     */
    private int nivel;

    /**
     * Indica si la declaracion es un argumento de un procedimiento/funcion
     */
    private boolean esArgum = false;

    public SDeclar(TipoDato t, String id, int fila, int columna) {
        this.t = t;
        this.id = id;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        if (ts.insertaId(id, this)) {
            GestionErroresNapol.errorAmbitosIdentificadorDuplicado(fila, columna, id);
        }
    }

    @Override
    public void checkTipos() {
        //Vacio
    }

    @Override
    public int precalculo(int i, int nivel) {
        this.rho = i;
        this.nivel = nivel;
        t.calculaDj();
        return i + t.tamMemoria();
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return 0;
    }

    @Override
    public int declarVarLocales() {
        return t.tamMemoria();
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        //Vacio
    }

    public void setEsArgumTrue() {
        esArgum = true;
    }

    public boolean getEsArgum() {
        return esArgum;
    }

    public Tipo getTipo() {
        return t.getTipo();
    }

    public int getNumDimensiones() {
        return t.getNumDimensiones();
    }

    public TipoDato getTipoDato() {
        return t;
    }

    public int getRho() {
        return this.rho;
    }

    public ArrayList<Integer> getDj() {
        return t.getDj();
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public TipoS tipo() {
        return TipoS.DECLAR;
    }

    @Override
    public String toString() {
        return "SDeclar(" + t.toString() + ", " + id + ")";
    }
}
