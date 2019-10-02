package asint.ast.expresiones;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representa una expresion true.
 */
public class ETrue extends E {

    private boolean v;

    public ETrue(String v) {
        this.v = true;
    }

    @Override
    public void checkAmbitos(TablaSimbolos a) {
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
        codigo.add("ldc true;\n");
    }

    @Override
    public TipoE tipo() {
        return TipoE.TRUE;
    }

    @Override
    public boolean valT() {
        return v;
    }

    @Override
    public String toString() {
        return "ETrue(" + v + ")";
    }
}