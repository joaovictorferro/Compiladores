package br.com.JL.compiler.lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import br.com.JL.compiler.exceptions.JLLexicalException;
import br.com.JL.compiler.lexico.Lexeme;

public class JLScanner {
	private char[] content;
	private int estado;
	private int pos;
	private int line;
	private int column;
	private String term = "";
	private BufferedReader bufferedReader;
	String txtConteudo = "";
	
	public JLScanner(String filename) {
		try {
			line = 1;
			column = 1;
			this.bufferedReader = new BufferedReader(new FileReader(new File(filename)));
			hasNextLine();
			txtConteudo+= " ";
			content = txtConteudo.toCharArray();
			//System.out.println(content.length);
			pos = 0;
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Token nextToken() {
		char currentChar;
		term = "";
		Token token;
		//System.out.println(content.length);
		if (isEOF()) {
			if (hasNextLine()) {
				txtConteudo+= " ";
				content = txtConteudo.toCharArray();
				pos = 0;
			}else {
				System.out.println("sai");
				return null;
			}
		}
		
		estado = 0;
		
		while(true) {
			column ++;
			if (isEOF()) {
				if (hasNextLine()) {
					txtConteudo+= " ";
					content = txtConteudo.toCharArray();
					pos = 0;
				}else {
					System.out.println("sai");
					return null;
				}
			}			
			currentChar = nextChar();
//			System.out.println(currentChar);
//			System.out.println(pos);
			
			switch(estado) {
			case 0:
				if(isLetter(currentChar)) {
					term += currentChar;
					estado = 1;
				}
				else if(isDigit(currentChar)) {
					term += currentChar;
					estado = 3;
				}
				else if(isSpace(currentChar)) {
					estado = 0;
				}
				else if(currentChar == '=') {
					term += currentChar;
					estado = 5;
				}
				else if(currentChar == '(') {
					term += currentChar;
					estado = 6;
				}
				else if(currentChar == ')') {
					term += currentChar;
					estado = 7;
				}
				else if(currentChar == ',') {
					term += currentChar;
					estado = 8;
				}
				else if(currentChar == ';') {
					term += currentChar;
					estado = 9;
				}
				else if(currentChar == '{') {
					term += currentChar;
					estado = 10;
				}
				else if(currentChar == '}') {
					term += currentChar;
					estado = 11;
				}
				else if(currentChar == '"') {
					term += currentChar;
					estado = 14;
				}
				else if(currentChar == '\'') {
					term += currentChar;
					estado = 16;
				}
				else if(currentChar == '!') {
					term += currentChar;
					estado = 19;
				}
				else if(currentChar == '+') {
					term += currentChar;
					estado = 21;
				}
				else if(currentChar == '-') {
					term += currentChar;
					estado = 22;
				}
				else if(currentChar == '*') {
					term += currentChar;
					estado = 23;
				}
				else if(currentChar == '/'){
						term+= currentChar;
						estado = 24;
				}
				else if(currentChar == '>') {
					term+= currentChar;
					estado = 26;
				}
				else if(currentChar == '<') {
					term += currentChar;
					estado = 28;
				}
				else if(currentChar == '&') {
					term += currentChar;
					estado = 30;
				}
				else if(currentChar == '|') {
					term += currentChar;
					estado = 31;
				}
				else if(currentChar == '%') {
					term += currentChar;
					estado = 32;
				}
				else if(currentChar == '#') {
					term += currentChar;
					estado = 33;
				}
				else {
					throw new JLLexicalException("Unrecognized SYMBOL");
				}
				break;
			case 1:
				if(isLetter(currentChar) || isDigit(currentChar)) {
					estado = 1;
					term += currentChar;
				}
				else if(isSpace(currentChar) || isTerminal(currentChar) || isOperator(currentChar)){
					estado = 2;
					back();
				}
				else {
					throw new JLLexicalException("Malformed Identifier");
				}
				break;
			case 2:
				back();
				if(LexemeTable.tokenMapping.get(term) != null){
					token = new Token();
					token.setType(LexemeTable.tokenMapping.get(term));
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}else {
					token = new Token();
					token.setType(Lexeme.ID);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}
			case 3:
				if (isDigit(currentChar)) {
					term += currentChar;
					estado = 3;
					break;
				}
				char temp = nextChar();
				back();
				if(currentChar == '.' && isDigit(temp)) {
					term += currentChar;
					estado = 12;
				}else if(!isLetter(currentChar) && currentChar != '.') {
					back();
					estado = 4;
				
				}
				else {
					throw new JLLexicalException("Unrecognized NUMBER");
				}
				break;
			case 4:
				token = new Token();
				token.setType(Lexeme.CT_INT);
				token.setText(term);
				back();
				return token;
			case 5:
				if (currentChar == '=') {
					term += currentChar;
					estado = 18;
					break;
				}else if(isOperator(currentChar)) {
					throw new JLLexicalException("Malformed Operator\n");
				} else {
					token = new Token();
					token.setType(Lexeme.OP_ATR);
					token.setText(term);
					back();
					return token;
				}
			case 6:
				token = new Token();
				token.setType(Lexeme.ON_PAR);
				token.setLine(line);
				token.setColumn(column - term.length());
				token.setText(term);
				back();
				return token;
			case 7:
				token = new Token();
				token.setType(Lexeme.OFF_PAR);
				token.setLine(line);
				token.setColumn(column - term.length());
				token.setText(term);
				back();
				return token;
			case 8:
				token = new Token();
				token.setType(Lexeme.SEP);
				token.setLine(line);
				token.setColumn(column - term.length());
				token.setText(term);
				back();
				return token;
			case 9:
				token = new Token();
				token.setType(Lexeme.SEMICOLON);
				token.setLine(line);
				token.setColumn(column - term.length());
				token.setText(term);
				back();
				return token;
			case 10:
				token = new Token();
				token.setType(Lexeme.ON_BRACE);
				token.setLine(line);
				token.setColumn(column - term.length());
				token.setText(term);
				back();
				return token;
			case 11:
				token = new Token();
				token.setType(Lexeme.OFF_BRACE);
				token.setLine(line);
				token.setColumn(column - term.length());
				token.setText(term);
				back();
				return token;
			case 12:
				if (isDigit(currentChar)) {
					term += currentChar;
					estado = 12;
				}
				else if(!isLetter(currentChar) && currentChar != '.') {
					back();
					estado = 13;
				}
				else {
					throw new JLLexicalException("Unrecognized NUMBER");
				}
				break;
			case 13:
				token = new Token();
				token.setType(Lexeme.CT_FLOAT);
				token.setText(term);
				back();
				return token;	
			case 14:
				term += currentChar;
				if (currentChar == '"') {
					estado = 15;
				}
				break;
			case 15:
				token = new Token();
				token.setType(Lexeme.CT_STRING);
				token.setText(term);
				back();
				return token;
			case 16:
				term += currentChar;
				if (term.length() > 3){
					throw new JLLexicalException("Malformed character\n");
				} else if (currentChar == '\'') {
					estado = 17;
				}
				break;
			case 17:
				token = new Token();
				token.setType(Lexeme.CT_CHAR);
				token.setText(term);
				back();
				return token;
			case 18:
				if (isOperator(currentChar)) {
					throw new JLLexicalException("Malformed Operator\n");
				} else {
					token = new Token();
					token.setType(Lexeme.OP_REL);
					token.setText(term);
					back();
					return token;
				}
			case 19:
				if (currentChar == '=') {
					term += currentChar;
					estado = 20;
					break;
				}else if(isOperator(currentChar)) {
					throw new JLLexicalException("Malformed Operator\n");
				} else {
					token = new Token();
					token.setType(Lexeme.OP_NOT);
					token.setText(term);
					back();
					return token;
				}
			case 20:
				if (isOperator(currentChar)) {
					throw new JLLexicalException("Malformed Operator\n");
				} else {
					token = new Token();
					token.setType(Lexeme.OP_RELNOT);
					token.setText(term);
					back();
					return token;
				}
			case 21:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_AD);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			case 22:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_SUB);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			case 23:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_MULT);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			case 24:
				if (currentChar == '/') {
					term += currentChar;
					estado = 25;
					break;
				}else if(isOperator(currentChar)) {
					throw new JLLexicalException("Malformed Operator\n");
				} else {
					token = new Token();
					token.setType(Lexeme.OP_DIV);
					token.setText(term);
					back();
					return token;
				}
			case 25:
				if(currentChar == '\n') {
					term = "";
					estado = 0;
				}
				break;
			case 26:
				if (currentChar == '=') {
					term += currentChar;
					estado = 27;
					break;
				}else if(isOperator(currentChar)) {
					throw new JLLexicalException("Malformed Operator\n");
				} else {
					token = new Token();
					token.setType(Lexeme.OP_GREATER);
					token.setText(term);
					back();
					return token;
				}
			case 27:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_GREATEREQ);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			case 28:
				if (currentChar == '=') {
					term += currentChar;
					estado = 29;
					break;
				}else if(isOperator(currentChar)) {
					throw new JLLexicalException("Malformed Operator\n");
				} else {
					token = new Token();
					token.setType(Lexeme.OP_LESS);
					token.setText(term);
					back();
					return token;
				}
			case 29:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_LESSEQ);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			case 30:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_AND);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			case 31:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_OR);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			case 32:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_MOD);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			case 33:
				if (!isOperator(currentChar)) {
					token = new Token();
					token.setType(Lexeme.OP_CONC);
					token.setText(term);
					back();
					return token;
				}else {
					throw new JLLexicalException("Malformed Operator\n");
				}
			}
		}
	}
	
	private boolean isTerminal(char c) {
		return c =='}' || c == ';' || c == '{' || c == '(' || c == ')' || c == ',';
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c<= '9';
	}
	
	private boolean isLetter(char c) {
		return (c>= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}
	
	private boolean isOperator(char c) {
		return c == '>' || c == '<' || c == '=' || c =='!' || c == '+' || c == '-' || c == '*' || c =='/' || c == '&' 
				|| c == '|' || c =='%' || c =='#';
	}
	
	private boolean isSpace(char c) {
		if (c == '\n' || c== '\r') {
			line ++;
			column = 0;
		}
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	
	public boolean hasNextLine() {
		String s = new String();
		try {
			s = bufferedReader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (s != null) {
			txtConteudo = s;
			++line;
			return true;
		}
		return false;
	}
	
	private char nextChar() {
			return content[pos++];
	}
	
	private boolean isEOF() {
		return pos == content.length;
	}
	
	private void back() {
		pos--;
	}
}
