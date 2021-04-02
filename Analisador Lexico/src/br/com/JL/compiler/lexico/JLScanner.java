package br.com.JL.compiler.lexico;

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
	
	public JLScanner(String filename) {
		try {
			line = 1;
			column = 1;
			String txtConteudo;
			txtConteudo = new String(Files.readAllBytes(Paths.get(filename)),StandardCharsets.UTF_8);
			System.out.println("DEBUG --------------------");
			System.out.println(txtConteudo);
			System.out.println("-----------------------------");
			content = txtConteudo.toCharArray();
			pos = 0;
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Token nextToken() {
		char currentChar;
		String term = "";
		Token token;
		if (isEOF()) {
			return null;
		}
		
		estado = 0;
		
		while(true) {
			column ++;
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
				else if(isAtr(currentChar)) {
					term += currentChar;
					estado = 5;
				}
				else if(isParOn(currentChar)) {
					term += currentChar;
					estado = 6;
				}
				else if(isParOff(currentChar)) {
					term += currentChar;
					estado = 7;
				}
				else if(isSep(currentChar)) {
					term += currentChar;
					estado = 8;
				}
				else if(isSemicolon(currentChar)) {
					term += currentChar;
					estado = 9;
				}
				else if(isBracesOn(currentChar)) {
					term += currentChar;
					estado = 10;
				}
				else if(isBracesOff(currentChar)) {
					term += currentChar;
					estado = 11;
				}
				else if(isQuote(currentChar)) {
					term += currentChar;
					estado = 14;
				}
				else if(isApostrophe(currentChar)) {
					term += currentChar;
					estado = 16;
				}
				else if(isNot(currentChar)) {
					term += currentChar;
					estado = 19;
				}
				else if(isAd(currentChar)) {
					term += currentChar;
					estado = 21;
				}
				else if(isSub(currentChar)) {
					term += currentChar;
					estado = 22;
				}
				else if(isMult(currentChar)) {
					term += currentChar;
					estado = 23;
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
				if(isPoint(currentChar) && isDigit(temp)) {
					term += currentChar;
					estado = 12;
				}else if(!isLetter(currentChar) && !isPoint(currentChar)) {
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
				if (isAtr(currentChar)) {
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
				else if(!isLetter(currentChar) && !isPoint(currentChar)) {
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
				if (isQuote(currentChar)) {
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
				} else if (isApostrophe(currentChar)) {
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
				if (isAtr(currentChar)) {
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
			}
		}
	}
	
	private boolean isMult(char c) {
		return c == '*';
	}
	
	private boolean isSub(char c) {
		return c == '-';
	}
	
	private boolean isAd(char c) {
		return c == '+';
	}
	
	private boolean isNot(char c) {
		return c == '!';
	}
	private boolean isAtr(char c) {
		return c == '=';
	}
	private boolean isApostrophe(char c) {
		return c == '\'';
	}
	
	private boolean isQuote(char c) {
		return c == '"';
	}
	
	private boolean isPoint(char c) {
		return c =='.';
	}
	
	private boolean isTerminal(char c) {
		return c =='}' || c == ';' || c == '{' || c == '(' || c == ')' || c == ',';
	}
	private boolean isBracesOff(char c) {
		return c =='}';
	}
	
	private boolean isBracesOn(char c) {
		return c == '{';
	}
	
	private boolean isSemicolon(char c) {
		return c == ';';
	}
	
	private boolean isParOn(char c) {
		return c == '(';
	}
	
	private boolean isParOff(char c) {
		return c == ')';
	}
	
	private boolean isSep(char c) {
		return c == ',';
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c<= '9';
	}
	
	private boolean isLetter(char c) {
		return (c>= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}
	
	private boolean isOperator(char c) {
		return c == '>' || c == '<' || c == '=' || c =='!' || c == '+' || c == '-' || c == '*';
	}
	
	private boolean isSpace(char c) {
		if (c == '\n' || c== '\r') {
			line ++;
			column = 0;
		}
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
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
