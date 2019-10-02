package alex;

import asint.ClaseLexica;
import java.util.HashMap;
import java_cup.runtime.*;

public class ALexOperations {

    private AnalizadorLexicoNapol alex;

    static final HashMap<String , Integer> PAL_RESERVADAS =
    new HashMap<String , Integer>() {
        {
            put("fun", ClaseLexica.FUN);
            put("proc", ClaseLexica.PROC);
            put("returns", ClaseLexica.RETS);
            put("return", ClaseLexica.RET);
            put("do", ClaseLexica.DO);
            put("return", ClaseLexica.RET);
            put("if", ClaseLexica.IF);
            put("else", ClaseLexica.ELSE);
            put("while", ClaseLexica.WHILE);
            put("for", ClaseLexica.FOR);
            put("int", ClaseLexica.INT);
            put("bool", ClaseLexica.BOOL);
            put("true", ClaseLexica.TRUE);
            put("false", ClaseLexica.FALSE);
        }
    };

    public ALexOperations(AnalizadorLexicoNapol alex) {
        this.alex = alex;
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, alex.fila(), alex.columna(), value);
    }

/*------------------------------------------------------------------------*/
    public Symbol unidadId() {
        if (PAL_RESERVADAS.containsKey(alex.lexema())) {
            return symbol(PAL_RESERVADAS.get(alex.lexema()),
                                    alex.lexema());
        } else {
            return symbol(ClaseLexica.ID,
                                    alex.lexema());
        }
    }
    public Symbol unidadEnt() {
        return symbol(ClaseLexica.ENT, alex.lexema());
    }

    public Symbol unidadSuma() {
        return symbol(ClaseLexica.MAS, "+");
    }
    public Symbol unidadResta() {
        return symbol(ClaseLexica.MENOS, "-");
    }
    public Symbol unidadMul() {
        return symbol(ClaseLexica.MUL, "*");
    }
    public Symbol unidadDiv() {
        return symbol(ClaseLexica.DIV, "/");
    }


    public Symbol unidadPAp() {
        return symbol(ClaseLexica.PAP, "(");
    }
    public Symbol unidadPCierre() {
        return symbol(ClaseLexica.PCIERRE, ")");
    }
    public Symbol unidadCAp() {
        return symbol(ClaseLexica.CAP, "[");
    }
    public Symbol unidadCCierre() {
        return symbol(ClaseLexica.CCIERRE, "]");
    }
    public Symbol unidadLlaveAp() {
        return symbol(ClaseLexica.LLAP, "{");
    }
    public Symbol unidadLlaveCierre() {
        return symbol(ClaseLexica.LLCIERRE, "}");
    }

    public Symbol unidadComa() {
        return symbol(ClaseLexica.COMA, ",");
    }
    public Symbol unidadDosPuntos() {
        return symbol(ClaseLexica.DOSPUNT, ":");
    }
    public Symbol unidadPuntoYComa() {
        return symbol(ClaseLexica.PUNTOCOMA, ";");
    }

    public Symbol unidadIgual() {
        return symbol(ClaseLexica.IGUAL, "=");
    }
    public Symbol unidadIgualIgual() {
        return symbol(ClaseLexica.II, "==");
    }
    public Symbol unidadMenorOIgual() {
        return symbol(ClaseLexica.MEI, "<=");
    }
    public Symbol unidadMayorOIgual() {
        return symbol(ClaseLexica.MAI, ">=");
    }
    public Symbol unidadMenor() {
        return symbol(ClaseLexica.ME, "<");
    }
    public Symbol unidadMayor() {
        return symbol(ClaseLexica.MA, ">");
    }
    public Symbol unidadDistinto() {
        return symbol(ClaseLexica.DIS, "!=");
    }



    public Symbol unidadAndLogica() {
        return symbol(ClaseLexica.AND, "&&");
    }
    public Symbol unidadOrLogica() {
        return symbol(ClaseLexica.OR, "||");
    }
    public Symbol unidadNegacionLogica() {
        return symbol(ClaseLexica.NEG, "!");
    }


    public Symbol unidadEof() {
        return symbol(ClaseLexica.EOF, "<EOF>");
    }
}