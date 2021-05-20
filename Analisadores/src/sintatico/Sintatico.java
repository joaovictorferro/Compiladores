package src.sintatico;

import java.io.IOException;

import src.lexico.JLScanner;
import src.lexico.Lexeme;
import src.lexico.Token;

public class Sintatico {
	private JLScanner lexicalScanner;
	private Token currentToken;
	private int scopeCounter = 0;
	private String epsilon = "&";
	
	public Sintatico(String a) {
			lexicalScanner = new JLScanner(a);
			setNextToken ();
			S();
	}
	
	public void setNextToken() {
			currentToken = lexicalScanner.nextToken();
			if(currentToken.getTokenCategory() == Lexeme.EOF) {
				return ;
			}
	}
	
	public boolean checkCategory(Lexeme... categories) {
		for (Lexeme category: categories) {
			if (currentToken.getTokenCategory() == category) {
				return true;
			}
		}
		return false;
	}
	
	public void printProduction(String left, String right) {
		String format = "%10s%s = %s";
		System.out.println(String.format(format, "", left, right));
	}
	
	public void S() {
		if (checkCategory(Lexeme.CT_VAR, Lexeme.RW_INT, Lexeme.RW_FLOAT, Lexeme.RW_BOOL, Lexeme.RW_CHAR, Lexeme.RW_STRING)) {
			printProduction("S", "DeclaId S");
			DeclaId();
			S();
		} else if (checkCategory(Lexeme.RW_FUN)) {
			printProduction("S", "FunDecla S");
			FunDecla();
			S();
		} else if (checkCategory(Lexeme.RW_PROC)) {
			printProduction("S", "ProcDecla S");
			ProcDecla();
			S();
		} else {
			printProduction("S", epsilon);
		}
	}
	
	public void DeclaId() {
		if (checkCategory(Lexeme.RW_INT, Lexeme.RW_FLOAT, Lexeme.RW_BOOL, Lexeme.RW_CHAR, Lexeme.RW_STRING)) {
			printProduction("DeclaId", "Type LId ';'");
			Type();
			LId();
			if (!checkCategory(Lexeme.SEMICOLON)){
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
		} else if (checkCategory(Lexeme.CT_VAR)) {
			printProduction("DeclaId", "'const' Type LId ';'");
			System.out.println(currentToken);
			setNextToken();
			Type();
			LId();
			if (checkCategory(Lexeme.SEMICOLON)) {
				System.out.println(currentToken);
				setNextToken();
			}
		}
	}
	
	public void Type() {
		if (checkCategory(Lexeme.RW_INT)) {
			printProduction("Type", "'int'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.RW_FLOAT)) {
			printProduction("Type", "'float'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.RW_CHAR)) {
			printProduction("Type", "'bool'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.RW_CHAR)) {
			printProduction("Type", "'char'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.RW_STRING)) {
			printProduction("Type", "'string'");
			System.out.println(currentToken);
			setNextToken();
		}
	}

	public void LId() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("LId", "Id AtriOpt LIdr");
			Id();
			AtriOpt();
			LIdr();
		}
	}
	
	public void LIdr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("LIdr", "',' Id AtriOpt LIdr");
			System.out.println(currentToken);
			setNextToken();
			Id();
			AtriOpt();
			LIdr();
		} else {
			printProduction("LIdr", epsilon);
		}
	}

	public void Id() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("Id", "'id' ArrayOpt");
			System.out.println(currentToken);
			setNextToken();
			ArrayOpt();
		}
	}
	
	public void AtriOpt() {
		if (checkCategory(Lexeme.OP_ATR)) {
			printProduction("AtriOpt", "'=' Ec");
			System.out.println(currentToken);
			setNextToken();
			Ec();
		} else {
			printProduction("AtriOpt", epsilon);
		}
	}
	
	public void FunDecla() {
		if (checkCategory(Lexeme.RW_FUN)) {
			printProduction("FunDecla", "'fun' Type FunName '(' ParamDecla ')' Body");
			System.out.println(currentToken);
			setNextToken();
			Type();
			FunName();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				ParamDecla();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					Body();
				} 
			}
		}
	}
	
	public void FunName() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("FunName", "'id'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.RW_MAIN)) {
			printProduction("FunName", "'main'");
			System.out.println(currentToken);
			setNextToken();
		}
	}

	public void Param() {
		if (checkCategory(Lexeme.ID, Lexeme.ON_PAR, Lexeme.OP_SUB, Lexeme.CT_BOOL, Lexeme.CT_CHAR, Lexeme.CT_FLOAT, Lexeme.CT_INT, Lexeme.CT_STRING)) {
			printProduction("Param", "Ec Paramr");
			Ec();
			Paramr();
		} else {
			printProduction("Param", epsilon);
		}
	}

	public void Paramr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("Paramr", "',' Ec Paramr");
			System.out.println(currentToken);
			setNextToken();
			Ec();
			Paramr();
		} else {
			printProduction("Paramr", epsilon);
		}
	}
	
	public void ParamDecla() {
		if (checkCategory(Lexeme.RW_BOOL, Lexeme.RW_CHAR, Lexeme.RW_FLOAT, Lexeme.RW_INT, Lexeme.RW_STRING)) {			
			printProduction("ParamDecla", "Type 'id' ArrayOpt ParamDeclar");
			Type();
			if (checkCategory(Lexeme.ID)) {
				System.out.println(currentToken);
				setNextToken();
				ArrayOpt();
				ParamDeclar();
			} 
		}else {
				printProduction("LParamDecl", epsilon);
			}
		}
	
	public void ParamDeclar() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("ParamDeclar", "',' Type 'id' ArrayOpt ParamDeclar");
			System.out.println(currentToken);
			setNextToken();
			Type();
			if (checkCategory(Lexeme.ID)) {
				System.out.println(currentToken);
				setNextToken();
				ArrayOpt();
				ParamDeclar();
			}
		}
	}
	
	public void ArrayOpt() {
		if (checkCategory(Lexeme.ON_BRACKET)) {
			printProduction("ArrayOpt", "'[' Ea ']'");
			System.out.println(currentToken);
			setNextToken();
			Ea();
			if (checkCategory(Lexeme.OFF_BRACKET)) {
				System.out.println(currentToken);
				setNextToken();
			}
		} else {
			printProduction("ArrayOpt", epsilon);
		}
	}

	public void ProcDecla() {
		if (checkCategory(Lexeme.RW_PROC)) {
			printProduction("ProcDecla", "'proc' FunName '(' ParamDecla ')' Body");
			System.out.println(currentToken);
			setNextToken();
			FunName();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				ParamDecla();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					Body();
				} 
			}
		}
	}
	
	public void Body() {
		if (checkCategory(Lexeme.ON_BRACE)) {
			printProduction("Body", "'{' BodyPart '}'");
			System.out.println(currentToken);
			setNextToken();
			BodyPart();
			if (checkCategory(Lexeme.OFF_BRACE)) {
				System.out.println(currentToken);
				setNextToken();
			}
		}
	}
	
	public void BodyPart() {
		if (checkCategory(Lexeme.CT_VAR, Lexeme.RW_INT, Lexeme.RW_FLOAT, Lexeme.RW_BOOL, Lexeme.RW_CHAR, Lexeme.RW_STRING)) {
			printProduction("BodyPart", "DeclaId BodyPart");
			DeclaId();
			BodyPart();
		} else if (checkCategory(Lexeme.RW_PRINT,Lexeme.RW_PRINTLN,Lexeme.RW_READ, Lexeme.RW_WHILE, Lexeme.RW_FOR, Lexeme.RW_IF)) {
			printProduction("BodyPart", "Command BodyPart");
			Command();
			BodyPart();
		} else if (checkCategory(Lexeme.ID)) {
			printProduction("BodyPart", "BodyPartr ';' BodyPart");
			BodyPartr();
			if (checkCategory(Lexeme.SEMICOLON)) {
				System.out.println(currentToken);
				setNextToken();
			}
			BodyPart();
		} else if (checkCategory(Lexeme.RW_RETURN)) {
			printProduction("BodyPart", "'return' Return ';'");
			System.out.println(currentToken);
			setNextToken();
			Return();
			if (checkCategory(Lexeme.SEMICOLON)) {
				System.out.println(currentToken);
				setNextToken();
			}
		} else {
			printProduction("BodyPart", epsilon);
		}
	}
	
	public void BodyPartr() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("BodyPartr", "'id' ParamAtr");
			System.out.println(currentToken);
			setNextToken();
			ParamAtr();
		}
	}
	
	public void ParamAtr() {
		if (checkCategory(Lexeme.ON_PAR)) {
			printProduction("ParamAtr", "'(' Param ')'");
			System.out.println(currentToken);
			setNextToken();
			Param();
			if (checkCategory(Lexeme.OFF_PAR)) {
				System.out.println(currentToken);
				setNextToken();
			}
		} else if (checkCategory(Lexeme.OP_ATR)) {
			printProduction("ParamAtr", "'=' Ec Atr");
			System.out.println(currentToken);
			setNextToken();
			Ec();
			Atr();
		} else if (checkCategory(Lexeme.ON_BRACKET)) {
			printProduction("ParamAtr", "'[' Ea ']' '=' Ec Atr");
			System.out.println(currentToken);
			setNextToken();
			Ea();
			if (checkCategory(Lexeme.OFF_BRACKET)) {
				System.out.println(currentToken);
				setNextToken();
				if (checkCategory(Lexeme.OP_ATR)) {
					System.out.println(currentToken);
					setNextToken();
					Ec();
					Atr();
				}
			} 
		}
	}
	
	public void Atr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("Atr", "',' Id '=' Ec Atr");
			System.out.println(currentToken);
			setNextToken();
			Id();
			if (checkCategory(Lexeme.OP_ATR)) {
				System.out.println(currentToken);
				setNextToken();
				Ec();
				Atr();
			} 
		} else {
			printProduction("Atr", epsilon);
		}
	}
	
	public void Return() {
		if (checkCategory(Lexeme.ON_PAR, Lexeme.OP_SUB, Lexeme.CT_INT, Lexeme.CT_BOOL, Lexeme.CT_CHAR, Lexeme.CT_FLOAT, Lexeme.CT_STRING, Lexeme.ID)) {
			printProduction("Return", "Ec");
			Ec();
		} else {
			printProduction("Return", epsilon);
		}
	}
	
	public void Command() {
		if (checkCategory(Lexeme.RW_PRINT)) {
			printProduction("Command", "'print' '(' 'CT_STRING' PrintParam ')' ';'");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				if (checkCategory(Lexeme.CT_STRING, Lexeme.ID)) {
					System.out.println(currentToken);
					setNextToken();
					PrintParam();
					if (checkCategory(Lexeme.OFF_PAR)) {
						System.out.println(currentToken);
						setNextToken();
						if (!checkCategory(Lexeme.SEMICOLON)) {
						} else {
							System.out.println(currentToken);
							setNextToken();
						}
					}
				}
			}
		}else if(checkCategory(Lexeme.RW_PRINTLN)){ 
			printProduction("Command", "'println' '(' 'CT_STRING' PrintLParam ')' ';'");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				if (checkCategory(Lexeme.CT_STRING, Lexeme.ID)) {
					System.out.println(currentToken);
					setNextToken();
					PrintParam();
					if (checkCategory(Lexeme.OFF_PAR)) {
						System.out.println(currentToken);
						setNextToken();
						if (!checkCategory(Lexeme.SEMICOLON)) {
						} else {
							System.out.println(currentToken);
							setNextToken();
						}
					}
				}
			}
		}else if (checkCategory(Lexeme.RW_READ)) {
			printProduction("Command", "'read' '(' ReadParam ')' ';'");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				ReadParam();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					if (!checkCategory(Lexeme.SEMICOLON)) {
					} else {
						System.out.println(currentToken);
						setNextToken();
					}
				} 
			} 
		} else if (checkCategory(Lexeme.RW_WHILE)) {
			printProduction("Command", "'while' '(' Eb ')' Body");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				Eb();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					Body();
				} 
			} 
		} else if (checkCategory(Lexeme.RW_FOR)) {
			printProduction("Command", "'for' ForParams");
			System.out.println(currentToken);
			setNextToken();
			ForParams();
		} else if (checkCategory(Lexeme.RW_IF)) {
			printProduction("Command", "'condIf' '(' Eb ')' Body Ifr");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				Eb();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					Body();
					Ifr();
				} 
			} 
		}
	}
	
	public void PrintParam() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("PrintParam", "',' Ec PrintParam");
			System.out.println(currentToken);
			setNextToken();
			Ec();
			PrintParam();
		} else {
			printProduction("PrintParam", epsilon);
		}
	}
	
	public void ReadParam() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("ReadParam", "'id' ArrayOpt ReadParamr");
			System.out.println(currentToken);
			setNextToken();
			ArrayOpt();
			ReadParamr();
		} 
	}
	
	public void ReadParamr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("ReadParamr", "',' 'id' ArrayOpt ReadParamr");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ID)) {
				System.out.println(currentToken);
				setNextToken();
				ArrayOpt();
				ReadParamr();
			}
		} else {
			printProduction("ReadParamr", epsilon);
		}
	}
	
	public void ForParams() {
		if (checkCategory(Lexeme.ON_PAR)) {
			printProduction("ForParams", "'(' 'typeInt' 'id' ':'  Ea ',' Ea ForStep ')' Body");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.RW_INT)) {
				System.out.println(currentToken);
				setNextToken();
				if (checkCategory(Lexeme.ID)) {
					System.out.println(currentToken);
					setNextToken();
					if (checkCategory(Lexeme.COLON)) {
						System.out.println(currentToken);
						setNextToken();
						Ea();
							if (checkCategory(Lexeme.SEP)) {
								System.out.println(currentToken);
								setNextToken();
								Ea();
								ForStep();
								if (checkCategory(Lexeme.OFF_PAR)) {
									System.out.println(currentToken);
									setNextToken();
									Body();
								}

							}
					} 
				} 
			} 
		} 
	}
	
	public void ForStep() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("ForStep", "',' Ea");
			System.out.println(currentToken);
			setNextToken();
			Ea();
		} else {
			printProduction("ForStep", epsilon);
		}
	}
	
	public void Ifr() {
		if (checkCategory(Lexeme.RW_ELSE)) {
			printProduction("Ifr", "'condElse' Body");
			System.out.println(currentToken);
			setNextToken();
			Body();
		} else {
			printProduction("Ifr", epsilon);
		}
	}
	
	public void Ec() {
		printProduction("Ec", "Fc Ecr");
		Eb();
		Ecr();
	}
	
	public void Ecr() {
		if (checkCategory(Lexeme.OP_CONC)) {
			printProduction("Ecr", "'OP_CONC' Fc Ecr");
			System.out.println(currentToken);
			setNextToken();
			Eb();
			Ecr();
		} else {
			printProduction("Ecr", epsilon);
		}
	}
	
	public void Eb() {
		printProduction("Eb", "Tb Ebr");
		Tb();
		Ebr();
	}
	
	public void Ebr() {
		if (checkCategory(Lexeme.OP_OR)) {
			printProduction("br", "'OP_OR' Tb Ebr");
			System.out.println(currentToken);
			setNextToken();
			Tb();
			Ebr();
		} else {
			printProduction("Ebr", epsilon);
		}
	}
	
	public void Tb() {
		printProduction("Tb", "Fb Tbr");
		Fb();
		Tbr();
	}
	
	public void Tbr() {
		if (checkCategory(Lexeme.OP_AND)) {
			printProduction("Tbr", "'OP_AND' Fb Tbr");
			System.out.println(currentToken);
			setNextToken();
			Fb();
			Tbr();
		} else {
			printProduction("Tbr", epsilon);
		}
	}
	
	public void Fb() {
		if (checkCategory(Lexeme.OP_NOT)) {
			printProduction("Fb", "'OP_NOT' Fb");
			System.out.println(currentToken);
			setNextToken();
			Fb();
		} else if (checkCategory(Lexeme.ON_PAR, Lexeme.OP_SUB, Lexeme.CT_INT, Lexeme.CT_BOOL, Lexeme.CT_CHAR, Lexeme.CT_FLOAT, Lexeme.CT_STRING, Lexeme.ID)) {
			printProduction("Fb", "Ra Fbr");
			Ra();
			Fbr();
		}
	}
	
	public void Fbr() {
		if (checkCategory(Lexeme.OP_GREATER)) {
			printProduction("Fbr", "'OP_GREATER' Ra Fbr");
			System.out.println(currentToken);
			setNextToken();
			Ra();
			Fbr();
		} else if (checkCategory(Lexeme.OP_LESS)) {
			printProduction("Fbr", "'OP_LESS' Ra Fbr");
			System.out.println(currentToken);
			setNextToken();
			Ra();
			Fbr();
		} else if (checkCategory(Lexeme.OP_GRTEREQ)) {
			printProduction("Fbr", "'OP_GRTEREQ' Ra Fbr");
			System.out.println(currentToken);
			setNextToken();
			Ra();
			Fbr();
		} else if (checkCategory(Lexeme.OP_LESSEQ)) {
			printProduction("Fbr", "'OP_LESSEQ' Ra Fbr");
			System.out.println(currentToken);
			setNextToken();
			Ra();
			Fbr();
		} else {
			printProduction("Fbr", epsilon);
		}
	}
	
	public void Ra() {
		printProduction("Ra", "Ea Rar");
		Ea();
		Rar();
	}
	
	public void Rar() {
		if (checkCategory(Lexeme.OP_REL)) {
			printProduction("Rar", "'OP_REL' Ea Rar");
			System.out.println(currentToken);
			setNextToken();
			Ea();
			Rar();
		} else if (checkCategory(Lexeme.OP_RELNOT)) {
			printProduction("Rar", "'OP_RELNOT' Ea Rar");
			System.out.println(currentToken);
			setNextToken();
			Ea();
			Rar();
		} else {
			printProduction("Rar", epsilon);
		}
	}
	
	public void Ea() {
		printProduction("Ea", "Ta Ear");
		Ta();
		Ear();
	}
	
	public void Ear() {
		if (checkCategory(Lexeme.OP_AD)) {
			printProduction("Ear", "'OP_AD' Ta Ear");
			System.out.println(currentToken);
			setNextToken();
			Ta();
			Ear();
		} else if (checkCategory(Lexeme.OP_SUB)) {
			printProduction("Ear", "'OP_SUB' Ta Ear");
			System.out.println(currentToken);
			setNextToken();
			Ta();
			Ear();
		} else {
			printProduction("Ear", epsilon);
		}
	}
	
	public void Ta() {
		printProduction("Ta", "Pa Tar");
		Pa();
		Tar();
	}
	
	public void Tar() {
		if (checkCategory(Lexeme.OP_MULT)) {
			printProduction("Tar", "'OP_MULT' Pa Tar");
			System.out.println(currentToken);
			setNextToken();
			Pa();
			Tar();
		} else if (checkCategory(Lexeme.OP_DIV)) {
			printProduction("Tar", "'OP_DIV' Pa Tar");
			System.out.println(currentToken);
			setNextToken();
			Pa();
			Tar();
		} else {
			printProduction("Tar", epsilon);
		}
	}
	
	public void Pa() {
		printProduction("Pa", "Fa Par");
		Fa();
		Par();
	}
	
	public void Par() {
		if (checkCategory(Lexeme.OP_MOD)) {
			printProduction("Par", "'OP_MOD' Fa Par");
			System.out.println(currentToken);
			setNextToken();
			Fa();
			Par();
		} else {
			printProduction("Par", epsilon);
		}
	}
	
	public void Fa() {
		if (checkCategory(Lexeme.ON_PAR)) {
			printProduction("Fa", "'(' Ec ')'");
			System.out.println(currentToken);
			setNextToken();
			Ec();
			if (checkCategory(Lexeme.OFF_PAR)) {
				System.out.println(currentToken);
				setNextToken();
			}
		} else if (checkCategory(Lexeme.OP_SUB)) {
			printProduction("Fa", "'OP_SUB' Fa");
			System.out.println(currentToken);
			setNextToken();
			Fa();
		} else if (checkCategory(Lexeme.ID)) {
			IdOrFun();
		} else if (checkCategory(Lexeme.CT_BOOL)) {
			printProduction("Fa", "'CT_BOOL'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.CT_CHAR)) {
			printProduction("Fa", "'CT_CHAR'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.CT_FLOAT)) {
			printProduction("Fa", "'CT_FLOAT'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.CT_INT)) {
			printProduction("Fa", "'CT_INT'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.CT_STRING)) {
			printProduction("Fa", "'CT_STRING'");
			System.out.println(currentToken);
			setNextToken();
		}
	}
	
	public void IdOrFun() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("IdOrFun", "'id' IdOrFunr");
			System.out.println(currentToken);
			setNextToken();
			IdOrFunr();
		}
	}
	
	public void IdOrFunr() {
		if (checkCategory(Lexeme.ON_PAR)) {
			printProduction("IdOrFunr", "'(' Param ')'");
			System.out.println(currentToken);
			setNextToken();
			Param();
			if (checkCategory(Lexeme.OFF_PAR)) {
				System.out.println(currentToken);
				setNextToken();
			}
		} else if (checkCategory(Lexeme.ON_BRACKET)) {
			printProduction("IdOrFunr", "'[' Ea ']'");
			System.out.println(currentToken);
			setNextToken();
			Ea();
			if (checkCategory(Lexeme.OFF_BRACKET)) {
				System.out.println(currentToken);
				setNextToken();
			}
		} else {
			printProduction("IdOrFunr", epsilon);
		}
	}
}
