package asint.ast.expresiones;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.sentencias.SDeclar;
import asint.ast.sentencias.SFund;
import asint.ast.sentencias.Sentencia;
import asint.ast.sentencias.TipoS;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Representa una expresion de tipo identificador, es decir, una variable.
 */
public class EId extends E {

    /**
     * Identificador de la variable
     */
    private String id;

    /**
     * Lista de indices, si es una posicion de un array
     */
    private LinkedList<E> is;

    /**
     * Puntero a la declaracion de la variable
     */
    private SDeclar declar;

    /**
     * Fila en la que se encuentra.
     */
    private final int fila;

    /**
     * Columna en la que se encuentra.
     */
    private final int columna;

    /**
     * Nivel de uso.
     */
    private int nivel;

    public EId(String id, LinkedList<E> is, int fila, int columna) {
        this.id = id;
        this.is = is;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        Sentencia aux = ts.buscaId(id);
        if (aux == null || (aux.tipo() != TipoS.DECLAR)) {
            GestionErroresNapol.errorAmbitosNoDeclaracion(fila, columna, id);
            return;
        }
        declar = (SDeclar) aux;
        for (E i : is) {
            i.checkAmbitos(ts);
        }
    }

    @Override
    public TipoDato checkTipos() {
        //Si pasamos un elemento del array
        if (getNumDimensionesDeclar() == getNumDimensiones()) {
            return new TipoDato(declar.getTipo().getNombre(), new LinkedList<>());
        } //Si pasamos el array completo
        else if (getNumDimensionesDeclar() > 0 && getNumDimensiones() == 0) {
            return declar.getTipoDato();
        } else { //No permitimos pasar "trozos" de arrays pues tenemos arrays
            // multidimensionales en vez de arrays de arrays
            GestionErroresNapol.errorNumDimensiones(fila, columna);
        }
        return declar.getTipoDato();
    }

    @Override
    public void precalculo(int nivel) {
        this.nivel = nivel;
        for (E e : is) {
            e.precalculo(nivel);
        }
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        int max = 0;
        int i = 0;
        for (E e : is) {
            max = Math.max(max, e.longitudPilaEvaluacionExpresiones() + i);
            i++;
        }
        return 1 + max;
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        if (nivel == 0) {
            codigo.add("ldc " + declar.getRho() + ";\n");
        } else if (difNiveles() == 0 && declar.getEsArgum() && is.size() > 0) {
            //Unico caso en el que el argumento es por referencia
            codigo.add("lod 0 " + declar.getRho() + ";\n");
        } else {
            codigo.add("lda " + difNiveles() + " " + declar.getRho() + ";\n");
        }

        if (is.size() > 0) { //Si es un array
            ArrayList<Integer> dj = declar.getDj();
            LinkedList<EEnt> dim = declar.getTipoDato().getIs();
            Iterator<EEnt> it = dim.iterator();
            int i = 0;
            for (E ind : is) {
                ind.generaCodigo(codigo);
                codigo.add("chk 0 " + it.next().valE() + ";\n");
                codigo.add("ixa " + dj.get(i) + ";\n");
                i++;
            }
            codigo.add("dec 0;\n");    //Nuestros limites inferiores son siempre 0
        }

        //Siempre es codeR salvo que sea un array (pasado por referencia) en
        // la llamada a un procedimiento/funcion (en cuyo caso queremos pasar
        // la direccion del array)
        if (esArgum && declar.getNumDimensiones() > 0) {
            if (declar.getEsArgum()) {
                codigo.add("ind;\n");
            }
        } else {
            codigo.add("ind;\n");
        }
    }

    private int difNiveles() {
        return nivel - declar.getNivel();
    }

    @Override
    public TipoE tipo() {
        return TipoE.ID;
    }

    @Override
    public String valI() {
        return id;
    }

    @Override
    public int getNumDimensiones() {
        return is.size();
    }

    @Override
    public int getNumDimensionesDeclar() {
        return declar.getNumDimensiones();
    }

    public LinkedList<EEnt> getDimensionesDeclar() {
        return declar.getTipoDato().getIs();
    }

    @Override
    public String toString() {
        return "EId(" + id + ")";
    }
}