package src.lexico;

import java.util.HashMap;
import java.util.Map;

public class reservedWord {
	public static Map<String, Lexeme> tokenMapping = new HashMap<String, Lexeme>();
	
	static {
		tokenMapping.put("main", Lexeme.RW_MAIN);
		tokenMapping.put("proc", Lexeme.RW_PROC);
		tokenMapping.put("fun", Lexeme.RW_FUN);
		tokenMapping.put("if", Lexeme.RW_IF);
		tokenMapping.put("else",Lexeme.RW_ELSE);
		tokenMapping.put("while",Lexeme.RW_WHILE);
		tokenMapping.put("for",Lexeme.RW_FOR);
		tokenMapping.put("float",Lexeme.RW_FLOAT);
		tokenMapping.put("int",Lexeme.RW_INT);
		tokenMapping.put("char",Lexeme.RW_CHAR);
		tokenMapping.put("string",Lexeme.RW_STRING);
		tokenMapping.put("read",Lexeme.RW_READ);
		tokenMapping.put("print",Lexeme.RW_PRINT);
		tokenMapping.put("bool",Lexeme.RW_BOOL);
		tokenMapping.put("void",Lexeme.RW_VOID);
		tokenMapping.put("True",Lexeme.RW_TRUE);
		tokenMapping.put("False",Lexeme.RW_FALSE);
		tokenMapping.put("toString",Lexeme.RW_TOSTRING);
		tokenMapping.put("Null",Lexeme.RW_NULL);
		tokenMapping.put("return",Lexeme.RW_RETURN);
		tokenMapping.put("println",Lexeme.RW_PRINTLN);
		tokenMapping.put("length",Lexeme.RW_LENGTH);
	}
}
