package br.com.JL.compiler.lexico;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import br.com.JL.compiler.exceptions.JLLexicalException;

public class JLScanner {
	private char[] content;
	private int estado;
	private int pos;
	private int line;
	private int column;
	
	public JLScanner(String filename) {
		try {
			line = 1;
			column = 0;
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
			
			currentChar = nextChar();
			
			switch(estado) {
			case 0:
				if(isChar(currentChar)) {
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
				else if(isOperator(currentChar)) {
					estado = 5;
				}
				else {
					throw new JLLexicalException("Unrecognized SYMBOL");
				}
				break;
			case 1:
				if(isChar(currentChar) || isDigit(currentChar)) {
					estado = 1;
					term += currentChar;
				}
				else if(isSpace(currentChar) || isOperator(currentChar)){
					estado = 2;
				}
				else {
					throw new JLLexicalException("Malformed Identifier");
				}
				break;
			case 2:
				back();
				// so adicionar o if
				token = new Token();
				token.setType(Token.TK_IDENTIFIER);
				token.setText(term);
				token.setLine(line);
				token.setColumn(column - term.length());
				return token;
			case 3:
				if (isDigit(currentChar)) {
					term += currentChar;
					estado = 3;
				}
				else if(!isChar(currentChar)) {
					estado = 4;
				}
				else {
					throw new JLLexicalException("Unrecognized NUMBER");
				}
				break;
			case 4:
				token = new Token();
				token.setType(Token.TK_NUMBER);
				token.setText(term);
				back();
				return token;
				
			}
		}
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c<= '9';
	}
	
	private boolean isChar(char c) {
		return (c>= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}
	
	private boolean isOperator(char c) {
		return c == '>' || c == '<' || c == '=' || c =='!';
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
