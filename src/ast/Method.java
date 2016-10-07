package ast;
/*
 * Method Class
 */

import lexer.*;

public class Method {
	
   public Method(String name, Type type, Symbol qualifier/*, ParamList paramList, StatementList statementList*/ ) {
	  this.name = name;
	  this.type = type;
	  this.qualifier = qualifier;
	  this.paramList = null;
      this.statementList = null;
      this.hasReturn = false;
   }
   
   public void setParamList(ParamList paramList) {
	   this.paramList = paramList;
   }
   
   public void setStatementList(StatementList statementList) {
	   this.statementList = statementList;
   }
   
   public void hasReturn() {
	   hasReturn = true;
   }
   
   public boolean getReturn() {
	   return hasReturn;
   }
   
   public void genKra(PW pw) {
   }
   
   private String name;
   private Type type;
   private Symbol qualifier;
   private ParamList paramList;
   private StatementList statementList;
   private boolean hasReturn;
   
}
