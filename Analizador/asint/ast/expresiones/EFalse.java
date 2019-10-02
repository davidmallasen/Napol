package asint.ast.expresiones;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representa una expresion false.
 */
public class EFalse extends E {

    private boolean v;

    public EFalse(String v) {
        this.v = false;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        //Vacio
    }

    @Override
    public TipoDato checkTipos() {
        return new TipoDato(Tipo.BOOL.getNombre(), new LinkedList<>());
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
        codigo.add("ldc false;\n");
    }

    @Override
    public TipoE tipo() {
        return TipoE.FALSE;
    }

    @Override
    public boolean valF() {
        return v;
    }

    @Override
    public String toString() {
        return "EFalse(" + v + ")";
    }
}