package br.com.JL.compiler.lexico;

public class Token {
	public static final int ID  = 0;
	public static final int CT_FLOAT = 1;
	public static final int CT_INT = 2;
	public static final int CT_CHAR= 3;
	public static final int CT_STRING = 4;
	public static final int OP_ATR = 5;
	public static final int OP_REL = 6;
	public static final int OP_RELNOT = 7;
	public static final int OP_AD = 8;
	public static final int OP_SUB = 9;
	public static final int OP_MULT = 10;
	public static final int OP_DIV = 11;
	public static final int OP_MOD = 12;
	public static final int OP_GREATER = 13;
	public static final int OP_LESS = 14;
	public static final int OP_GREATEREQ = 15;
	public static final int OP_LESSEQ = 16;
	public static final int OP_NOT = 17;
	public static final int OP_NEGUN = 18;
	public static final int OP_POSUN = 19;
	public static final int OP_AND = 20;
	public static final int OP_OR = 21;
	public static final int OP_CONC = 22;
	public static final int COMMENT= 23;
	public static final int RW_FUNCTION = 24;
	public static final int RW_RETURN = 25;
	public static final int RW_IF = 26;
	public static final int RW_ELSE = 27;
	public static final int RW_WHILE = 28;
	public static final int RW_FOR = 29;
	public static final int RW_INT= 30;
	public static final int RW_FLOAT =31;
	public static final int RW_STRING = 32;
	public static final int RW_TOSTRING =33;
	public static final int RW_CHAR = 34;
	public static final int RW_BOOL = 35;
	public static final int RW_READ = 36;
	public static final int RW_PRINT = 37;
	public static final int RW_TRUE = 38;
	public static final int RW_FALSE = 39;
	public static final int RW_NULL = 40;
	public static final int ON_PAR = 41;
	public static final int OFF_PAR = 42;
	public static final int ON_BRACE = 43;
	public static final int OFF_BRACE = 44;
	public static final int QUOTE = 45;
	public static final int APOSTROPHE = 46;
	public static final int RW_PROC = 47;
	public static final int RW_MAIN = 48;
	public static final int TERMINAL= 49;
	public static final int SEP = 50;
	
	private int type;
	private String text;
	private Lexeme lexeme;
	private int    line;
	private int    column;
	
	public Token(int type, String text) {
		super();
		this.type = type;
		this.text = text;
	}
	
	public Token() {
		super();
	}
	
	public int getType() {
		return type;
	}

	public void setType(int id) {
		this.type = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	
	@Override
	public String toString() {
		return "Token [type = " + type + ", text=" + text + "]" ;
	}

}
