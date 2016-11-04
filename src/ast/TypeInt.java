/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class TypeInt extends Type {

    public TypeInt() {
        super("int");
    }

   public String getCname() {
      return "int";
   }

   public String getKraname() {
	   return "int";
   }

}
