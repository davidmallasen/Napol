package asint;

import alex.AnalizadorLexicoNapol;
import asint.ast.Programa;
import errors.GestionErroresNapol;
import java_cup.runtime.Symbol;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        String nombreFichero = args[0];
        //Comprobamos que el fichero tenga la extension del lenguaje
        String[] partesNombre = nombreFichero.split(Pattern.quote("."));
        if (!(partesNombre[1].equals("napol"))) {
            GestionErroresNapol.errorExtensionArchivo();
        }
        Reader input = null;
        try {
            input = new InputStreamReader(new FileInputStream("./Ejemplos/" + nombreFichero));
        } catch (FileNotFoundException e) {
            GestionErroresNapol.ficheroNoEncontrado();
        }

        AnalizadorLexicoNapol alex = new AnalizadorLexicoNapol(input);
        AnalizadorSintacticoNapol asint = new AnalizadorSintacticoNapol(alex);

        Symbol r = asint.parse();
        imprimirArbol(r);

        Programa p = (Programa) r.value;

        input.close();

        p.checkAmbitos();
        if (GestionErroresNapol.errorAmbitos) {
            System.exit(1);
        }

        p.checkTipos();
        if (GestionErroresNapol.errorTipos) {
            System.exit(1);
        }

        p.precalculo();

        BufferedWriter bf = new BufferedWriter(new FileWriter("input.txt"));
        ArrayList<String> codigo = p.generaCodigo();
        //Añadimos comentario cabecera y comentarios de pascal con el numero
        // de linea de las instrucciones y generamos el archivo.
        bf.write("{ \n  programa-P\n\n  generado por el compilador Napol\n  desarrollado por David Mallasen e Ivan Prada\n\n}\n\n");
        for (int i = 0; i < codigo.size(); i++) {
            bf.write("{" + i + "} " + codigo.get(i));
        }
        bf.close();
    }

    public static void imprimirArbol(Symbol r) {
        FileWriter arbol = null;
        PrintWriter p = null;
        try {
            arbol = new FileWriter("arbol.txt");
            p = new PrintWriter(arbol);
            p.println(r.value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != arbol)
                    arbol.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}

