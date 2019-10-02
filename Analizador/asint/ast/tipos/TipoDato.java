package asint.ast.tipos;

import asint.ast.expresiones.EEnt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Representa el tipo de un dato del programa. Contiene el tipo basico
 * junto con los tamaños de las dimensiones si es un array.
 *
 * @see Tipo
 */
public class TipoDato {

    /**
     * Tipo de dato
     */
    private Tipo tipo;

    /**
     * Lista de los tamaños de las dimensiones si es un array.
     * Lista vacia si no.
     */
    private LinkedList<EEnt> is;

    /**
     * Productos parciales de las dimensiones del array
     * d^j[i] = prod_{l=i+1}^{is.size()-1} is[l]
     */
    private ArrayList<Integer> dj;

    public TipoDato(String tipo, LinkedList<EEnt> is) {
        for (Tipo t : Tipo.values()) {
            if (tipo.equals(t.getNombre())) {
                this.tipo = t;
            }
        }
        this.is = is;
    }

    /**
     * Calcula los parametros d_j de un array. Llamado en las declaraciones
     * de arrays y usado para calcular el desplazamiento de un elemento
     * concreto del array.
     */
    public void calculaDj() {
        if (is.isEmpty()) {
            this.dj = new ArrayList<>();
            return;
        }

        int j = is.size() - 1;
        Integer[] aux = new Integer[is.size()];
        aux[j] = 1;
        j--;
        Iterator<EEnt> it = is.descendingIterator();
        while (it.hasNext() && j >= 0) {
            aux[j] = aux[j + 1] * it.next().valE();
            j--;
        }
        this.dj = new ArrayList<>(Arrays.asList(aux));
    }

    public int tamMemoria() {
        int x = 1;
        for (EEnt e : is) {
            x *= e.valE();
        }
        return x;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getNumDimensiones() {
        return is.size();
    }

    public ArrayList<Integer> getDj() {
        return dj;
    }

    public LinkedList<EEnt> getIs() {
        return is;
    }

    @Override
    public String toString() {
        return "TipoDato(" + tipo + ", " + is.toString() + ")";
    }
}
