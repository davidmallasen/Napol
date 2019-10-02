#!/bin/bash
# -*- ENCODING: UTF-8 -*-


if [ $# -ne 1 ] ; 
then
	echo "Introduzca el fichero que quiere compilar como parametro";
else
	# Borramos el fichero del arbol
	rm arbol.txt;
	# Borramos el fichero del codigo
	rm input.txt;
	#Compilamos nuestro codigo Napol
	java -cp java-cup-11b.jar:./build/ asint.Main $1;
	#Mensaje de confirmacion
	echo "Compilacion terminada";
fi