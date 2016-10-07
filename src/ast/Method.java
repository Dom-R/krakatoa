package ast;
/*
 * Method Class
 */
public class Method extends Type {
	
   public Method(String name, Type type, /*Symbol qualifier,*/ ParamList paramList, StatementList statementList ) {
	  super(name);
      this.paramList = paramList;
      this.statementList = statementList;
   }
   
	public String getCname() {
		return getName();
	}
   
   public void genKra(PW pw) {
   }
   
   private String name;
   private Type type;
   //private Symbol qualifier;
   private ParamList paramList;
   private StatementList statementList;
   
}
