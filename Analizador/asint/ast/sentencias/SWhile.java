package asint.ast.sentencias;

import asint.ast.BloqueSentencias;
import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;

/**
 * Representa una sentencia while.
 */
public class SWhile implements Sentencia {

    /**
     * Condicion booleana de salida del while.
     */
    private E b;

    /**
     * Bloque de sentencias del cuerpo del while.
     */
    private BloqueSentencias s;

    /**
     * Fila en la que se encuentra la condicion
     */
    private final int filaCond;

    /**
     * Columna en la que se encuentra la condicion
     */
    private final int columnaCond;

    public SWhile(E b, BloqueSentencias s, int filaCond, int columnaCond) {
        this.b = b;
        this.s = s;
        this.filaCond = filaCond;
        this.columnaCond = columnaCond;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        b.checkAmbitos(ts);
        ts.abreBloque();
        s.checkAmbitos(ts);
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
        s.checkTipos();
    }

    @Override
    public int precalculo(int i, int nivel) {
        b.precalculo(nivel);
        s.precalculo(i, nivel);
        return i;
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return Math.max(b.longitudPilaEvaluacionExpresiones(), s.longitudPilaEvaluacionExpresiones());
    }

    @Override
    public int declarVarLocales() {
        return s.getVariablesLocales();
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        int numEtiquetaInicio = codigo.size();

        b.generaCodigo(codigo);
        int numEtiquetaFin = codigo.size();

        codigo.add("fjp etPorDefinir;\n");

        s.generaCodigo(codigo);

        codigo.add("ujp " + numEtiquetaInicio + ";\n");
        codigo.set(numEtiquetaFin, "fjp " + codigo.size() + ";\n");
    }

    @Override
    public TipoS tipo() {
        return TipoS.WHILE;
    }

    @Override
    public String toString() {
        return "SWhile(" + b.toString() + ", " + s.toString() + ")";
    }
}
