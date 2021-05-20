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
			FS();
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
	
	public void FS() {
		if (checkCategory(Lexeme.CT_VAR, Lexeme.RW_INT, Lexeme.RW_FLOAT, Lexeme.RW_BOOL, Lexeme.RW_CHAR, Lexeme.RW_STRING)) {
			printProduction("S", "DeclId S");
			fDeclId();
			FS();
		} else if (checkCategory(Lexeme.RW_FUN)) {
			printProduction("S", "FunDecl S");
			fFunDecl();
			FS();
		} else if (checkCategory(Lexeme.RW_PROC)) {
			printProduction("S", "ProcDecl S");
			fProcDecl();
			FS();
		} else {
			printProduction("S", epsilon);
		}
	}
	
	public void fDeclId() {
		if (checkCategory(Lexeme.RW_INT, Lexeme.RW_FLOAT, Lexeme.RW_BOOL, Lexeme.RW_CHAR, Lexeme.RW_STRING)) {
			printProduction("DeclId", "Type LId ';'");
			fType();
			fLId();
			if (!checkCategory(Lexeme.SEMICOLON)){
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
		} else if (checkCategory(Lexeme.CT_VAR)) {
			printProduction("DeclId", "'const' Type LId ';'");
			System.out.println(currentToken);
			setNextToken();
			fType();
			fLId();
			if (checkCategory(Lexeme.SEMICOLON)) {
				System.out.println(currentToken);
				setNextToken();
			}
		}
	}
	
	public void fType() {
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

	public void fLId() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("LId", "Id AttrOpt LIdr");
			fId();
			fAttrOpt();
			fLIdr();
		}
	}
	
	public void fLIdr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("LIdr", "',' Id AttrOpt LIdr");
			System.out.println(currentToken);
			setNextToken();
			fId();
			fAttrOpt();
			fLIdr();
		} else {
			printProduction("LIdr", epsilon);
		}
	}

	public void fId() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("Id", "'id' ArrayOpt");
			System.out.println(currentToken);
			setNextToken();
			fArrayOpt();
		}
	}
	
	public void fAttrOpt() {
		if (checkCategory(Lexeme.OP_ATR)) {
			printProduction("AttrOpt", "'=' Ec");
			System.out.println(currentToken);
			setNextToken();
			fEc();
		} else {
			printProduction("AttrOpt", epsilon);
		}
	}
	
	public void fFunDecl() {
		if (checkCategory(Lexeme.RW_FUN)) {
			printProduction("FunDecl", "'fun' Type FunName '(' LParamDecl ')' Body");
			System.out.println(currentToken);
			setNextToken();
			fType();
			fFunName();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				fLParamDecl();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					fBody();
				} 
			}
		}
	}
	
	public void fFunName() {
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

	public void fLParamCall() {
		if (checkCategory(Lexeme.ID, Lexeme.ON_PAR, Lexeme.OP_SUB, Lexeme.CT_BOOL, Lexeme.CT_CHAR, Lexeme.CT_FLOAT, Lexeme.CT_INT, Lexeme.CT_STRING)) {
			printProduction("LParamCall", "Ec LParamCallr");
			fEc();
			fLParamCallr();
		} else {
			printProduction("LParamCall", epsilon);
		}
	}

	public void fLParamCallr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("LParamCallr", "',' Ec LParamCallr");
			System.out.println(currentToken);
			setNextToken();
			fEc();
			fLParamCallr();
		} else {
			printProduction("LParamCallr", epsilon);
		}
	}
	
	public void fLParamDecl() {
		if (checkCategory(Lexeme.RW_BOOL, Lexeme.RW_CHAR, Lexeme.RW_FLOAT, Lexeme.RW_INT, Lexeme.RW_STRING)) {			
			printProduction("LParamDecl", "Type 'id' ArrayOpt LParamDeclr");
			fType();
			if (checkCategory(Lexeme.ID)) {
				System.out.println(currentToken);
				setNextToken();
				fArrayOpt();
				fLParamDeclr();
			} 
		}else {
				printProduction("LParamDecl", epsilon);
			}
		}
	
	public void fLParamDeclr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("LParamDeclr", "',' Type 'id' ArrayOpt LParamDeclr");
			System.out.println(currentToken);
			setNextToken();
			fType();
			if (checkCategory(Lexeme.ID)) {
				System.out.println(currentToken);
				setNextToken();
				fArrayOpt();
				fLParamDeclr();
			}
		}
	}
	
	public void fArrayOpt() {
		if (checkCategory(Lexeme.ON_BRACKET)) {
			printProduction("ArrayOpt", "'[' Ea ']'");
			System.out.println(currentToken);
			setNextToken();
			fEa();
			if (!checkCategory(Lexeme.OFF_BRACKET)) {
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
		} else {
			printProduction("ArrayOpt", epsilon);
		}
	}

	public void fProcDecl() {
		if (checkCategory(Lexeme.RW_PROC)) {
			printProduction("ProcDecl", "'proc' FunName '(' LParamDecl ')' Body");
			System.out.println(currentToken);
			setNextToken();
			fFunName();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				fLParamDecl();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					fBody();
				} 
			}
		}
	}
	
	public void fBody() {
		if (checkCategory(Lexeme.ON_BRACE)) {
			++scopeCounter;
			printProduction("Body", "'{' BodyPart '}'");
			System.out.println(currentToken);
			setNextToken();
			fBodyPart();
			if (!checkCategory(Lexeme.OFF_BRACE)) {
			} else {
				System.out.println(currentToken);
				setNextToken();
				--scopeCounter;
			}
		}
	}
	
	public void fBodyPart() {
		if (checkCategory(Lexeme.CT_VAR, Lexeme.RW_INT, Lexeme.RW_FLOAT, Lexeme.RW_BOOL, Lexeme.RW_CHAR, Lexeme.RW_STRING)) {
			printProduction("BodyPart", "DeclId BodyPart");
			fDeclId();
			fBodyPart();
		} else if (checkCategory(Lexeme.RW_PRINT,Lexeme.RW_PRINTLN,Lexeme.RW_READ, Lexeme.RW_WHILE, Lexeme.RW_FOR, Lexeme.RW_IF)) {
			printProduction("BodyPart", "Command BodyPart");
			fCommand();
			fBodyPart();
		} else if (checkCategory(Lexeme.ID)) {
			printProduction("BodyPart", "BodyPartr ';' BodyPart");
			fBodyPartr();
			if (!checkCategory(Lexeme.SEMICOLON)) {
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
			fBodyPart();
		} else if (checkCategory(Lexeme.RW_RETURN)) {
			printProduction("BodyPart", "'return' Return ';'");
			System.out.println(currentToken);
			setNextToken();
			fReturn();
			if (!checkCategory(Lexeme.SEMICOLON)) {
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
		} else {
			printProduction("BodyPart", epsilon);
		}
	}
	
	public void fBodyPartr() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("BodyPartr", "'id' ParamAttr");
			System.out.println(currentToken);
			setNextToken();
			fParamAttr();
		}
	}
	
	public void fParamAttr() {
		if (checkCategory(Lexeme.ON_PAR)) {
			printProduction("ParamAttrib", "'(' LParamCall ')'");
			System.out.println(currentToken);
			setNextToken();
			fLParamCall();
			if (!checkCategory(Lexeme.OFF_PAR)) {
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
		} else if (checkCategory(Lexeme.OP_ATR)) {
			printProduction("ParamAttrib", "'=' Ec LAttr");
			System.out.println(currentToken);
			setNextToken();
			fEc();
			fLAttr();
		} else if (checkCategory(Lexeme.ON_BRACKET)) {
			printProduction("ParamAttrib", "'[' Ea ']' '=' Ec LAttr");
			System.out.println(currentToken);
			setNextToken();
			fEa();
			if (checkCategory(Lexeme.OFF_BRACKET)) {
				System.out.println(currentToken);
				setNextToken();
				if (checkCategory(Lexeme.OP_ATR)) {
					System.out.println(currentToken);
					setNextToken();
					fEc();
					fLAttr();
				}
			} 
		}
	}
	
	public void fLAttr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("LAttr", "',' Id '=' Ec LAttr");
			System.out.println(currentToken);
			setNextToken();
			fId();
			if (checkCategory(Lexeme.OP_ATR)) {
				System.out.println(currentToken);
				setNextToken();
				fEc();
				fLAttr();
			} 
		} else {
			printProduction("LAttr", epsilon);
		}
	}
	
	public void fReturn() {
		if (checkCategory(Lexeme.ON_PAR, Lexeme.OP_SUB, Lexeme.CT_INT, Lexeme.CT_BOOL, Lexeme.CT_CHAR, Lexeme.CT_FLOAT, Lexeme.CT_STRING, Lexeme.ID)) {
			printProduction("Return", "Ec");
			fEc();
		} else {
			printProduction("Return", epsilon);
		}
	}
	
	public void fCommand() {
		if (checkCategory(Lexeme.RW_PRINT)) {
			printProduction("Command", "'print' '(' 'constStr' PrintLParam ')' ';'");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				if (checkCategory(Lexeme.CT_STRING, Lexeme.ID)) {
					System.out.println(currentToken);
					setNextToken();
					fPrintLParam();
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
			printProduction("Command", "'println' '(' 'constStr' PrintLParam ')' ';'");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				if (checkCategory(Lexeme.CT_STRING, Lexeme.ID)) {
					System.out.println(currentToken);
					setNextToken();
					fPrintLParam();
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
			printProduction("Command", "'scan' '(' ScanLParam ')' ';'");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				fScanLParam();
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
			printProduction("Command", "'whileLoop' '(' Eb ')' Body");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				fEb();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					fBody();
				} 
			} 
		} else if (checkCategory(Lexeme.RW_FOR)) {
			printProduction("Command", "'forLoop' ForParams");
			System.out.println(currentToken);
			setNextToken();
			fForParams();
		} else if (checkCategory(Lexeme.RW_IF)) {
			printProduction("Command", "'condIf' '(' Eb ')' Body Ifr");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ON_PAR)) {
				System.out.println(currentToken);
				setNextToken();
				fEb();
				if (checkCategory(Lexeme.OFF_PAR)) {
					System.out.println(currentToken);
					setNextToken();
					fBody();
					fIfr();
				} 
			} 
		}
	}
	
	public void fPrintLParam() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("PrintLParam", "',' Ec PrintLParam");
			System.out.println(currentToken);
			setNextToken();
			fEc();
			fPrintLParam();
		} else {
			printProduction("PrintLParam", epsilon);
		}
	}
	
	public void fScanLParam() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("ScanLParam", "'id' ArrayOpt ScanLParamr");
			System.out.println(currentToken);
			setNextToken();
			fArrayOpt();
			fScanLParamr();
		} 
	}
	
	public void fScanLParamr() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("ScanLParamr", "',' 'id' ArrayOpt ScanLParamr");
			System.out.println(currentToken);
			setNextToken();
			if (checkCategory(Lexeme.ID)) {
				System.out.println(currentToken);
				setNextToken();
				fArrayOpt();
				fScanLParamr();
			}
		} else {
			printProduction("ScanLParamr", epsilon);
		}
	}
	
	public void fForParams() {
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
						fEa();
							if (checkCategory(Lexeme.SEP)) {
								System.out.println(currentToken);
								setNextToken();
								fEa();
								fForStep();
								if (checkCategory(Lexeme.OFF_PAR)) {
									System.out.println(currentToken);
									setNextToken();
									fBody();
								}

							}
					} 
				} 
			} 
		} 
	}
	
	public void fForStep() {
		if (checkCategory(Lexeme.SEP)) {
			printProduction("ForStep", "',' Ea");
			System.out.println(currentToken);
			setNextToken();
			fEa();
		} else {
			printProduction("ForStep", epsilon);
		}
	}
	
	public void fIfr() {
		if (checkCategory(Lexeme.RW_ELSE)) {
			printProduction("Ifr", "'condElse' Body");
			System.out.println(currentToken);
			setNextToken();
			fBody();
		} else {
			printProduction("Ifr", epsilon);
		}
	}
	
	public void fEc() {
		printProduction("Ec", "Fc Ecr");
		fEb();
		fEcr();
	}
	
	public void fEcr() {
		if (checkCategory(Lexeme.OP_CONC)) {
			printProduction("Ecr", "'opConcat' Fc Ecr");
			System.out.println(currentToken);
			setNextToken();
			fEb();
			fEcr();
		} else {
			printProduction("Ecr", epsilon);
		}
	}
	
	public void fEb() {
		printProduction("Eb", "Tb Ebr");
		fTb();
		fEbr();
	}
	
	public void fEbr() {
		if (checkCategory(Lexeme.OP_OR)) {
			printProduction("Ebr", "'opOr' Tb Ebr");
			System.out.println(currentToken);
			setNextToken();
			fTb();
			fEbr();
		} else {
			printProduction("Ebr", epsilon);
		}
	}
	
	public void fTb() {
		printProduction("Tb", "Fb Tbr");
		fFb();
		fTbr();
	}
	
	public void fTbr() {
		if (checkCategory(Lexeme.OP_AND)) {
			printProduction("Tbr", "'opAnd' Fb Tbr");
			System.out.println(currentToken);
			setNextToken();
			fFb();
			fTbr();
		} else {
			printProduction("Tbr", epsilon);
		}
	}
	
	public void fFb() {
		if (checkCategory(Lexeme.OP_NOT)) {
			printProduction("Fb", "'opNot' Fb");
			System.out.println(currentToken);
			setNextToken();
			fFb();
		} else if (checkCategory(Lexeme.ON_PAR, Lexeme.OP_SUB, Lexeme.CT_INT, Lexeme.CT_BOOL, Lexeme.CT_CHAR, Lexeme.CT_FLOAT, Lexeme.CT_STRING, Lexeme.ID)) {
			printProduction("Fb", "Ra Fbr");
			fRa();
			fFbr();
		}
	}
	
	public void fFbr() {
		if (checkCategory(Lexeme.OP_GREATER)) {
			printProduction("Fbr", "'opGreater' Ra Fbr");
			System.out.println(currentToken);
			setNextToken();
			fRa();
			fFbr();
		} else if (checkCategory(Lexeme.OP_LESS)) {
			printProduction("Fbr", "'opLesser' Ra Fbr");
			System.out.println(currentToken);
			setNextToken();
			fRa();
			fFbr();
		} else if (checkCategory(Lexeme.OP_GRTEREQ)) {
			printProduction("Fbr", "'opGreq' Ra Fbr");
			System.out.println(currentToken);
			setNextToken();
			fRa();
			fFbr();
		} else if (checkCategory(Lexeme.OP_LESSEQ)) {
			printProduction("Fbr", "'opLeq' Ra Fbr");
			System.out.println(currentToken);
			setNextToken();
			fRa();
			fFbr();
		} else {
			printProduction("Fbr", epsilon);
		}
	}
	
	public void fRa() {
		printProduction("Ra", "Ea Rar");
		fEa();
		fRar();
	}
	
	public void fRar() {
		if (checkCategory(Lexeme.OP_REL)) {
			printProduction("Rar", "'opEquals' Ea Rar");
			System.out.println(currentToken);
			setNextToken();
			fEa();
			fRar();
		} else if (checkCategory(Lexeme.OP_RELNOT)) {
			printProduction("Rar", "'opNotEqual' Ea Rar");
			System.out.println(currentToken);
			setNextToken();
			fEa();
			fRar();
		} else {
			printProduction("Rar", epsilon);
		}
	}
	
	public void fEa() {
		printProduction("Ea", "Ta Ear");
		fTa();
		fEar();
	}
	
	public void fEar() {
		if (checkCategory(Lexeme.OP_AD)) {
			printProduction("Ear", "'opAdd' Ta Ear");
			System.out.println(currentToken);
			setNextToken();
			fTa();
			fEar();
		} else if (checkCategory(Lexeme.OP_SUB)) {
			printProduction("Ear", "'opSub' Ta Ear");
			System.out.println(currentToken);
			setNextToken();
			fTa();
			fEar();
		} else {
			printProduction("Ear", epsilon);
		}
	}
	
	public void fTa() {
		printProduction("Ta", "Pa Tar");
		fPa();
		fTar();
	}
	
	public void fTar() {
		if (checkCategory(Lexeme.OP_MULT)) {
			printProduction("Tar", "'opMult' Pa Tar");
			System.out.println(currentToken);
			setNextToken();
			fPa();
			fTar();
		} else if (checkCategory(Lexeme.OP_DIV)) {
			printProduction("Tar", "'opDiv' Pa Tar");
			System.out.println(currentToken);
			setNextToken();
			fPa();
			fTar();
		} else {
			printProduction("Tar", epsilon);
		}
	}
	
	public void fPa() {
		printProduction("Pa", "Fa Par");
		fFa();
		fPar();
	}
	
	public void fPar() {
		if (checkCategory(Lexeme.OP_MOD)) {
			printProduction("Par", "'opPow' Fa Par");
			System.out.println(currentToken);
			setNextToken();
			fFa();
			fPar();
		} else {
			printProduction("Par", epsilon);
		}
	}
	
	public void fFa() {
		if (checkCategory(Lexeme.ON_PAR)) {
			printProduction("Fa", "'(' Ec ')'");
			System.out.println(currentToken);
			setNextToken();
			fEc();
			if (!checkCategory(Lexeme.OFF_PAR)) {
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
		} else if (checkCategory(Lexeme.OP_SUB)) {
			printProduction("Fa", "'opSub' Fa");
			System.out.println(currentToken);
			setNextToken();
			fFa();
		} else if (checkCategory(Lexeme.ID)) {
			fIdOrFunCall();
		} else if (checkCategory(Lexeme.CT_BOOL)) {
			printProduction("Fa", "'constBool'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.CT_CHAR)) {
			printProduction("Fa", "'constChar'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.CT_FLOAT)) {
			printProduction("Fa", "'constFloat'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.CT_INT)) {
			printProduction("Fa", "'constInt'");
			System.out.println(currentToken);
			setNextToken();
		} else if (checkCategory(Lexeme.CT_STRING)) {
			printProduction("Fa", "'constStr'");
			System.out.println(currentToken);
			setNextToken();
		}
	}
	
	public void fIdOrFunCall() {
		if (checkCategory(Lexeme.ID)) {
			printProduction("IdOrFunCall", "'id' IdOrFunCallr");
			System.out.println(currentToken);
			setNextToken();
			fIdOrFunCallr();
		}
	}
	
	public void fIdOrFunCallr() {
		if (checkCategory(Lexeme.ON_PAR)) {
			printProduction("IdOrFunCallr", "'(' LParamCall ')'");
			System.out.println(currentToken);
			setNextToken();
			fLParamCall();
			if (!checkCategory(Lexeme.OFF_PAR)) {
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
		} else if (checkCategory(Lexeme.ON_BRACKET)) {
			printProduction("IdOrFunCallr", "'[' Ea ']'");
			System.out.println(currentToken);
			setNextToken();
			fEa();
			if (!checkCategory(Lexeme.OFF_BRACKET)) {
			} else {
				System.out.println(currentToken);
				setNextToken();
			}
		} else {
			printProduction("IdOrFunCallr", epsilon);
		}
	}
}
