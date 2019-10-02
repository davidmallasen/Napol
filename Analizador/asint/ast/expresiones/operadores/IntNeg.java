package asint.ast.expresiones.operadores;

import asint.ast.expresiones.E;
import asint.ast.expresiones.TipoE;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.LinkedList;

public class IntNeg extends OpUnar {

    public IntNeg(E opnd1, int filaOp, int columnaOp) {
        super(opnd1, filaOp, columnaOp);
    }

    @Override
    public TipoE tipo() {
        return TipoE.INTNEG;
    }

    @Override
    public TipoDato checkTipos() {
        TipoDato tipoOp = opnd.checkTipos();

        //El operando debe ser de tipo entero
        if (tipoOp.getTipo() != Tipo.INT) {
            GestionErroresNapol.errorTipos(filaOp, columnaOp, Tipo.INT, tipoOp.getTipo());
        }

        if (tipoOp.getNumDimensiones() != 0) {
            GestionErroresNapol.errorNumDimensiones(filaOp, columnaOp);
        }

        return new TipoDato(Tipo.INT.getNombre(), new LinkedList<>());
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        opnd.generaCodigo(codigo);
        codigo.add("neg;\n");
    }

    @Override
    public String toString() {
        return "IntNeg(" + opnd.toString() + ")";
    }
}
