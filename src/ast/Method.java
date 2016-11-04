/*-------------------------------------------------------------------------------------------------------------------------

Dominik Reller - 587516
Luan Gustavo Maia Dias - 587737

-------------------------------------------------------------------------------------------------------------------------*/
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
      this.whileCounter = 0;
   }

   public void setParamList(ParamList paramList) {
	   this.paramList = paramList;
   }

   public ParamList getParamList() {
	   return paramList;
   }

   public void setStatementList(StatementList statementList) {
	   this.statementList = statementList;
   }

   /*****************************/
   /* Validacao para return */
   /*****************************/
   public void hasReturn() {
	   hasReturn = true;
   }

   public boolean getReturn() {
	   return hasReturn;
   }
   /*****************************/
   /* Fim validacao para return */
   /*****************************/

   /****************************/
   /* Validacao para while */
   /****************************/
   public void addWhile() {
	   whileCounter++;
   }

   // Retorna true se pode usar break, false caso nao possa pq esta fora de while
   public boolean canBreak() {
	   if(whileCounter > 0) {
		   return true;
	   } else {
		   return false;
	   }
   }

   public void addBreak() {
	   whileCounter--;
   }
   /****************************/
   /* Fim validacao para While */
   /****************************/

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
		   else
			   System.out.println("Debug: Statement Null!");
		   /*
		    * Fim Debug pois alguns statements ainda retornam null
		    */

		   //s.next().genKra(pw);
	   }

	   pw.sub();
	   pw.printlnIdent("}");
   }

   public String getName() {
	   return name;
   }

   public Type getType() {
	   return type;
   }

   private String name;
   private Type type;
   private Symbol qualifier;
   private ParamList paramList;
   private StatementList statementList;
   private boolean hasReturn;
   private int whileCounter;

}
