package asint.ast.sentencias;

import asint.ast.BloqueSentencias;
import asint.ast.ambitos.TablaSimbolos;
import asint.ast.expresiones.E;
import asint.ast.expresiones.EEnt;
import asint.ast.tipos.Tipo;
import asint.ast.tipos.TipoDato;
import errors.GestionErroresNapol;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Representa una sentencia de declaracion de una funcion.
 */
public class SFund implements Sentencia {

    /**
     * Nombre de la funcion a declarar.
     */
    private String nombre;

    /**
     * Lista de declaraciones de los argumentos.
     */
    private LinkedList<SDeclar> argums;

    /**
     * Tipo de retorno de la funcion.
     */
    private TipoDato tr;

    /**
     * Bloque de sentencias del cuerpo de la funcion.
     */
    private BloqueSentencias s;

    /**
     * Valor devuelto en el return de la funcion.
     */
    private E e;

    /**
     * Fila en la que se encuentra el nombre
     */
    private final int filaNombre;

    /**
     * Columna en la que se encuentra el nombre
     */
    private final int columnaNombre;

    /**
     * Fila en la que se encuentra el return
     */
    private final int filaReturn;

    /**
     * Columna en la que se encuentra el return
     */
    private final int columnaReturn;

    /**
     * Nivel de la declaracion.
     */
    private int nivel;

    /**
     * Direccion de comienzo de codigo de la funcion.
     */
    private int l;

    /**
     * Declaracion de una funcion con argumentos.
     *
     * @param nombre        Nombre de la funcion.
     * @param argums        Lista de declaraciones de los argumentos.
     * @param tr            Tipo de retorno de la funcion.
     * @param s             Bloque de sentencias del cuerpo de la funcion.
     * @param e             Expresion devuelta por la funcion.
     * @param filaNombre    Fila en la que se encuentra el identificador.
     * @param columnaNombre Columna en la que se encuentra el identificador.
     */
    public SFund(String nombre, LinkedList<SDeclar> argums, TipoDato tr,
                 BloqueSentencias s, E e, int filaNombre, int columnaNombre,
                 int filaReturn, int columnaReturn) {
        this.nombre = nombre;
        this.argums = argums;
        for (SDeclar sd : argums) {
            sd.setEsArgumTrue();
        }
        this.tr = tr;
        this.s = s;
        this.e = e;
        this.filaNombre = filaNombre;
        this.columnaNombre = columnaNombre;
        this.filaReturn = filaReturn;
        this.columnaReturn = columnaReturn;
    }

    @Override
    public void checkAmbitos(TablaSimbolos ts) {
        if (ts.insertaId(nombre, this)) {
            GestionErroresNapol.errorAmbitosIdentificadorDuplicado(filaNombre, columnaNombre, nombre);
        }
        ts.abreBloque();
        for (SDeclar arg : argums) {
            arg.checkAmbitos(ts);
        }
        s.checkAmbitos(ts);
        e.checkAmbitos(ts);
        ts.cierraBloque();
    }

    @Override
    public void checkTipos() {
        s.checkTipos();
        Tipo tipoReturn = tr.getTipo();
        Tipo tipoExpr = e.checkTipos().getTipo();
        if (e.checkTipos().getNumDimensiones() != 0) {
            GestionErroresNapol.errorTiposReturnArray(filaReturn, columnaReturn);
        }
        if (tipoReturn != tipoExpr) {
            GestionErroresNapol.errorTiposReturn(filaReturn, columnaReturn,
                    tipoReturn, tipoExpr);
        }
        if (tr.getNumDimensiones() != 0) {
            GestionErroresNapol.errorNumDimensiones(filaReturn, columnaReturn);
        }
    }

    @Override
    public int precalculo(int i, int nivel) {
        this.nivel = nivel;

        int x = 5;
        for (SDeclar s : argums) {
            s.precalculo(x, nivel + 1);
            x++;
        }
        s.precalculo(x, nivel + 1);
        e.precalculo(nivel + 1);
        return i;
    }

    @Override
    public int longitudPilaEvaluacionExpresiones() {
        return Math.max(e.longitudPilaEvaluacionExpresiones(), s.longitudPilaEvaluacionExpresiones());
    }

    @Override
    public int declarVarLocales() {
        //No es necesario que devuelva nada ya que no podemos anidar
        // funciones/funciones y este metodo se usa para
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

        //No hay funciones anidados
        codigo.add("ujp " + (codigo.size() + 1) + ";\n");
        s.generaCodigo(codigo);
        e.generaCodigo(codigo);
        codigo.add("str 0 0;\n");
        codigo.add("retf;\n");
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

    public TipoDato getTipoReturn() {
        return tr;
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
        return TipoS.FUND;
    }

    @Override
    public String toString() {
        return "SFund(" + nombre + ", " + argums.toString() + ", " + tr + ", "
                + s.toString() + ", " + e.toString() + ")";
    }
}
