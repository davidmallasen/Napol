package asint.ast.ambitos;

import asint.ast.sentencias.Sentencia;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Clase dedicada a los ambitos y declaraciones
 */
public class TablaSimbolos {

    /**
     * En esta pila se irán almacenando los bloques. No llevaran ninguna
     * clase de identificador del bloque, simplemente el ultimo sera el
     * bloque actual. Cada bloque de la pila sera una lista de punteros a los
     * identificadores correspondientes de la tabla (listas de declaraciones)
     * que hayan sido declarados en este bloque.
     */
    private Stack<LinkedList<LinkedList<Sentencia>>> pila;

    /**
     * En el se almacenaran los identificadores junto con listas de los
     * punteros a las declaraciones que se han hecho del identificador (la
     * ultima sera la que corresponda al bloque actual). Reducira el coste de
     * la busqueda del identificador.
     */
    private HashMap<String, LinkedList<Sentencia>> tabla;

    /**
     * Crea una tabla de simbolos vacia.
     */
    public TablaSimbolos() {
        pila = new Stack<>();
        tabla = new HashMap<>();
    }

    /**
     * Empieza un nuevo bloque.
     */
    public void abreBloque() {
        pila.push(new LinkedList<>());
    }

    /**
     * Elimina los vinculos del bloque en curso y el bloque.
     */
    public void cierraBloque() {
        for (LinkedList<Sentencia> b : pila.peek()) {
            b.removeFirst();
        }
        pila.pop();
    }

    /**
     * Añade el identificador id al bloque en curso y la posicion del arbol
     * donde esta su aparicion de definicion.
     *
     * @param id Identificador a insertar.
     * @param s  Declaracion del identificador a insertar.
     * @return True si el identificador esta duplicado. False si no.
     */
    public boolean insertaId(String id, Sentencia s) {
        boolean duplicado = false;

        //Miramos si tiene el identificador en el mismo bloque en el que estamos
        //El identificador ha sido declarado
        if (tabla.get(id) != null) {
            //Iteramos sobre las declaraciones de este bloque
            for (LinkedList<Sentencia> decl : pila.peek()) {
                //Comparamos si la declaracion del bloque es de id
                if (decl.peek().equals(tabla.get(id).peek())) {
                    duplicado = true;
                    break;
                }
            }
        }

        if (!duplicado) {
            if (tabla.get(id) != null) {
                tabla.get(id).addFirst(s);
            } else {
                LinkedList<Sentencia> aux = new LinkedList<>();
                aux.addFirst(s);
                tabla.put(id, aux);
            }
            pila.peek().addFirst(tabla.get(id));
        }
        return duplicado;
    }

    /**
     * Busca la aparicion de definicion mas interna para id.
     *
     * @param id Identificador a buscar.
     * @return Declaracion correspondiente del identificador o null si no ha
     * sido declarado.
     */
    public Sentencia buscaId(String id) {
        if (!tabla.containsKey(id) || tabla.get(id).size() == 0) {
            return null;
        } else {
            return tabla.get(id).peek();
        }
    }
}