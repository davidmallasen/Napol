package asint.ast.sentencias;

import asint.ast.BloqueSentencias;
import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;

/**
 * Representa una sentencia if.
 *
 * @see SIfElse
 */
public class SIf implements Sentencia {

    /**
     * Condicion booleana del if
     */
    protected E b;

    /**
     * Bloque de sentencias de la rama del then
     */
    protected BloqueSentencias sThen;

    /**
     * Fila en la que se encuentra la condicion
     */
    private final int filaCond;

    /**
     * Columna en la que se encuentra la condicion
     */
    private final int columnaCond;

    public SIf(E b, BloqueSentencias sThen, int filaCond, int columnaCond) {
        this.b = b;
        this.sThen = sThen;
        this.filaCond = filaCond;
        this.columnaCond = columnaCond;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        b.checkAmbitos(ts);
        ts.abreBloque();
        sThen.checkAmbitos(ts);
        ts.cierraBloque();
    }

    @Override
    public void checkTipos() {
        TipoDato tipoCond = b.checkTipos();
        if (tipoCond.getTipo() != Tipo.BOOL) {
            GestionErroresNapol.errorTipos(filaCond, columnaCond, Tipo.BOOL, tipoCond.getTipo());
        }
        if (tipoCond.getNumDimensiones() != 0) {
            GestionErroresNapol.errorNumDimensiones(filaCond, columnaCond);
        }
        sThen.checkTipos();
    }

    @Override
    public int precalculo(int i, int nivel) {
        b.precalculo(nivel);
        sThen.precalculo(i, nivel);
        return i;
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return Math.max(b.longitudPilaEvaluacionExpresiones(), sThen.longitudPilaEvaluacionExpresiones());
    }

    @Override
    public int declarVarLocales() {
        return sThen.getVariablesLocales();
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        b.generaCodigo(codigo);

        int numEtiquetaIf = codigo.size();
        codigo.add("fjp etPorDefinir;\n");

        sThen.generaCodigo(codigo);
        codigo.set(numEtiquetaIf, "fjp " + codigo.size() + ";\n");
    }

    @Override
    public TipoS tipo() {
        return TipoS.IF;
    }

    @Override
    public String toString() {
        return "SIf(" + b.toString() + ", " + sThen.toString() + ")";
    }
}
