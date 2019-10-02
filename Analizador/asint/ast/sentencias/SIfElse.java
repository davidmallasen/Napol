package asint.ast.sentencias;

import asint.ast.BloqueSentencias;
import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;

import java.util.ArrayList;

/**
 * Representa una sentencia if-else.
 *
 * @see SIf
 */
public class SIfElse extends SIf {

    /**
     * Bloque de sentencias de la rama del else.
     */
    private BloqueSentencias sElse;

    public SIfElse(E b, BloqueSentencias sThen, BloqueSentencias sElse, int filaCond, int columnaCond) {
        super(b, sThen, filaCond, columnaCond);
        this.sElse = sElse;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        b.checkAmbitos(ts);
        ts.abreBloque();
        sThen.checkAmbitos(ts);
        ts.cierraBloque();
        ts.abreBloque();
        sElse.checkAmbitos(ts);
        ts.cierraBloque();
    }

    @Override
    public void checkTipos() {
        super.checkTipos();
        sElse.checkTipos();
    }

    @Override
    public int precalculo(int i, int nivel) {
        super.precalculo(i, nivel);
        sElse.precalculo(i, nivel);
        return i;
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return Math.max(super.longitudPilaEvaluacionExpresiones(), sElse.longitudPilaEvaluacionExpresiones());
    }

    @Override
    public int declarVarLocales() {
        return sThen.getVariablesLocales() + sElse.getVariablesLocales();
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        b.generaCodigo(codigo);

        int numEtiquetaIf = codigo.size();
        codigo.add("fjp etPorDefinir;\n");

        sThen.generaCodigo(codigo);
        int numEtiquetaElse = codigo.size();

        codigo.add("ujp etPorDefinir;\n");
        codigo.set(numEtiquetaIf, "fjp " + codigo.size() + ";\n");

        sElse.generaCodigo(codigo);

        codigo.set(numEtiquetaElse, "ujp " + codigo.size() + ";\n");

    }

    @Override
    public TipoS tipo() {
        return TipoS.IFELSE;
    }

    @Override
    public String toString() {
        return "SIfElse(" + b.toString() + ", " + sThen.toString() + ", " +
                sElse.toString() + ")";
    }
}
