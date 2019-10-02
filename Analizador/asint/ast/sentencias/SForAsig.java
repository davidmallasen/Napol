package asint.ast.sentencias;

import asint.ast.BloqueSentencias;
import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;

/**
 * Representa una sentencia for cuya variable iteradora se asigna al principio
 * con un valor.
 */
public class SForAsig extends ASFor {

    /**
     * Asignacion inicial.
     */
    private SAsig asigIni;

    /**
     * Fila donde se encuentra la asignacion inicial
     */
    private final int filaAsigIni;

    /**
     * Columna donde se encuentra la asignacion inicial
     */
    private final int columnaAsigIni;

    public SForAsig(SAsig asigIni, E b, SAsig asigFin, BloqueSentencias s,
                    int filaAsigIni, int columnaAsigIni, int filaCond, int columnaCond,
                    int filaAsigFin, int columnaAsigFin) {
        super(b, s, asigFin, filaCond, columnaCond, filaAsigFin,
                columnaAsigFin);
        this.asigIni = asigIni;
        this.filaAsigIni = filaAsigIni;
        this.columnaAsigIni = columnaAsigIni;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        asigIni.checkAmbitos(ts);
        b.checkAmbitos(ts);
        asigFin.checkAmbitos(ts);
        ts.abreBloque();
        s.checkAmbitos(ts);
        ts.cierraBloque();
    }

    @Override
    public void checkTipos() {
        asigIni.checkTipos();
        TipoDato tipoAsigIni = asigIni.getTipoDato();
        if (tipoAsigIni.getTipo() != Tipo.INT) {
            GestionErroresNapol.errorTipos(filaAsigIni, columnaAsigIni,
                    Tipo.INT, tipoAsigIni.getTipo());
        }

        super.checkTipos();
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        int max = Math.max(asigIni.longitudPilaEvaluacionExpresiones(), asigFin.longitudPilaEvaluacionExpresiones());
        max = Math.max(max, b.longitudPilaEvaluacionExpresiones());
        max = Math.max(max, s.longitudPilaEvaluacionExpresiones());
        return max;
    }

    @Override
    public int declarVarLocales() {
        return s.getVariablesLocales();
    }

    @Override
    public int precalculo(int i, int nivel) {
        i = asigIni.precalculo(i, nivel);
        b.precalculo(nivel);
        i = s.precalculo(i, nivel);
        i = asigFin.precalculo(i, nivel);
        return i;
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        asigIni.generaCodigo(codigo);

        int numEtiquetaInicio = codigo.size();

        b.generaCodigo(codigo);

        int numEtiquetaFin = codigo.size();

        codigo.add("fjp etPorDefinir;\n");

        s.generaCodigo(codigo);

        asigFin.generaCodigo(codigo);

        codigo.add("ujp " + numEtiquetaInicio + ";\n");
        codigo.set(numEtiquetaFin, "fjp " + codigo.size() + ";\n");
    }

    @Override
    public TipoS tipo() {
        return TipoS.FORASIG;
    }

    @Override
    public String toString() {
        return "SForAsig(" + asigIni.toString() + ", " + b.toString() + ", "
                + asigFin.toString() + ", " + s.toString() + ")";
    }
}
