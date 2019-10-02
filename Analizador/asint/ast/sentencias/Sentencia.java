package asint.ast.sentencias;

import asint.ast.ambitos.TablaSimbolos;

import java.util.ArrayList;

/**
 * Proporciona una interfaz que tendran que implementar todas las clases que
 * sean sentencias de nuestro programa.
 */
public interface Sentencia {

    /**
     * Realiza la comprobacion de ambitos.
     *
     * @param ts Tabla de simbolos con la que se realizara la identificacion
     *           de identificadores.
     */
    void checkAmbitos(TablaSimbolos ts);

    /**
     * Realiza la comprobacion estatica de tipos.
     */
    void checkTipos();

    /**
     * Calcula la rho usada en la generacion de codigo y los niveles de uso y
     * declaracion.
     *
     * @param i     indice de la rho actual.
     * @param nivel nivel actual.
     * @return rho al terminar la sentencia
     */
    int precalculo(int i, int nivel);

    /**
     * Longitud pila evaluacion expresiones.
     *
     * @return Longitud de la pila de evaluacion de expresiones.
     */
    int longitudPilaEvaluacionExpresiones();

    /**
     * @return Numero de variables locales en la sentencia.
     */
    int declarVarLocales();

    /**
     * Se encarga de generar el codigo de la sentencia.
     *
     * @param codigo Objeto donde vamos construyendo el codigo
     */
    void generaCodigo(ArrayList<String> codigo);

    /**
     * @return TipoS de la sentencia.
     */
    public TipoS tipo();

}
