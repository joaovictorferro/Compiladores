package src.lexico;

public class Token {

	//private int type;
	private String text;
	private Lexeme type;
	private int    line;
	private int    column;
	
	public Token(Lexeme type, String text, int line, int column) {
		//super();
		this.line = line;
		this.column = column;
		this.type = type;
		this.text = text;
	}
	
	public Token() {
		super();
	}
	
	public Lexeme getType() {
		return type;
	}

	public void setType(Lexeme id) {
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
		String format = "              [%04d, %04d] (%04d, %20s) {%s}";
		return String.format(format, line-1, column, type.ordinal(), type.toString(), text);
	}

}
