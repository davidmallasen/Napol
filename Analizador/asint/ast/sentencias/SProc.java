package asint.ast.sentencias;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;
import asint.ast.expresiones.EEnt;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Representa una sentencia de llamada a un procedimiento.
 */
public class SProc implements Sentencia {

    /**
     * Nombre del procedimiento.
     */
    private String nombre;

    /**
     * Expresiones de los argumentos del procedimiento.
     */
    private LinkedList<E> es;

    /**
     * Puntero a la declaracion del id.
     */
    private SProcd declar;

    /**
     * Fila en la que se encuentra.
     */
    private final int fila;

    /**
     * Columna en la que se encuentra.
     */
    private final int columna;

    /**
     * Nivel de la declaracion.
     */
    private int nivel;

    public SProc(String nombre, LinkedList<E> es, int fila, int columna) {
        this.nombre = nombre;
        this.es = es;
        for (E e : es) {
            e.setEsArgumTrue();
        }
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        Sentencia aux = ts.buscaId(nombre);
        if (aux == null || (aux.tipo() != TipoS.PROCD)) {
            GestionErroresNapol.errorAmbitosNoDeclaracion(fila, columna, nombre);
            return;
        }
        declar = (SProcd) aux;
        for (E e : es) {
            e.checkAmbitos(ts);
        }
    }

    @Override
    public void checkTipos() {
        LinkedList<TipoDato> tiposDatoDeclarFun = declar.getTiposArgums();

        //Comprobamos si hay el mismo numero de argumentos
        if (tiposDatoDeclarFun.size() != es.size()) {
            GestionErroresNapol.errorNumArgumentos(fila, columna,
                    tiposDatoDeclarFun.size(), es.size());
        } else {
            //Iteramos sobre cada uno de los argumentos, comprobando que sus
            // tipos son correctos
            Iterator<TipoDato> itTiposDatoDeclarFun = tiposDatoDeclarFun.iterator();
            for (E argumLlamada : es) {
                checkTiposArgumento(argumLlamada, itTiposDatoDeclarFun.next());
            }
        }
    }

    /**
     * Hace la comprobacion de tipos de un argumento de la llamada a una funcion
     *
     * @param argumLlamada      Expresion del argumento de llamada
     * @param tipoDatoDeclarFun Tipo de dato del parametro correspondiente de
     *                          la declaracion de la funcion
     */
    private void checkTiposArgumento(E argumLlamada, TipoDato tipoDatoDeclarFun) {
        TipoDato tipoDatoArgumLlamada = argumLlamada.checkTipos();

        //Comprobamos que el tipo del argumento de la llamada sea igual
        // al de la declaracion de la funcion
        if (tipoDatoArgumLlamada.getTipo() != tipoDatoDeclarFun.getTipo()) {
            GestionErroresNapol.errorTipos(fila, columna,
                    tipoDatoDeclarFun.getTipo(), tipoDatoArgumLlamada.getTipo());
            return;
        }

        //Comprobamos que el numero de dimensiones del argumento de la llamada
        // y el del parametro de la declaracion de la funcion sea el mismo
        if (tipoDatoDeclarFun.getNumDimensiones() != tipoDatoArgumLlamada.getNumDimensiones()) {
            GestionErroresNapol.errorTiposDimensiones(fila, columna,
                    tipoDatoDeclarFun.getTipo(), tipoDatoArgumLlamada.getTipo(),
                    tipoDatoDeclarFun.getNumDimensiones(), tipoDatoArgumLlamada.getNumDimensiones());
            return;
        }

        //Comprobamos que el tamanyo de cada una de las dimensiones coincida
        Iterator<EEnt> tamDimArgumLlamada = tipoDatoArgumLlamada.getIs().iterator();
        Iterator<EEnt> tamDimDeclarFun = tipoDatoDeclarFun.getIs().iterator();
        while (tamDimArgumLlamada.hasNext()) {
            if (tamDimArgumLlamada.next().valE().intValue() != tamDimDeclarFun.next().valE().intValue()) {
                GestionErroresNapol.errorTamDimensiones(fila, columna);
                break;
            }
        }
    }

    @Override
    public int precalculo(int i, int nivel) {
        this.nivel = nivel;
        for (E e : es) {
            e.precalculo(nivel);
        }
        return i;
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        int max = 0;
        int i = 0;
        for (E param : es) {
            max = Math.max(max, param.longitudPilaEvaluacionExpresiones() + i);
            i++;
        }
        return max;
    }

    @Override
    public int declarVarLocales() {
        return 0;
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        codigo.add("mst " + difNiveles() + ";\n");
        for (E e : es) {
            e.generaCodigo(codigo);
        }
        codigo.add("cup " + declar.getNumArgums() + " " + declar.getL() + ";\n");
    }

    private int difNiveles() {
        return nivel - declar.getNivel();
    }

    @Override
    public TipoS tipo() {
        return TipoS.PROC;
    }

    @Override
    public String toString() {
        return "SProcArgs(" + nombre + ", " + es.toString() + ")";
    }
}
