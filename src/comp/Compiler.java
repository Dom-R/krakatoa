
package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		// Program ::= KraClass { KraClass }
		ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
		ArrayList<KraClass> kraClassList = new ArrayList<>();
		try {
			while ( lexer.token == Symbol.MOCall ) {
				metaobjectCallList.add(metaobjectCall());
			}
			kraClassList.add(classDec());
			while ( lexer.token == Symbol.CLASS )
				kraClassList.add(classDec());
			if ( lexer.token != Symbol.EOF ) {
				signalError.showError("End of file expected");
			}
			
			// Verificacao se ha uma classe chamada Program
			boolean hasProgram = false;
			Iterator<KraClass> iter = kraClassList.iterator();
			while(iter.hasNext()) {
				if(iter.next().getName().equals("Program")) {
					hasProgram = true;
				}
			}
			if(!hasProgram) {
				signalError.showError("Source code without a class 'Program'");
			}
		}
		catch( RuntimeException e) {
			// if there was an exception, there is a compilation signalError
			//compilationErrorList.add(e);
		}
		Program program = new Program(kraClassList, metaobjectCallList, compilationErrorList);
		return program;
	}

	/**  parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
     * <code>
     * @ce(5, "'class' expected") <br>
     * clas Program <br>
     *     public void run() { } <br>
     * end <br>
     * </code>
     * 
	   
	 */
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {
		String name = lexer.getMetaobjectName();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		if ( lexer.token == Symbol.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Symbol.LITERALINT || lexer.token == Symbol.LITERALSTRING ||
					lexer.token == Symbol.IDENT ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case IDENT:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if ( lexer.token == Symbol.COMMA ) 
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Symbol.RIGHTPAR ) 
				signalError.showError("')' expected after metaobject call with parameters");
			else
				lexer.nextToken();
		}
		if ( name.equals("nce") ) {
			if ( metaobjectParamList.size() != 0 )
				signalError.showError("Metaobject 'nce' does not take parameters");
		}
		else if ( name.equals("ce") ) {
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				signalError.showError("Metaobject 'ce' take three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  )
				signalError.showError("The first parameter of metaobject 'ce' should be an integer number");
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				signalError.showError("The second and third parameters of metaobject 'ce' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )  
				signalError.showError("The fourth parameter of metaobject 'ce' should be a literal string");
			
		}
			
		return new MetaobjectCall(name, metaobjectParamList);
	}

	private KraClass classDec() {
		// Note que os métodos desta classe não correspondem exatamente às
		// regras
		// da gramática. Este método classDec, por exemplo, implementa
		// a produção KraClass (veja abaixo) e partes de outras produções.

		/*
		 * KraClass ::= ``class'' Id [ ``extends'' Id ] "{" MemberList "}"
		 * MemberList ::= { Qualifier Member } 
		 * Member ::= InstVarDec | MethodDec
		 * InstVarDec ::= Type IdList ";" 
		 * MethodDec ::= Qualifier Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 
		 * Qualifier ::= [ "static" ]  ( "private" | "public" )
		 */
		
		if ( lexer.token != Symbol.CLASS ) signalError.showError("'class' expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.IDENT )
			signalError.show(ErrorSignaller.ident_expected);
		
		String className = lexer.getStringValue();
		//System.out.println("Current Class: " + className);
		currentClass = new KraClass(className);
		symbolTable.putInGlobal(className, currentClass); //Instanciar objeto da classe fora do symboltable e inserir ela pelo symboltable. Alterar um dos objetos altera o outro?
		
		lexer.nextToken();
		if ( lexer.token == Symbol.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show(ErrorSignaller.ident_expected);
			String superclassName = lexer.getStringValue();
			
			// Validacao se classe esta se extendendo
			if(className.equals(superclassName)) {
				signalError.showError("Class '" + className + "' is inheriting from itself");
			}
			
			// Inserir verificacao caso superclasse nao esteja no symboltable
			KraClass superclass = symbolTable.getInGlobal(superclassName);
			
			// Não tem teste se a superclasse existe ou não
			if(superclass == null) {
				// signalError.showError("No superclass with name:" + superclassName
			}
			
			currentClass.setSuperclass(superclass);
			
			lexer.nextToken();
		}
		if ( lexer.token != Symbol.LEFTCURBRACKET )
			signalError.showError("{ expected", true);
		lexer.nextToken();

		while (lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {

			Symbol qualifier;
			switch (lexer.token) {
			case PRIVATE:
				lexer.nextToken();
				qualifier = Symbol.PRIVATE;
				break;
			case PUBLIC:
				lexer.nextToken();
				qualifier = Symbol.PUBLIC;
				break;
			default:
				signalError.showError("private, or public expected");
				qualifier = Symbol.PUBLIC;
			}
			Type t = type();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			String name = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token == Symbol.LEFTPAR ) {
				
				// Limpa variaveis locais
				symbolTable.removeLocalIdent();
				
				//System.out.println("Metodo: " + qualifier.toString() + " " + name);
				if(qualifier == Symbol.PRIVATE)
					currentClass.addPrivateMethod(methodDec(t, name, qualifier));
				else
					currentClass.addPublicMethod(methodDec(t, name, qualifier));
			} else if ( qualifier != Symbol.PRIVATE )
				signalError.showError("Attempt to declare public instance variable '" + name + "'");
			else {
				ArrayList<InstanceVariable> arrayInstanceVariable = instanceVarDec(t, name);
				for( InstanceVariable i : arrayInstanceVariable ) {
					//System.out.println("Debug: " + i.getType() + " " + i.getName());
					currentClass.addInstanceVariable(i);
				}
			}
		}
		
		// Verificacao se classe Program possui metodo run
		// TODO: Arrumar
		/*if(className.equals("Program")) {
			boolean hasRun = false;
			Iterator<Method> publicMethodIterator = currentClass.getPublicMethodList().elements();
			while(publicMethodIterator.hasNext()) {
				if(publicMethodIterator.next().getName().equals("run")) {
					hasRun = true;
				}
			}
			if(!hasRun) {
				signalError.showError("Method 'run' was not found in class 'Program'");
			}
		}*/
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.showError("public/private or \"}\" expected");
		lexer.nextToken();

		return currentClass;
	}

	private ArrayList<InstanceVariable> instanceVarDec(Type type, String name) {
		// InstVarDec ::= [ "static" ] "private" Type IdList ";"
		
		ArrayList<InstanceVariable> arrayInstanceVariable = new ArrayList<InstanceVariable>();
		
		// Verificacao se variavel de instancia esta sendo redeclarada
		Iterator<InstanceVariable> iter = currentClass.getInstanceVariableList().elements();
		while(iter.hasNext()) {
			InstanceVariable tempVar = iter.next();
			if(tempVar.getName().equals(name)) {
				signalError.showError("Variable '" + name + "' is being redeclared");
				break;
			}
		}
		
		arrayInstanceVariable.add(new InstanceVariable(name, type) );
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			String variableName = lexer.getStringValue();
			
			// Verificacao se variavel de instancia esta sendo redeclarada
			iter = currentClass.getInstanceVariableList().elements();
			while(iter.hasNext()) {
				InstanceVariable tempVar = iter.next();
				if(tempVar.getName().equals(variableName)) {
					signalError.showError("Variable '" + variableName + "' is being redeclared");
					break;
				}
			}
			
			arrayInstanceVariable.add(new InstanceVariable(variableName, type) );
			lexer.nextToken();
		}
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return arrayInstanceVariable;
	}

	private Method methodDec(Type type, String name, Symbol qualifier) {
		/*
		 * MethodDec ::= Qualifier Return Id "("[ FormalParamDec ] ")" "{"
		 *                StatementList "}"
		 */
		
		// Verificacao se metodo run da classe Program eh privado
		if(currentClass.getName().equals("Program") && name.equals("run") && qualifier == Symbol.PRIVATE) {
			signalError.showError("Method 'run' of class 'Program' cannot be private");
		}

		// Verificacao se metodo esta sendo redeclarado
		Iterator<Method> privateMethodIterator = currentClass.getPrivateMethodList().elements();
		while(privateMethodIterator.hasNext()) {
			if(privateMethodIterator.next().getName().equals(name)) {
				signalError.showError("Method '" + name + "' is being redefined");
			}
		}
		
		// Verificacao se metodo esta sendo redeclarado
		Iterator<Method> publicMethodIterator = currentClass.getPublicMethodList().elements();
		while(publicMethodIterator.hasNext()) {
			if(publicMethodIterator.next().getName().equals(name)) {
				signalError.showError("Method '" + name + "' is being redefined");
			}
		}
		
		// Verificacao se metodo tem mesmo nome de uma variavel de instancia
		Iterator<InstanceVariable> instanceVariableIterator = currentClass.getInstanceVariableList().elements();
		while(instanceVariableIterator.hasNext()) {
			if(instanceVariableIterator.next().getName().equals(name)) {
				signalError.showError("Method '" + name + "' has name equal to an instance variable");
			}
		}
		
		currentMethod = new Method(name, type, qualifier);
		
		lexer.nextToken();
		ParamList paramList = null;
		if ( lexer.token != Symbol.RIGHTPAR ) paramList = formalParamDec();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");

		
		// Verifica se metodo esta sendo redeclarado com estrutura diferente de sua superclasse
		if(currentClass.getSuperclass() != null) {
			if(currentClass.getSuperclass().getPublicMethodList() != null) {
				// Procura metodo publico com mesmo nome na superclasse
				Iterator<Method> iter = currentClass.getSuperclass().getPublicMethodList().elements();
				while(iter.hasNext()) {
					Method tempMethod = iter.next();
					if( tempMethod.getName().equals(name) ) {
						
						// verifica return type diferente
						if(tempMethod.getType() != type) {
							signalError.showError("Method '" + name + "' of subclass '" + currentClass.getName() + "' has a signature different from method inherited from superclass '" + currentClass.getSuperclass().getName() + "'");
						}
						
						// verifica se parametros sao diferentes
						if(paramList == null && tempMethod.getParamList() == null) {
							break;
						} else if( ( paramList == null && tempMethod.getParamList() != null )
								|| ( paramList != null && tempMethod.getParamList() == null )) {
							signalError.showError("Method '" + name + "' of the subclass '" + currentClass.getName() + "' has a signature different from the same method of superclass '" + currentClass.getSuperclass().getName() + "'");
						} else {
							
							//System.out.println("Current Param List" + paramList);
							//System.out.println("Temp Param List" + tempMethod.getParamList());
							
							// TODO: Implementar verificacao de tipos diferentes no parametro aqui
							Iterator<Parameter> iterCurrentParamList = paramList.elements();
							Iterator<Parameter> iterTempParamList = tempMethod.getParamList().elements();
							while(iterCurrentParamList.hasNext() && iterTempParamList.hasNext()) {
								if(iterCurrentParamList.next().getType() != iterTempParamList.next().getType()) {
									signalError.showError("Method '" + name + "' is being redefined in subclass '" + currentClass.getName() + "' with a signature different from the method of superclass '" + currentClass.getSuperclass().getName() + "'");
								}
							}

						}
						
					}
				}
			}
		}
		
		// Verificacao se metodo run da classe Program esta recebendo parametros
		if(currentClass.getName().equals("Program") && name.equals("run") && paramList != null) {
			signalError.showError("Method 'run' of class 'Program' cannot take parameters");
		}
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTCURBRACKET ) signalError.showError("{ expected");

		// Validacao que metodo run da classe Program tem que retornar obrigatoriamente void
		if(currentClass.getName().equals("Program") && name.equals("run") && type != Type.voidType) {
			signalError.showError("Method 'run' of class 'Program' with a return value type different from 'void'");
		}
		
		lexer.nextToken();
		StatementList statementList = statementList();
		
		// Verificacao se metodo corrente tem return
		if(type != Type.voidType && currentMethod.getReturn() == false ) {
			signalError.showError("Missing 'return' statement in method '" + currentMethod.getName() + "'");
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) signalError.showError("} expected");
		lexer.nextToken();
		
		currentMethod.setParamList(paramList);
		currentMethod.setStatementList(statementList);
		
		return currentMethod;
	}

	private LocalVariableList localDec() {
		// LocalDec ::= Type IdList ";"

		LocalVariableList variableList = new LocalVariableList();
		
		Type type = type();
		
		// TODO: Verificar se tipo eh valido
		
		if ( lexer.token != Symbol.IDENT ) signalError.showError("Identifier expected");
		
		// Valida se variavel ja nao foi declarada
		if(symbolTable.getInLocal(lexer.getStringValue()) != null) {
			signalError.showError("Variable '" + lexer.getStringValue() + "' is being redeclared");
		}
		
		Variable v = new Variable(lexer.getStringValue(), type);
		
		// Adiciona a tabela local e a lista de variaveis
		symbolTable.putInLocal(lexer.getStringValue(), v);
		variableList.addElement(v);
		
		lexer.nextToken();
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			
			// Valida se variavel ja nao foi declarada
			if(symbolTable.getInLocal(lexer.getStringValue()) != null) {
				signalError.showError("Variable '" + lexer.getStringValue() + "' is being redeclared");
			}
			
			v = new Variable(lexer.getStringValue(), type);
			
			// Adiciona a tabela local e a lista de variaveis
			symbolTable.putInLocal(lexer.getStringValue(), v);
			variableList.addElement(v);
			
			lexer.nextToken();
		}
		
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.showError("Missing ';'", true);
		lexer.nextToken();
		
		return variableList;
	}

	private ParamList formalParamDec() {
		// FormalParamDec ::= ParamDec { "," ParamDec }

		ParamList paramList = new ParamList();
		
		paramList.addElement(paramDec());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			paramList.addElement(paramDec());
		}
		
		return paramList;
	}

	private Parameter paramDec() {
		// ParamDec ::= Type Id

		Type type = type();
		if ( lexer.token != Symbol.IDENT ) signalError.showError("Identifier expected");
		String name = lexer.getStringValue();
		lexer.nextToken();
		
		Parameter parameter = new Parameter(name, type);
		
		// Adiciona o parametro a lista local de variaveis e parametros
		symbolTable.putInLocal(name, parameter);
		
		return parameter;
	}

	private Type type() {
		// Type ::= BasicType | Id
		Type result;

		switch (lexer.token) {
		case VOID:
			result = Type.voidType;
			break;
		case INT:
			result = Type.intType;
			break;
		case BOOLEAN:
			result = Type.booleanType;
			break;
		case STRING:
			result = Type.stringType;
			break;
		case IDENT:
			// # corrija: faça uma busca na TS para buscar a classe
			// IDENT deve ser uma classe.
			String className = lexer.getStringValue();
			result = symbolTable.getInGlobal(className);
			
			if(result == null) {
				signalError.showError("Type '" + className + "' was not found");
				result = Type.undefinedType;
			}
			
			break;
		default:
			signalError.showError("Type expected");
			result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;
	}

	private StatementComposite compositeStatement() {

		lexer.nextToken();
		StatementList statementList = statementList();
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.showError("} expected");
		else
			lexer.nextToken();
		
		StatementComposite statementComposite = new StatementComposite(statementList);
		return statementComposite;
	}

	private StatementList statementList() {
		// CompStatement ::= "{" { Statement } "}"
		
		StatementList statementList = new StatementList();
		Symbol tk;
		// statements always begin with an identifier, if, read, write, ...
		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET
				&& tk != Symbol.ELSE)
			statementList.addElement(statement());
		
		return statementList;
	}

	private Statement statement() {
		/*
		 * Statement ::= Assignment ``;'' | IfStat |WhileStat | MessageSend
		 *                ``;'' | ReturnStat ``;'' | ReadStat ``;'' | WriteStat ``;'' |
		 *               ``break'' ``;'' | ``;'' | CompStatement | LocalDec
		 */

		Statement statement = null;
		
		switch (lexer.token) {
		case THIS:
		case IDENT:
		case SUPER:
		case INT:
		case BOOLEAN:
		case STRING:
			statement = assignExprLocalDec();
			if(statement instanceof MessageSendStatement) {
				 MessageSendStatement message = (MessageSendStatement) statement;
				 if(message.getType() != Type.voidType) {
					 signalError.showError("Message send '" + message.getName() + "' returns a value that is not used");
				 }
			}
			break;
		case ASSERT:
			statement = assertStatement();
			break;
		case RETURN:
			statement = returnStatement();
			break;
		case READ:
			statement = readStatement();
			break;
		case WRITE:
			statement = writeStatement();
			break;
		case WRITELN:
			statement = writelnStatement();
			break;
		case IF:
			statement = ifStatement();
			break;
		case BREAK:
			statement = breakStatement();
			break;
		case WHILE:
			statement = whileStatement();
			break;
		case DO:
			statement = doWhileStatement();
			break;
		case SEMICOLON:
			statement = nullStatement();
			break;
		case LEFTCURBRACKET:
			statement = compositeStatement();
			break;
		default:
			//System.out.println("Statement expected: " + lexer.token.toString());
			signalError.showError("Statement expected");
		}
		
		return statement; // REMOVER
	}

	private Statement assertStatement() {
		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		Expr e = expr();
		if ( e.getType() != Type.booleanType )
			signalError.showError("boolean expression expected");
		if ( lexer.token != Symbol.COMMA ) {
			this.signalError.showError("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Symbol.LITERALSTRING ) {
			this.signalError.showError("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		if ( lexer.token == Symbol.SEMICOLON )
			lexer.nextToken();
		
		return new StatementAssert(e, lineNumber, message);
	}

	/*
	 * retorne true se 'name' é uma classe declarada anteriormente. É necessário
	 * fazer uma busca na tabela de símbolos para isto.
	 */
	private boolean isType(String name) {
		return this.symbolTable.getInGlobal(name) != null;
	}

	/*
	 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec
	 */
	private Statement assignExprLocalDec() { // Retornar Statement

		Statement statement = null;
		if ( lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
				|| lexer.token == Symbol.STRING ||
				// token é uma classe declarada textualmente antes desta
				// instrução
				(lexer.token == Symbol.IDENT && isType(lexer.getStringValue())) ) {
			/*
			 * uma declaração de variável. 'lexer.token' é o tipo da variável
			 * 
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec 
			 * LocalDec ::= Type IdList ``;''
			 */
			statement = localDec(); // retornar LocalVariableList que herda de statement
		}
		else {
			/*
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ]
			 */
			Expr left = expr();
			Expr right = null;
			if ( lexer.token == Symbol.ASSIGN ) {
				lexer.nextToken();
				right = expr();
				
				// Validacao quando right retorna void
				if(right.getType() == Type.voidType) {
					signalError.showError("Expression expected in the right-hand side of assignment");
				}
				
				// Validacao para atribuicao de null
				if(right instanceof NullExpr && ( left.getType() == Type.intType || left.getType() == Type.booleanType ) ) {
					signalError.showError("Type error: 'null' cannot be assigned to a variable of a basic type");
				}
				
				// Validacao se lado esquerdo eh classe e lado direito eh tipo basico
				if(left.getType() instanceof KraClass && !(right.getType() instanceof KraClass) ) {
					signalError.showError("Type error: the type of the expression of the right-hand side is a basic type and the type of the variable of the left-hand side is a class");
				}
				
				// Validacao se lado direito eh classe e lado esquerdo eh tipo basico
				if( !(left.getType() instanceof KraClass) && right.getType() instanceof KraClass ) {
					signalError.showError("Type error: type of the left-hand side of the assignment is a basic type and the type of the right-hand side is a class");
				}
				
				// Validacao inicial se as duas expr tem o mesmo tipo
				// TODO: Arrumar
				if(left.getType() != right.getType()) {
					
					// Validacao expr direita nao eh subtipo da expr esquerda
					if(right.getType() instanceof KraClass && left.getType() instanceof KraClass && !(right instanceof NullExpr) ) {
						
						/*KraClass classRight = (KraClass) right.getType();
						
						KraClass superClassRight = classRight.getSuperclass();
						while(superClassRight != null) {
							if(superClassRight == left.getType()) {
								break;
							}
							superClassRight = superClassRight.getSuperclass();
						}*/
						
						if(!isSubType((KraClass) left.getType(), (KraClass) right.getType())) {
							signalError.showError("Type error: type of the right-hand side of the assignment is not a subclass of the left-hand side");
						}
						
					}
					
					if( !(right.getType() instanceof KraClass && left.getType() instanceof KraClass) ) {
						signalError.showError("'" + right.getType().getName() + "' cannot be assigned to '" + left.getType().getName() + "'");
					}
				}
				
				
			} // StatementExpr que herda de statement
			
			if ( lexer.token != Symbol.SEMICOLON )
				signalError.showError("';' expected", true);
			else
				lexer.nextToken();
			
			// Message Send para Statement
			if(right == null && left instanceof MessageSend ) {
				//System.out.println("Message Send");
				statement = new MessageSendStatement((MessageSend) left);
			} else {
				statement = new StatementExpr(left, right);
			}
		}
		return statement;
	}

	private ExprList realParameters() {
		ExprList anExprList = null;

		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		if ( startExpr(lexer.token) ) anExprList = exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		return anExprList;
	}

	private StatementWhile whileStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		Expr expr = expr();
		
		// Validacao se expressao eh booleana
		if(expr.getType() != Type.booleanType) {
			signalError.showError("non-boolean expression in 'while' command");
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		
		// Validacao de break
		currentMethod.addWhile();
		
		Statement statement = statement();
		
		StatementWhile statementWhile = new StatementWhile(expr, statement);
		return statementWhile;
	}
	
	private StatementDoWhile doWhileStatement() {
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTCURBRACKET ) signalError.showError("'{' expected after 'do'");
		lexer.nextToken();
		
		StatementList statementList = new StatementList();
		// statements always begin with an identifier, if, read, write, ...
		while (lexer.token != Symbol.RIGHTCURBRACKET)
			statementList.addElement(statement());
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) signalError.showError("} expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.WHILE ) signalError.showError("'while' expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		Expr expr = expr();
		
		// Validacao se expressao eh booleana
		if(expr.getType() != Type.booleanType) {
			signalError.showError("boolean expression expected in a do-while statement");
		}
		
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON ) signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		StatementDoWhile statementDoWhile = new StatementDoWhile(expr, statementList);
		return statementDoWhile;
	}

	private StatementIf ifStatement() {
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		Expr expr = expr();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		Statement statementThen = statement();
		
		Statement statementElse = null;
		if ( lexer.token == Symbol.ELSE ) {
			lexer.nextToken();
			statementElse = statement();
		}
		
		StatementIf statementIf = new StatementIf(expr, statementThen, statementElse);
		return statementIf;
	}

	private StatementReturn returnStatement() {

		lexer.nextToken();
		Expr expr = expr();
		
		// TODO: Verificar tipo de expr e verificar se o tipo de expr eh igual ao do tipo do metodo, senao da erro
		// Arrumar isso abaixo
		if(currentMethod.getType() != expr.getType()) {
			
			// Validacao expr direita nao eh subtipo da expr esquerda
			if(expr.getType() instanceof KraClass && currentMethod.getType() instanceof KraClass && !(expr instanceof NullExpr) ) {
				
				KraClass classRight = (KraClass) expr.getType();
				
				KraClass superClassRight = classRight.getSuperclass();
				while(superClassRight != null) {
					if(superClassRight == currentMethod.getType()) {
						break;
					}
					superClassRight = superClassRight.getSuperclass();
				}
				
				if(superClassRight == null) {
					signalError.showError("Type error: type of the right-hand side of the assignment is not a subclass of the left-hand side");
				}
				
			}
			
			if( !(expr.getType() instanceof KraClass && currentMethod.getType() instanceof KraClass) ) {
				signalError.showError("Illegal 'return' statement. Method returns '" + currentMethod.getType().getName() + "'");
			}
		}
		
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		// Seta que metodo tem return
		currentMethod.hasReturn();
		
		StatementReturn statementReturn = new StatementReturn(expr);
		return statementReturn;
	}

	private StatementRead readStatement() {

		ArrayList<VariableExpr> variableExprList = new ArrayList<VariableExpr>();
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		while (true) {
			boolean flagThis = false;
			if ( lexer.token == Symbol.THIS ) {
				lexer.nextToken();
				if ( lexer.token != Symbol.DOT ) signalError.showError(". expected");
				lexer.nextToken();
				flagThis = true;
			}
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Command 'read' expects a variable");

			String name = lexer.getStringValue();
			
			// verifica se a variavel eh uma variavel de instancia
			Variable variable = null;
			if(flagThis) {
				// Procura a variavel de instancia e adiciona ela a lista de VariableExpr
				Iterator<InstanceVariable> i = currentClass.getInstanceVariableList().elements();
				while(i.hasNext()) {
					InstanceVariable v = i.next();
					if(v.getName().equals(name)) {
						variable = v;
					}
				}
			} else {
				if(symbolTable.getInLocal(name) != null) {
					variable = symbolTable.getInLocal(name);
				}
			}
			
			// Verificacao para impedir boolean no read
			if(variable.getType() == Type.booleanType) {
				signalError.showError("Command 'read' does not accept 'boolean' variables");
			}
			
			// Verificacao para impedir variaveis que nao sao de tipo int ou string
			if(variable.getType() != Type.intType && variable.getType() != Type.stringType ) {
				signalError.showError("'int' or 'String' expression expected");
			}
			
			variableExprList.add(new VariableExpr(variable, flagThis));
			
			lexer.nextToken();
			if ( lexer.token == Symbol.COMMA )
				lexer.nextToken();
			else
				break;
		}

		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		StatementRead statementRead = new StatementRead(variableExprList);
		return statementRead;
	}

	private StatementWrite writeStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		ExprList exprList = exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		
		// Verificacao que impede write de expressoes com tipo booleano ou de objeto
		Iterator<Expr> iter = exprList.getExprListIterator();
		while(iter.hasNext()) {
			Type type = iter.next().getType();
			if( type == Type.booleanType) {
				signalError.showError("Command 'write' does not accept 'boolean' expressions");
			}
			
			if( type instanceof KraClass ) {
				signalError.showError("Command 'write' does not accept objects");
			}
		}
		
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		StatementWrite statementWrite = new StatementWrite(exprList);
		return statementWrite;
	}

	private StatementWrite writelnStatement() {

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		ExprList exprList = exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		StatementWrite statementWrite = new StatementWrite(exprList, true);
		return statementWrite;
	}

	private StatementBreak breakStatement() {
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		
		// Validacao de break fora de while
		if(currentMethod.canBreak()) {
			currentMethod.addBreak();
		} else {
			signalError.showError("'break' statement found outside a 'while' statement");
		}
		
		lexer.nextToken();
		
		return new StatementBreak();
	}

	private StatementNull nullStatement() {
		lexer.nextToken();
		return new StatementNull();
	}

	private ExprList exprList() {
		// ExpressionList ::= Expression { "," Expression }

		ExprList anExprList = new ExprList();
		anExprList.addElement(expr());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			anExprList.addElement(expr());
		}
		return anExprList;
	}

	private Expr expr() {

		Expr left = simpleExpr();
		Symbol op = lexer.token;
		if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
				|| op == Symbol.LT || op == Symbol.GE || op == Symbol.GT ) {
			lexer.nextToken();
			Expr right = simpleExpr();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr simpleExpr() {
		Symbol op;

		Expr left = term();
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
				|| op == Symbol.OR) {
			
			// Validacao de + e - com boolean
			if(left.getType() == Type.booleanType && (op == Symbol.MINUS || op == Symbol.PLUS)) {
				signalError.showError("type boolean does not support operation '" + op.toString()  + "'");
			}
			
			lexer.nextToken();
			Expr right = term();
			
			if( (op == Symbol.MINUS || op == Symbol.PLUS) && ( ( left.getType() == Type.intType && right.getType() != Type.intType ) || ( left.getType() != Type.intType && right.getType() == Type.intType ) ) ) {
				signalError.showError("operator '" + op.toString() + "' of 'int' expects an 'int' value");
			}
			
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr term() {
		Symbol op;

		Expr left = signalFactor();
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
				|| op == Symbol.AND) {
			
			// Validacao que && nao pode ser usado com int
			if(left.getType() == Type.intType && op == Symbol.AND) {
				signalError.showError("type 'int' does not support operator '&&'");
			}
			
			lexer.nextToken();
			Expr right = signalFactor();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr signalFactor() {
		Symbol op;
		if ( (op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {
			lexer.nextToken();
			
			Expr factor = factor();
			
			// Validacao que sinal de positivo e negativo nao podem ser usados com booleano 
			if(factor.getType() == Type.booleanType) {
				signalError.showError("Operator '" + op.toString() + "' does not accepts 'boolean' expressions");
			}
			
			return new SignalExpr(op, factor);
		}
		else
			return factor();
	}

	/*
	 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
	 *      ObjectCreation | PrimaryExpr
	 * 
	 * BasicValue ::= IntValue | BooleanValue | StringValue 
	 * BooleanValue ::=  "true" | "false" 
	 * ObjectCreation ::= "new" Id "(" ")" 
	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 Id  |
	 *                 Id "." Id | 
	 *                 Id "." Id "(" [ ExpressionList ] ")" |
	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
	 *                 "this" | 
	 *                 "this" "." Id | 
	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
	 */
	private Expr factor() {

		Expr anExpr;
		ExprList exprList;
		String messageName, id;

		switch (lexer.token) {
		// IntValue
		case LITERALINT:
			return literalInt();
			// BooleanValue
		case FALSE:
			lexer.nextToken();
			return LiteralBoolean.False;
			// BooleanValue
		case TRUE:
			lexer.nextToken();
			return LiteralBoolean.True;
			// StringValue
		case LITERALSTRING:
			String literalString = lexer.getLiteralStringValue();
			lexer.nextToken();
			return new LiteralString(literalString);
			// "(" Expression ")" |
		case LEFTPAR:
			lexer.nextToken();
			anExpr = expr();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(anExpr);

			// "null"
		case NULL:
			lexer.nextToken();
			return new NullExpr();
			// "!" Factor
		case NOT:
			lexer.nextToken();
			anExpr = expr();
			
			// Validacao que qualquer outro tipo exceto booleano nao pode ser negado
			if(anExpr.getType() != Type.booleanType) {
				signalError.showError("Operator '!' does not accepts '" + anExpr.getType().getName() + "' values");
			}
			
			return new UnaryExpr(anExpr, Symbol.NOT);
			// ObjectCreation ::= "new" Id "(" ")"
		case NEW:
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");

			String className = lexer.getStringValue();
			/*
			 * // encontre a classe className in symbol table KraClass 
			 *      aClass = symbolTable.getInGlobal(className); 
			 *      if ( aClass == null ) ...
			 */
			KraClass classObj = symbolTable.getInGlobal(className);
			if( classObj == null ) {
				// Faz algo que não sabemos
				signalError.showError("Class '" + className + "' was not found");
			}

			lexer.nextToken();
			if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
			lexer.nextToken();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			/*
			 * return an object representing the creation of an object
			 */
			// Criar e retornar um literalClass?
			LiteralClass literalClass = new LiteralClass(classObj);
			return literalClass;
			/*
          	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
		case SUPER:
			// "super" "." Id "(" [ ExpressionList ] ")"
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				signalError.showError("'.' expected");
			}
			else
				lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			messageName = lexer.getStringValue();
			/*
			 * para fazer as conferências semânticas, procure por 'messageName'
			 * na superclasse/superclasse da superclasse etc
			 */
			KraClass pointedClass = currentClass;
			
			if(pointedClass.getSuperclass() == null) {
				signalError.showError("'super' used in class '" + currentClass.getName() + "' that does not have a superclass");
			}
			
			Method method = null;
			do {
				KraClass superClass = pointedClass.getSuperclass();
				if( superClass != null) {
					MethodList publicMethod = superClass.getPublicMethodList();
					Iterator<Method> iterator = publicMethod.elements();
					while(iterator.hasNext()) {
						Method tempMethod = iterator.next();
						if(tempMethod.getName().equals(messageName)) {
							
							// TODO: verifica se parametros sao iguais ao do metodo
							
							method = tempMethod;
							break;
						}
					}
				} else {
					signalError.showError("Method '" + messageName + "' was not found in superclass '" + currentClass.getName() + "' or its superclasses");
				}
				pointedClass = superClass;
			} while(method == null);
			
			lexer.nextToken();
			exprList = realParameters();
			
			MessageSendToSuper messageSendToSuper = new MessageSendToSuper(method, exprList);
			return messageSendToSuper;
		case IDENT:
			/*
          	 * PrimaryExpr ::=  
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
			 */

			String firstId = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// Id
				// retorne um objeto da ASA que representa um identificador
				Variable v = (Variable) symbolTable.getInLocal(firstId);
				
				// TODO: Arrumar
				if(v == null) {
					//System.out.println("Variavel Null: " + firstId);
					//signalError.showError("Identifier '" + firstId + "' was not found");
				}
				
				VariableExpr variableExpr = new VariableExpr(v, false);
				return variableExpr;
			}
			else { // Id "."
				lexer.nextToken(); // coma o "."
				if ( lexer.token != Symbol.IDENT ) {
					signalError.showError("Identifier expected");
				}
				else {
					// Id "." Id
					lexer.nextToken();
					id = lexer.getStringValue();
					if ( lexer.token == Symbol.DOT ) {
						// Id "." Id "." Id "(" [ ExpressionList ] ")"
						/*
						 * se o compilador permite variáveis estáticas, é possível
						 * ter esta opção, como
						 *     Clock.currentDay.setDay(12);
						 * Contudo, se variáveis estáticas não estiver nas especificações,
						 * sinalize um erro neste ponto.
						 */
						signalError.showError("Static method using a static variable is not supported");
						
						lexer.nextToken();
						if ( lexer.token != Symbol.IDENT )
							signalError.showError("Identifier expected");
						messageName = lexer.getStringValue();
						lexer.nextToken();
						exprList = this.realParameters();

					}
					else if ( lexer.token == Symbol.LEFTPAR ) {
						// Id "." Id "(" [ ExpressionList ] ")"
						exprList = this.realParameters();
						/*
						 * para fazer as conferências semânticas, procure por
						 * método 'ident' na classe de 'firstId'
						 */
						
						// firstId = Classe 
						// id = method
						Variable variable = symbolTable.getInLocal(firstId);
						if( variable == null ) {
							// Faz algo que não sabemos
							signalError.showError("Unknown variable");
						}
						
						// TODO: verificar se initialClass é realmente uma classe, pois ele poderia ser um int, boolean, string
						if(variable.getType() == Type.booleanType || variable.getType() == Type.intType || variable.getType() == Type.stringType || variable.getType() == Type.voidType ) {
							signalError.showError("Message send to a non-object receiver");
						}
						
						KraClass variableClass = (KraClass) variable.getType();

						Method method2 = null;
						
						// Metodo corrente
						if( variableClass == currentClass && currentMethod.getName().equals(id)) {
							//System.out.println("Method calling itself with a class variable!");
							method2 = currentMethod;
						}
						
						KraClass pointedClass2 = variableClass;
						do {
							if( pointedClass2 != null) {
								MethodList publicMethod = pointedClass2.getPublicMethodList();
								Iterator<Method> iterator = publicMethod.elements();
								while(iterator.hasNext()) {
									Method tempMethod = iterator.next();
									if(tempMethod.getName().equals(id)) {
										
										// TODO: verifica se parametros sao iguais ao do metodo
										
										method2 = tempMethod;
										break;
									}
								}
							} else {
								signalError.showError("Method '" + id + "' was not found in class '" + variableClass.getName() + "' or its superclasses");
							}
							pointedClass2 = pointedClass2.getSuperclass();
						} while(method2 == null);
						
						MessageSendToVariable messageSendToVariable = new MessageSendToVariable(variable, method2, exprList);
						return messageSendToVariable;
					}
					else {
						// retorne o objeto da ASA que representa Id "." Id
						signalError.showError("Static variable is not supported");
					}
				}
			}
			break;
		case THIS:
			/*
			 * Este 'case THIS:' trata os seguintes casos: 
          	 * PrimaryExpr ::= 
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// only 'this'
				// retorne um objeto da ASA que representa 'this'
				// confira se não estamos em um método estático
				return null; // TODO: Descobrir o que retornar?
			}
			else {
				lexer.nextToken();
				if ( lexer.token != Symbol.IDENT )
					signalError.showError("Identifier expected");
				id = lexer.getStringValue();
				lexer.nextToken();
				// já analisou "this" "." Id
				if ( lexer.token == Symbol.LEFTPAR ) {
					// "this" "." Id "(" [ ExpressionList ] ")"
					/*
					 * Confira se a classe corrente possui um método cujo nome é
					 * 'ident' e que pode tomar os parâmetros de ExpressionList
					 */
					exprList = this.realParameters();
					
					KraClass pointedClass3 = currentClass;
					
					Method method3 = null;
					
					// Metodo corrente
					if(currentMethod.getName().equals(id)) {
						method3 = currentMethod;
					}
					
					do {
						if( pointedClass3 != null) {
							
							// Metodos privados
							MethodList privateMethod = pointedClass3.getPrivateMethodList();
							Iterator<Method> iterator = privateMethod.elements();
							while(iterator.hasNext()) {
								Method tempMethod = iterator.next();
								//System.out.println("Metodos privados: " + tempMethod.getName());
								if(tempMethod.getName().equals(id)) {
									
									// TODO: verifica se parametros sao iguais ao do metodo
									
									method3 = tempMethod;
									break;
								}
							}
							
							// Metodos publicos
							MethodList publicMethod = pointedClass3.getPublicMethodList();
							iterator = publicMethod.elements();
							while(iterator.hasNext()) {
								Method tempMethod = iterator.next();
								//System.out.println("Metodos publicos: " + tempMethod.getName());
								if(tempMethod.getName().equals(id)) {
									
									// TODO: verifica se parametros sao iguais ao do metodo
									
									method3 = tempMethod;
									break;
								}
							}
						} else {
							signalError.showError("Method '" + id + "' was not found in class '" + currentClass.getName() + "' or its superclasses");
						}
						pointedClass3 = pointedClass3.getSuperclass();
					} while(method3 == null);
					
					MessageSendToSelf messageSendToSelf = new MessageSendToSelf(method3, exprList);
					return messageSendToSelf;
				}
				else if ( lexer.token == Symbol.DOT ) {
					// "this" "." Id "." Id "(" [ ExpressionList ] ")"
					signalError.showError("Static method using a static variable is not supported");
					
					lexer.nextToken();
					if ( lexer.token != Symbol.IDENT )
						signalError.showError("Identifier expected");
					lexer.nextToken();
					exprList = this.realParameters();
				}
				else {
					// retorne o objeto da ASA que representa "this" "." Id
					/*
					 * confira se a classe corrente realmente possui uma
					 * variável de instância 'ident'
					 */
					
					InstanceVariable variavel = null;
					Iterator<InstanceVariable> iter = currentClass.getInstanceVariableList().elements();
					while(iter.hasNext()) {
						InstanceVariable tempVar = iter.next();
						if(tempVar.getName().equals(id)) {
							variavel = tempVar;
						}
					}
					
					VariableExpr variableExpr = new VariableExpr(variavel, true);
					return variableExpr;
				}
			}
			break;
		default:
			signalError.showError("Expression expected");
		}
		return null;
	}

	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}
	
	public boolean isSubType(KraClass left, KraClass right) {
		
		KraClass superClassRight = right.getSuperclass();
		while(superClassRight != null) {
			if(superClassRight == left) {
				return true;
			}
			superClassRight = superClassRight.getSuperclass();
		}
		
		return false;
	}

	private static boolean startExpr(Symbol token) {

		return token == Symbol.FALSE || token == Symbol.TRUE
				|| token == Symbol.NOT || token == Symbol.THIS
				|| token == Symbol.LITERALINT || token == Symbol.SUPER
				|| token == Symbol.LEFTPAR || token == Symbol.NULL
				|| token == Symbol.IDENT || token == Symbol.LITERALSTRING;

	}

	private Method			currentMethod;
	private KraClass		currentClass;
	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;

}
