/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class TypeVoid extends Type {

    public TypeVoid() {
        super("void");
    }

   public String getCname() {
      return "void";
   }

   public String getKraname() {
	   return "void";
   }

}
