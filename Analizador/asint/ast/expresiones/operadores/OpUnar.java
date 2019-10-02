package asint.ast.expresiones.operadores;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;

public abstract class OpUnar extends E {

    /**
     * Operando
     */
    protected E opnd;

    /**
     * Fila del operando
     */
    protected final int filaOp;

    /**
     * Columna del operando
     */
    protected final int columnaOp;

    public OpUnar(E opnd, int filaOp, int columnaOp) {
        this.opnd = opnd;
        this.filaOp = filaOp;
        this.columnaOp = columnaOp;
    }

    @Override
    public void checkAmbitos(TablaSimbolos a) {
        opnd.checkAmbitos(a);
    }

    @Override
    public void precalculo(int nivel) {
        opnd.precalculo(nivel);
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return opnd.longitudPilaEvaluacionExpresiones();
    }

    @Override
    public E opnd1() {
        return opnd;
    }
}