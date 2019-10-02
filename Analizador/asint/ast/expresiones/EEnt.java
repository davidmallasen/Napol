package asint.ast.expresiones;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representa una expresion entera.
 */
public class EEnt extends E {

    /**
     * Valor entero de la expresion.
     */
    private Integer v;

    public EEnt(String v) {
        this.v = new Integer(v);
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        //Vacio
    }

    @Override
    public TipoDato checkTipos() {
        return new TipoDato(Tipo.INT.getNombre(), new LinkedList<>());
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return 1;
    }

    @Override
    public void precalculo(int nivel) {
        //Vacio
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        codigo.add("ldc " + v + ";\n");
    }

    @Override
    public TipoE tipo() {
        return TipoE.ENT;
    }

    @Override
    public Integer valE() {
        return v;
    }

    @Override
    public String toString() {
        return "EEnt(" + v.toString() + ")";
    }
}