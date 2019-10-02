.ONESHELL:
	## Para que funcione el comando cd
all:
	### Cambiamos la ruta
	cd ../alex/
	### Generamos la clase de java asociada al archivo jflex
	jflex AnalizadorLexicoNapol.l
	### Volvemos a la ruta anterior
	cd ../asint/
	### Generamos las clases asociadas al fichero CUP
	java -cp java-cup-11b.jar java_cup.Main -parser AnalizadorSintacticoNapol -symbols ClaseLexica Napol.cup
	### Guardamos en un archivo todas los archivos java del proyecto para compilarlos
	### Busca en la carpeta anterior todos los ficheros que tengan como nombre el acabado en la extension java y añade sus rutas a un fichero
	find ../ -name "*.java" > ficheros-java-compilar.txt
	### Compilamos el proyecto
	### Con el parametro d decimos en que carpeta queremos almacenarlos, y con @ el fichero donde tengamos listadas las rutas de los archivos
	mkdir build
	javac -cp ../asint/java-cup-11b.jar -d build @ficheros-java-compilar.txt
	### Ejecutamos el compilador
	#java -cp java-cup-11b.jar:./build/ asint.Main
		#Esta opcion se hace con "Napol.sh nombreFichero.napol"

clean:
	### Borramos los class
	rm -r ./build
	### Borramos la clase java generada por jflex
	rm ../alex/AnalizadorLexicoNapol.java
	### Borramos las clases generadas por CUP
	rm AnalizadorSintacticoNapol.java ClaseLexica.java
	### Borramos el fichero de ficheros a compilar
	rm ficheros-java-compilar.txt
	### Borramos el fichero del arbol
	rm arbol.txt
	### Borramos el fichero del codigo
	rm input.txt

