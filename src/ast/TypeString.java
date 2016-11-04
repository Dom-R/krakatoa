/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
package ast;

public class TypeString extends Type {

    public TypeString() {
        super("String");
    }

   public String getCname() {
      return "char *";
   }

   public String getKraname() {
	   return "String";
   }

}
