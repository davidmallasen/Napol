package asint.ast.sentencias;

import asint.ast.BloqueSentencias;
import asint.ast.expresiones.E;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

/**
 * Representa una sentencia abstracta for.
 *
 * @see SForAsig
 * @see SForId
 */
public abstract class ASFor implements Sentencia {

    /**
     * Condicion booleana de salida.
     */
    protected E b;

    /**
     * Bloque de sentencias del cuerpo.
     */
    protected BloqueSentencias s;

    /**
     * Asignacion final para modificar la variable iteradora.
     */
    protected SAsig asigFin;

    private int filaCond;

    private int columnaCond;

    private int filaAsigFin;

    private int columnaAsigFin;

    public ASFor(E b, BloqueSentencias s, SAsig asigFin, int filaCond, int
            columnaCond, int filaAsigFin, int columnaAsigFin) {
        this.b = b;
        this.s = s;
        this.asigFin = asigFin;
        this.filaCond = filaCond;
        this.columnaCond = columnaCond;
        this.filaAsigFin = filaAsigFin;
        this.columnaAsigFin = columnaAsigFin;
    }

    @Override
    public void checkTipos() {
        TipoDato tipoCond = b.checkTipos();
        if (tipoCond.getTipo() != Tipo.BOOL) {
            GestionErroresNapol.errorTipos(filaCond, columnaCond, Tipo.BOOL,
                    tipoCond.getTipo());
        }
        if (tipoCond.getNumDimensiones() != 0) {
            GestionErroresNapol.errorNumDimensiones(filaCond, columnaCond);
        }

        asigFin.checkTipos();
        TipoDato tipoAsigFin = asigFin.getTipoDato();
        if (tipoAsigFin.getTipo() != Tipo.INT) {
            GestionErroresNapol.errorTipos(filaAsigFin, columnaAsigFin, Tipo
                    .INT, tipoAsigFin.getTipo());
        }

        s.checkTipos();
    }
}
