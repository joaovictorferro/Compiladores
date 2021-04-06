package src.lexico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import src.exceptions.JLLexicalException;
import src.lexico.Lexeme;

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
			nextLine();
			content = txtConteudo.toCharArray();
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Token nextToken() {
		char currentChar;
		term = "";
		Token token;
		estado = 0;
		
		while(true) {
			
			if (isEOF()) {
				if (nextLine()) {
					content = txtConteudo.toCharArray();
				}else {
					return null;
				}
			}		
			
			currentChar = nextChar();
			
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
				else if(currentChar == ':') {
					term+= currentChar;
					estado = 34;
				}
				else {
					token = new Token(Lexeme.UN_SYMBOL,term,line,column - term.length());
					return token;
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
					token = new Token(Lexeme.UN_ID,term,line,column - term.length());
					return token;
				}
				break;
			case 2:
				back();
				if(reservedWord.tokenMapping.get(term) != null){
					token = new Token(reservedWord.tokenMapping.get(term),term,line,column - term.length());
					return token;
				}else {
					token = new Token(Lexeme.ID,term,line,column - term.length());
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
					token = new Token(Lexeme.UN_NUMBER,term,line,column - term.length());
					return token;
				}
				break;
			case 4:
				back();
				token = new Token(Lexeme.CT_INT,term,line,column - term.length());
				return token;
			case 5:
				if (currentChar == '=') {
					term += currentChar;
					estado = 18;
					break;
				}else if(isOperator(currentChar)) {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				} else {
					back();
					token = new Token(Lexeme.OP_ATR,term,line,column - term.length());
					return token;
				}
			case 6:
				back();
				token = new Token();
				token = new Token(Lexeme.ON_PAR,term,line,column - term.length());
				return token;
			case 7:
				back();
				token = new Token(Lexeme.OFF_PAR,term,line,column - term.length());
				return token;
			case 8:
				back();
				token = new Token(Lexeme.SEP,term,line,column - term.length());
				return token;
			case 9:
				back();
				token = new Token(Lexeme.SEMICOLON,term,line,column - term.length());
				return token;
			case 10:
				back();
				token = new Token(Lexeme.ON_BRACE,term,line,column - term.length());
				return token;
			case 11:
				back();
				token = new Token(Lexeme.OFF_BRACE,term,line,column - term.length());
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
					token = new Token(Lexeme.UN_NUMBER,term,line,column - term.length());
					return token;
				}
				break;
			case 13:
				back();
				token = new Token(Lexeme.CT_FLOAT,term,line,column - term.length());
				return token;	
			case 14:
				term += currentChar;
				if (currentChar == '"') {
					estado = 15;
				}
				break;
			case 15:
				back();
				token = new Token(Lexeme.CT_STRING,term,line,column - term.length());
				return token;
			case 16:
				term += currentChar;
				if (term.length() > 3){
					token = new Token(Lexeme.UN_CHAR,term,line,column - term.length());
					return token;
				} else if (currentChar == '\'') {
					estado = 17;
				}
				break;
			case 17:
				back();
				token = new Token(Lexeme.CT_CHAR,term,line,column - term.length());
				return token;
			case 18:
				if (isOperator(currentChar)) {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				} else {
					back();
					token = new Token(Lexeme.OP_REL,term,line,column - term.length());
					return token;
				}
			case 19:
				if (currentChar == '=') {
					term += currentChar;
					estado = 20;
					break;
				}else if(isOperator(currentChar)) {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				} else {
					back();
					token = new Token(Lexeme.OP_NOT,term,line,column - term.length());
					return token;
				}
			case 20:
				if (isOperator(currentChar)) {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				} else {
					back();
					token = new Token(Lexeme.OP_RELNOT,term,line,column - term.length());
					return token;
				}
			case 21:
				if (!isOperator(currentChar)) {
					back();
					token = new Token(Lexeme.OP_AD,term,line,column - term.length());
					return token;
				}else {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				}
			case 22:
				if (!isOperator(currentChar)) {
					back();
					token = new Token(Lexeme.OP_SUB,term,line,column - term.length());
					return token;
				}else {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				}
			case 23:
				if (!isOperator(currentChar)) {
					back();
					token = new Token(Lexeme.OP_MULT,term,line,column - term.length());
					return token;
				}else {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				}
			case 24:
				if (currentChar == '/') {
					term += currentChar;
					estado = 25;
					break;
				}else if(isOperator(currentChar)) {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				} else {
					back();
					token = new Token(Lexeme.OP_DIV,term,line,column - term.length());
					return token;
				}
			case 25:
				estado = 0;
				term = "";
				if (nextLine()) {
                    content = txtConteudo.toCharArray();
                }else {
                    System.out.println("sai");
                    return null;
                }
				break;
			case 26:
				if (currentChar == '=') {
					term += currentChar;
					estado = 27;
					break;
				}else if(isOperator(currentChar)) {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				} else {
					back();
					token = new Token(Lexeme.OP_GREATER,term,line,column - term.length());
					return token;
				}
			case 27:
				if (!isOperator(currentChar)) {
					back();
					token = new Token(Lexeme.OP_GRTEREQ,term,line,column - term.length());
					return token;
				}else {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				}
			case 28:
				if (currentChar == '=') {
					term += currentChar;
					estado = 29;
					break;
				}else if(isOperator(currentChar)) {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				} else {
					back();
					token = new Token(Lexeme.OP_LESS,term,line,column - term.length());
					return token;
				}
			case 29:
				if (!isOperator(currentChar)) {
					back();
					token = new Token(Lexeme.OP_LESSEQ,term,line,column - term.length());
					return token;
				}else {
					token = new Token(Lexeme.UN_OP,term,line,column - term.length());
					return token;
				}
			case 30:
				back();
				token = new Token(Lexeme.OP_AND,term,line,column - term.length());
				return token;
			case 31:
				back();
				token = new Token(Lexeme.OP_OR,term,line,column - term.length());
				return token;
			case 32:
				back();
				token = new Token(Lexeme.OP_MOD,term,line,column - term.length());
				return token;
			case 33:
				back();
				token = new Token(Lexeme.OP_CONC,term,line,column - term.length());
				return token;
			case 34:
				back();
				token = new Token(Lexeme.COLON,term,line,column - term.length());
				return token;
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
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	
	public boolean nextLine() {
		String s = new String();
		try {
			s = bufferedReader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (s != null) {
			txtConteudo = s;
			printCodeLine(s);
			txtConteudo+= " ";
			++line;
			pos = 0;
			column = 1;
			return true;
		}
		return false;
	}
	
	public void printCodeLine(String content) {
		String format = "%4d  %s";
		System.out.println(String.format(format, line, content));
	}
	
	private char nextChar() {
			column++;
			return content[pos++];
	}
	
	private boolean isEOF() {
		return pos == content.length;
	}
	
	private void back() {
		column --;
		pos--;
	}
}
