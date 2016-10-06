package ast;
/*
 * Method Class
 */
public class Method {
	
   public Method(String name, Type type, /*Symbol qualifier,*/ ParamList paramList, StatementList statementList ) {
      this.paramList = paramList;
      this.statementList = statementList;
   }
   
   public void genKra(PW pw) {
   }
   
   private String name;
   private Type type;
   //private Symbol qualifier;
   private ParamList paramList;
   private StatementList statementList;
   
}
