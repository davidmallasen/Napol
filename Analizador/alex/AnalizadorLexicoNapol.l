package alex;

import errors.GestionErroresNapol;
import java_cup.runtime.*;

%%
%cup
%line
%class AnalizadorLexicoNapol
%unicode
%public
%column

%{
  private ALexOperations ops;
  private GestionErroresNapol errores;
  public String lexema() {return yytext();}
  public int fila() {return yyline + 1;}
  public int columna() {return yycolumn + 1;}
  public void fijaGestionErrores(GestionErroresNapol errores) {
   this.errores = errores;
  }
%}

%eofval{
  return ops.unidadEof();
%eofval}

%init{
  ops = new ALexOperations(this);
%init}

letra  = ([A-Z]|[a-z])
digitoPositivo = [1-9]
digito = ({digitoPositivo}|0)

separador = [ \t\r\b\n]
comentario = #[^#]*#
identificador = {letra}({letra}|{digito})*
numeroEntero = ({digitoPositivo}{digito}*) | 0

operadorSuma = \+
operadorResta = \-
operadorMultiplicacion = \*
operadorDivision = \/

parentesisApertura = \(
parentesisCierre = \)
corcheteApertura = \[
corcheteCierre = \]
llaveApertura = \{
llaveCierre = \}

coma  = \,
dosPuntos = \:
puntoYcoma = \;

igual = =
igualIgual = ==
menorOIgual = <=
mayorOIgual = >=
menor = <
mayor = >
distinto = \!=

andLogica = &&
orLogica = \|\|
negacionLogica = \!

%%

{separador}                 {}
{comentario}                {}
{identificador}             {return ops.unidadId();}
{numeroEntero}              {return ops.unidadEnt();}

{operadorSuma}              {return ops.unidadSuma();}
{operadorResta}             {return ops.unidadResta();}
{operadorMultiplicacion}    {return ops.unidadMul();}
{operadorDivision}          {return ops.unidadDiv();}

{parentesisApertura}        {return ops.unidadPAp();}
{parentesisCierre}          {return ops.unidadPCierre();} 
{corcheteApertura}          {return ops.unidadCAp();}
{corcheteCierre}            {return ops.unidadCCierre();}
{llaveApertura}             {return ops.unidadLlaveAp();}
{llaveCierre}               {return ops.unidadLlaveCierre();}
 
{coma}                      {return ops.unidadComa();}
{dosPuntos}                 {return ops.unidadDosPuntos();}
{puntoYcoma}                {return ops.unidadPuntoYComa();}

{igual}                     {return ops.unidadIgual();}
{igualIgual}                {return ops.unidadIgualIgual();}
{menorOIgual}               {return ops.unidadMenorOIgual();}
{mayorOIgual}               {return ops.unidadMayorOIgual();}
{menor}                     {return ops.unidadMenor();}
{mayor}                     {return ops.unidadMayor();}
{distinto}                  {return ops.unidadDistinto();}

{andLogica}                 {return ops.unidadAndLogica();}
{orLogica}                  {return ops.unidadOrLogica();}
{negacionLogica}            {return ops.unidadNegacionLogica();}

[^]                         {errores.errorLexico(fila(), columna(), lexema());}  