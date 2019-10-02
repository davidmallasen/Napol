package asint.ast.expresiones;

import asint.ast.ambitos.TablaSimbolos;
import asint.ast.tipos.TipoDato;

import java.util.ArrayList;

/**
 * Representa una expresion del lenguaje.
 */
public abstract class E {

    /**
     * Indica si la expresion es un argumento de un procedimiento/funcion
     */
    protected boolean esArgum = false;

    /**
     * Realiza la comprobacion de ambitos de la expresion.
     *
     * @param ts Tabla de simbolos
     */
    public abstract void checkAmbitos(TablaSimbolos ts);

    /**
     * Realiza la comprobacion de los tipos de las expresiones.
     *
     * @return Tipo de dato de la expresion
     */
    public abstract TipoDato checkTipos();

    /**
     * Precalcula el nivel de uso.
     *
     * @param nivel nivel actual.
     */
    public abstract void precalculo(int nivel);

    /**
     * Longitud pila evaluacion expresiones.
     *
     * @return Longitud de la pila de evaluacion de expresiones.
     */
    public abstract int longitudPilaEvaluacionExpresiones();

    /**
     * Se encarga de generar el codigo del programa.
     *
     * @param sb Objeto donde vamos construyendo el codigo
     */
    public abstract void generaCodigo(ArrayList<String> sb);

    /**
     * @return TipoE de la expresion.
     */
    public abstract TipoE tipo();

    /**
     * @return Primer operando.
     */
    public E opnd1() {
        throw new UnsupportedOperationException("opnd1");
    }

    /**
     * @return Segundo operando.
     */
    public E opnd2() {
        throw new UnsupportedOperationException("opnd2");
    }

    /**
     * @return Valor true.
     */
    public boolean valT() {
        throw new UnsupportedOperationException("true");
    }

    /**
     * @return Valor false.
     */
    public boolean valF() {
        throw new UnsupportedOperationException("false");
    }

    /**
     * @return Valor del entero.
     */
    public Integer valE() {
        throw new UnsupportedOperationException("entero");
    }

    /**
     * @return Identificador.
     */
    public String valI() {
        throw new UnsupportedOperationException("id");
    }

    /**
     * @return Numero de dimensiones del tipo de la expresion.
     */
    public int getNumDimensiones() {
        return 0;
    }

    /**
     * @return Numero de dimensiones de la declaracion del tipo de la expresion.
     */
    public int getNumDimensionesDeclar() {
        return 0;
    }

    /**
     * Establece esArgum a true.
     */
    public void setEsArgumTrue() {
        esArgum = true;
    }

}