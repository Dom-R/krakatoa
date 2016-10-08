package ast;
/*
 * Method Class
 */

import java.util.Iterator;

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
	   pw.printIdent(qualifier + " " + type.getKraname() + " " + name + "(");
	   
	   if(paramList != null) {
		   pw.print(" ");
		   Iterator<Parameter> p = paramList.elements();
		   while(p.hasNext()) {
			   p.next().genKra(pw);
			   if(p.hasNext()) pw.print(", ");
		   }
		   pw.print(" ");
	   }
	   
	   pw.println(") {");
	   pw.add();
	   
	   Iterator<Statement> s = statementList.elements();
	   while(s.hasNext()) {
		   /*
		    * Debug pois alguns statements ainda retornam null
		    */
		   Statement stmt = s.next();
		   if(stmt != null)
			   stmt.genKra(pw);
		   /*
		    * Fim Debug pois alguns statements ainda retornam null
		    */
		   
		   //s.next().genKra(pw);
	   }
	   
	   pw.sub();
	   pw.printlnIdent("}");
   }
   
   private String name;
   private Type type;
   private Symbol qualifier;
   private ParamList paramList;
   private StatementList statementList;
   private boolean hasReturn;
   
}
