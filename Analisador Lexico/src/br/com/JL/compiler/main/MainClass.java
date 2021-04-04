package br.com.JL.compiler.main;

import br.com.JL.compiler.exceptions.JLLexicalException;
import br.com.JL.compiler.lexico.JLScanner;
import br.com.JL.compiler.lexico.Token;

public class MainClass {
	public static void main (String[]args) {
		try {
			JLScanner sc = new JLScanner("input.jvlal");
			Token token;
		
			do {
				token = sc.nextToken();
				if(token != null) {
					System.out.println(token);
				}
			}while(token != null);
		} catch(JLLexicalException ex) {
			System.out.println("Lexical ERROR " + ex.getMessage());
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}

}