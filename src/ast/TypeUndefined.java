/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class TypeUndefined extends Type {
    // variables that are not declared have this type

   public TypeUndefined() { super("undefined"); }

   public String getCname() {
      return "int";
   }

   public String getKraname() {
	   return "int";
   }

}
