package asint.ast.expresiones.operadores;

import asint.ast.expresiones.E;
import asint.ast.expresiones.TipoE;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.LinkedList;

public class Neg extends OpUnar {

    public Neg(E opnd1, int filaOp, int columnaOp) {
        super(opnd1, filaOp, columnaOp);
    }

    @Override
    public TipoE tipo() {
        return TipoE.NEG;
    }

    @Override
    public TipoDato checkTipos() {
        TipoDato tipoOp = opnd.checkTipos();

        //El operando debe ser de tipo bool
        if (tipoOp.getTipo() != Tipo.BOOL) {
            GestionErroresNapol.errorTipos(filaOp, columnaOp, Tipo.BOOL, tipoOp.getTipo());
        }

        if (tipoOp.getNumDimensiones() != 0) {
            GestionErroresNapol.errorNumDimensiones(filaOp, columnaOp);
        }

        return new TipoDato(Tipo.BOOL.getNombre(), new LinkedList<>());
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        opnd.generaCodigo(codigo);
        codigo.add("not;\n");
    }

    @Override
    public String toString() {
        return "Neg(" + opnd.toString() + ")";
    }
}