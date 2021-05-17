package src.lexico;

public class Token {

	private String text;
	private Lexeme tokenCategory;
	private int    line;
	private int    column;
	
	public Token(Lexeme tokenCategory, String text, int line, int column) {
		this.line = line;
		this.column = column;
		this.tokenCategory = tokenCategory;
		this.text = text;
	}
	
	public Token() {
		super();
	}
	
	public Lexeme getTokenCategory() {
		return tokenCategory;
	}

	public void setTokenCategory(Lexeme id) {
		this.tokenCategory = id;
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
		return String.format(format, line-1, column, tokenCategory.ordinal(), tokenCategory.toString(), text);
	}

}
