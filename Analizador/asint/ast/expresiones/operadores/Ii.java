package asint.ast.expresiones.operadores;

import asint.ast.expresiones.E;
import asint.ast.expresiones.TipoE;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.LinkedList;

public class Ii extends OpBin {

    public Ii(E opnd1, E opnd2, int filaOp1, int columnaOp1, int filaOp2,
              int columnaOp2) {
        super(opnd1, opnd2, filaOp1, columnaOp1, filaOp2, columnaOp2);
    }

    @Override
    public TipoE tipo() {
        return TipoE.II;
    }

    @Override
    public TipoDato checkTipos() {
        TipoDato tipoOp1 = opnd1.checkTipos();
        TipoDato tipoOp2 = opnd2.checkTipos();

        //Ambos operandos deben ser del mismo tipo
        if (tipoOp1.getTipo() != tipoOp2.getTipo()) {
            GestionErroresNapol.errorTiposDistintos(filaOp2, columnaOp2);
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
        codigo.add("equ;\n");
    }

    @Override
    public String toString() {
        return "Ii(" + opnd1.toString() + ", " + opnd2.toString() + ")";
    }
}