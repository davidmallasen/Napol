package asint.ast.expresiones.operadores;

import asint.ast.expresiones.E;
import asint.ast.expresiones.TipoE;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.LinkedList;

public class Or extends OpBin {

    public Or(E opnd1, E opnd2, int filaOp1, int columnaOp1, int filaOp2,
              int columnaOp2) {
        super(opnd1, opnd2, filaOp1, columnaOp1, filaOp2, columnaOp2);
    }

    @Override
    public TipoE tipo() {
        return TipoE.OR;
    }

    @Override
    public TipoDato checkTipos() {
        TipoDato tipoOp1 = opnd1.checkTipos();
        TipoDato tipoOp2 = opnd2.checkTipos();

        //Ambos operandos deben ser de tipo bool
        if (tipoOp1.getTipo() != Tipo.BOOL) {
            GestionErroresNapol.errorTipos(filaOp1, columnaOp1, Tipo.BOOL, tipoOp1.getTipo());
        }
        if (tipoOp2.getTipo() != Tipo.BOOL) {
            GestionErroresNapol.errorTipos(filaOp2, columnaOp2, Tipo.BOOL, tipoOp2.getTipo());
        }

        if (tipoOp1.getNumDimensiones() != 0) {
            GestionErroresNapol.errorNumDimensiones(filaOp1, columnaOp1);
        }
        if (tipoOp2.getNumDimensiones() != 0) {
            GestionErroresNapol.errorNumDimensiones(filaOp2, columnaOp2);
        }

        return new TipoDato(Tipo.BOOL.getNombre(), new LinkedList<>());
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        opnd1.generaCodigo(codigo);
        opnd2.generaCodigo(codigo);
        codigo.add("or;\n");
    }

    @Override
    public String toString() {
        return "Or(" + opnd1.toString() + ", " + opnd2.toString() + ")";
    }
}