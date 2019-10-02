package asint.ast.sentencias;

import asint.ast.BloqueSentencias;
import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;

/**
 * Representa una sentencia for cuya variable iteradora se indica al inicio sin
 * asignar.
 */
public class SForId extends ASFor {

    /**
     * Variable iteradora.
     */
    private String id;

    /**
     * Puntero a la declaracion del identificador de la variable iteradora.
     */
    private SDeclar declar;

    /**
     * Fila en la que se encuentra.
     */
    private final int filaIdIni;

    /**
     * Columna en la que se encuentra.
     */
    private final int columnaIdIni;

    public SForId(String id, E b, SAsig asigFin, BloqueSentencias s, int
            filaIdIni, int columnaIdIni, int filaCond, int columnaCond, int
                          filaAsigFin, int columnaAsigFin) {
        super(b, s, asigFin, filaCond, columnaCond, filaAsigFin, columnaAsigFin);
        this.id = id;
        this.filaIdIni = filaIdIni;
        this.columnaIdIni = columnaIdIni;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        ts.abreBloque();
        Sentencia aux = ts.buscaId(id);
        if (aux == null || (aux.tipo() != TipoS.DECLAR)) {
            GestionErroresNapol.errorAmbitosNoDeclaracion(filaIdIni, columnaIdIni, id);
            return;
        }
        declar = (SDeclar) aux;
        b.checkAmbitos(ts);
        asigFin.checkAmbitos(ts);
        s.checkAmbitos(ts);
        ts.cierraBloque();
    }

    @Override
    public void checkTipos() {
        TipoDato tipoIdIni = declar.getTipoDato();
        if (tipoIdIni.getTipo() != Tipo.INT) {
            GestionErroresNapol.errorTipos(filaIdIni, columnaIdIni,
                    Tipo.INT, tipoIdIni.getTipo());
        }
        if (tipoIdIni.getNumDimensiones() != 0) {
            GestionErroresNapol.errorNumDimensiones(filaIdIni, columnaIdIni);
        }

        super.checkTipos();
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        int max = Math.max(b.longitudPilaEvaluacionExpresiones(), asigFin.longitudPilaEvaluacionExpresiones());
        max = Math.max(max, s.longitudPilaEvaluacionExpresiones());
        return max;
    }

    @Override
    public int declarVarLocales() {
        return s.getVariablesLocales();
    }

    @Override
    public int precalculo(int i, int nivel) {
        b.precalculo(nivel);
        i = s.precalculo(i, nivel);
        i = asigFin.precalculo(i, nivel);
        return i;
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
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
        return TipoS.FORID;
    }

    @Override
    public String toString() {
        return "SForId(" + id + ", " + b.toString() + ", " + asigFin.toString()
                + ", " + s.toString() + ")";
    }
}
