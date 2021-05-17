package src.main;

import src.sintatico.Sintatico;
import src.exceptions.JLLexicalException;
import src.lexico.JLScanner;
import src.lexico.Token;

public class MainClass {
	public static void main (String[]args) {
		Sintatico analiSintatico = new Sintatico("input.jvlal");
//		try {
//			JLScanner sc = new JLScanner("input.jvlal");
//			Token token;
//			do {
//				token = sc.nextToken();
//				if(token != null) {
//					System.out.println(token);
//				}
//			}while(token != null);
//		} catch(JLLexicalException ex) {
//			System.out.println("Lexical ERROR " + ex.getMessage());
//		}catch(Exception ex) {
//			System.out.println(ex);
//		}
	}

}
