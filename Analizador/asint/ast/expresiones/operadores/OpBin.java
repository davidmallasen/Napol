package asint.ast.expresiones.operadores;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;

public abstract class OpBin extends E {

    /**
     * Primer operando
     */
    protected E opnd1;

    /**
     * Segundo operando
     */
    protected E opnd2;

    /**
     * Fila del primer operando
     */
    protected final int filaOp1;

    /**
     * Columna del primer operando
     */
    protected final int columnaOp1;

    /**
     * Fila del segundo operando
     */
    protected final int filaOp2;

    /**
     * Columna del segundo operando
     */
    protected final int columnaOp2;

    public OpBin(E opnd1, E opnd2, int filaOp1, int columnaOp1, int filaOp2,
                 int columnaOp2) {
        this.opnd1 = opnd1;
        this.opnd2 = opnd2;
        this.filaOp1 = filaOp1;
        this.columnaOp1 = columnaOp1;
        this.filaOp2 = filaOp2;
        this.columnaOp2 = columnaOp2;
    }

    @Override
    public void checkAmbitos(TablaSimbolos a) {
        opnd1.checkAmbitos(a);
        opnd2.checkAmbitos(a);
    }

    @Override
    public void precalculo(int nivel) {
        opnd1.precalculo(nivel);
        opnd2.precalculo(nivel);
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return Math.max(opnd1.longitudPilaEvaluacionExpresiones(),
                opnd2.longitudPilaEvaluacionExpresiones() + 1);
    }

    @Override
    public E opnd1() {
        return opnd1;
    }

    @Override
    public E opnd2() {
        return opnd2;
    }
}