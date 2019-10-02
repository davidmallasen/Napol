package asint.ast.sentencias;

import asint.ast.BloqueSentencias;
import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.EEnt;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representa una sentencia de declaracion de un procedimiento.
 */
public class SProcd implements Sentencia {

    /**
     * Nombre del procedimiento.
     */
    private String nombre;

    /**
     * Lista de argumentos del procedimiento.
     */
    private LinkedList<SDeclar> argums;

    /**
     * Bloque de sentencias del cuerpo del procedimiento.
     */
    private BloqueSentencias s;

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

    /**
     * Direccion de comienzo de codigo del procedimiento.
     */
    private int l;

    /**
     * Declaracion de un procedimiento con argumentos.
     *
     * @param nombre  Nombre del procedimiento.
     * @param argums  Lista de declaraciones de los argumentos.
     * @param s       Bloque de sentencias del cuerpo del procedimiento.
     * @param fila    Fila en la que se encuentra el procedimiento.
     * @param columna Columna en la que se encuentra el procedimiento.
     */
    public SProcd(String nombre, LinkedList<SDeclar> argums,
                  BloqueSentencias s, int fila, int columna) {
        this.nombre = nombre;
        this.argums = argums;
        for (SDeclar sd : argums) {
            sd.setEsArgumTrue();
        }
        this.s = s;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        if (ts.insertaId(nombre, this)) {
            GestionErroresNapol.errorAmbitosIdentificadorDuplicado(fila, columna, nombre);
        }
        ts.abreBloque();
        for (SDeclar argum : argums) {
            argum.checkAmbitos(ts);
        }
        s.checkAmbitos(ts);
        ts.cierraBloque();
    }

    @Override
    public void checkTipos() {
        s.checkTipos();
    }

    @Override
    public int precalculo(int i, int nivel) {
        int x = 5;
        for (SDeclar s : argums) {
            s.precalculo(x, nivel + 1);
            x++;
        }
        this.nivel = nivel;
        s.precalculo(x, nivel + 1);
        return i;
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return s.longitudPilaEvaluacionExpresiones();
    }

    @Override
    public int declarVarLocales() {
        //No es necesario que devuelva nada ya que no podemos anidar
        // funciones/procedimientos y este metodo se usa para
        // calcular esto en anidaciones.
        return 0;
    }

    @Override
    public void generaCodigo(ArrayList<String> codigo) {
        int salto = codigo.size();
        codigo.add("ujp porDeterminar" + ";\n");
        this.l = codigo.size();

        codigo.add("ssp " + (5 + argums.size() + s.getVariablesLocales()) + ";\n");
        codigo.add("sep " + this.longitudPilaEvaluacionExpresiones() + ";\n");

        //No hay procedimientos anidados
        codigo.add("ujp " + (codigo.size() + 1) + ";\n");
        s.generaCodigo(codigo);
        codigo.add("retp;\n");
        codigo.set(salto, "ujp " + codigo.size() + ";\n");
    }

    public LinkedList<TipoDato> getTiposArgums() {
        LinkedList<TipoDato> tipos = new LinkedList<>();
        for (SDeclar s : argums) {
            tipos.add(s.getTipoDato());
        }
        return tipos;
    }

    public LinkedList<LinkedList<EEnt>> getDimensionesArgumentos() {
        LinkedList<LinkedList<EEnt>> tipos = new LinkedList<>();
        for (SDeclar s : argums) {
            tipos.add(s.getTipoDato().getIs());
        }
        return tipos;
    }

    public int getNivel() {
        return nivel;
    }

    public int getNumArgums() {
        return argums.size();
    }

    public int getL() {
        return l;
    }

    @Override
    public TipoS tipo() {
        return TipoS.PROCD;
    }

    @Override
    public String toString() {
        return "SProcdArg(" + nombre + ", " + argums.toString() + ", " + s
                .toString() + ")";
    }

}
