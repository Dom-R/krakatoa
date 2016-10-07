package ast;
/*
 * Krakatoa Class
 */
public class KraClass extends Type {
	
   public KraClass( String name ) {
      super(name);
      superclass = null;
      instanceVariableList = null;
      publicMethodList = null;
      privateMethodList = null;
   }
   
   /*public void addSuperClass(KraClass superclass) {
	   this.superclass = superclass;
   }*/
   
   public String getCname() {
      return getName();
   }
   
   private String name;
   private KraClass superclass;
   private InstanceVariableList instanceVariableList;
   private MethodList publicMethodList, privateMethodList;
   // m�todos p�blicos get e set para obter e iniciar as vari�veis acima,
   // entre outros m�todos
}
