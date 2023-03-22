package sintatico;

import Erro.Erro;
import exceptions.ExcecoesSintaticas;
import lexico.Analisador;
import lexico.Token;

import java.util.ArrayList;

public class AnalisadoSintatico {

    public static Analisador scanner;
    private Token token;

    private boolean estouFor = false;
    private boolean dentroFor = false;
    private boolean dentroWhile = false;
    public static ArrayList<Erro> errosSintatico = new ArrayList();

    public AnalisadoSintatico(Analisador scanner) {
        this.scanner = scanner;
        token = scanner.nextToken();
    }

    public void P() {
        System.out.println("P - "+token.toString());
        if (!scanner.isEOF()) {
            if (token.getType() == Token.TK_CONDITIONAL) {
                C();
            } else if (token.getType() == Token.TK_LOOP || token.getType() == Token.TK_LOOPFOR) {
                R();
            } else if (token.getType() == Token.TK_IDENTIFIER) {
                A();
            } else {
                errosSintatico.add(new Erro("(Identificador,if, for ou while)" + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
                while (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_LOOP && token.getType() != Token.TK_LOOPFOR && token.getType() != Token.TK_CONDITIONAL) {
                    leproximo();
                }
                P();

            }
        } else {
            errosSintatico.add(new Erro("não há continuação na expressão iniciada por : " + token.toString(), token.getLine(), token.getColumn()));
        }
    }

    public void A() {
        leproximo();
        if (token.getType() == Token.TK_ASSIGN) {
            leproximo();
            System.out.println("A - "+token.toString());
            E();
        } else {
            errosSintatico.add(new Erro(Token.TK_TEXT[Token.TK_ASSIGN] + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
            while (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_LOOP && token.getType() != Token.TK_LOOPFOR && token.getType() != Token.TK_CONDITIONAL) {
                leproximo();
            }
            P();

        }
        if (!scanner.isEOF() && token.getType() != Token.TK_CONDITIONAL3 && token.getType() != Token.TK_END && estouFor != true) {
            P();
        }

    }

    public void E() {
        D();
        System.out.println("E - "+token.toString());
        System.out.println(scanner.isEOF());
        if (!scanner.isEOF()) {
            leproximo();
            EA();
        }

    }

    public void EA() {

        if (token.getType() == Token.TK_OPERATORA) {
            leproximo();
            E();
        }
    }

    public void D() {
        if (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_NUMBER) {
            errosSintatico.add(new Erro("numero ou um identificador" + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
        }
    }

    public void OP() {
        if (token.getType() == Token.TK_OPERATORR) {
            leproximo();
            D();
        }
    }

    public void ER() {

        D();
        if (!scanner.isEOF()) {
            leproximo();
            OP();
        }
    }

    public void C() {
        leproximo();
        ER();
        leproximo();
        if (token.getType() == Token.TK_CONDITIONAL2) {
            leproximo();
            P();
            if (token.getType() == Token.TK_CONDITIONAL3) {
                leproximo();
                P();
            }
            if (token.getType() != Token.TK_END) {
                errosSintatico.add(new Erro(Token.TK_TEXT[Token.TK_END] + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
                while (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_LOOP && token.getType() != Token.TK_LOOPFOR && token.getType() != Token.TK_CONDITIONAL) {
                    leproximo();
                }

                P();
            } else {
                if (!scanner.isEOF()) {
                    token = scanner.nextToken();
                    if (token.getType() != Token.TK_END) {
                        P();
                    }
                }
            }
        } else {
            errosSintatico.add(new Erro(Token.TK_TEXT[Token.TK_CONDITIONAL2] + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
            while (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_LOOP && token.getType() != Token.TK_LOOPFOR && token.getType() != Token.TK_CONDITIONAL) {
                leproximo();
            }

            P();

        }

    }

    public void R() {
        if (token.getType() == Token.TK_LOOP) {
            W();
        } else {
            F();
        }
        //System.out.println("fim do r - "+token.toString());
        
    }

    public void W() {
        dentroWhile = true;
        leproximo();
        ER();
        leproximo();
        if (token.getType() == Token.TK_LOOP2) {
            leproximo();
            System.out.println("token : "+token.toString());
            P();
            System.out.println("token : "+token.toString());
            if (token.getType() != Token.TK_END) {
                errosSintatico.add(new Erro(Token.TK_TEXT[Token.TK_END] + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
                while (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_LOOP && token.getType() != Token.TK_LOOPFOR && token.getType() != Token.TK_CONDITIONAL) {
                    leproximo();
                }

                P();

            } else {
                if (!scanner.isEOF()) {
                    token = scanner.nextToken();
                    if (token.getType() != Token.TK_END) {
                        P();
                    }
                }
                
            }
        } else {
            errosSintatico.add(new Erro(Token.TK_TEXT[Token.TK_LOOP2] + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
            while (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_LOOP && token.getType() != Token.TK_LOOPFOR && token.getType() != Token.TK_CONDITIONAL) {
                leproximo();
            }
            P();
        }

    }

    public void F() {
        dentroFor = true;
        estouFor = true;
        leproximo();
        A();
        ER();
        leproximo();
        A();
        if (token.getType() == Token.TK_LOOP2 && !scanner.isEOF()) {
            leproximo();
            estouFor = false;
            P();
            System.out.println("for - "+token.toString());
            if (token.getType() != Token.TK_END) {
                errosSintatico.add(new Erro(Token.TK_TEXT[Token.TK_END] + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
                while (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_LOOP && token.getType() != Token.TK_LOOPFOR && token.getType() != Token.TK_CONDITIONAL) {
                    leproximo();
                }

                P();

            } else {
                if (!scanner.isEOF()) {
                    token = scanner.nextToken();
                    System.out.println("fim do for - "+token.toString());
                    if (token.getType() != Token.TK_END) {
                        P();
                    }
                }else{
                    return;
                }
            }

        } else {
            errosSintatico.add(new Erro(Token.TK_TEXT[Token.TK_LOOP2] + " era esperado, foi encontrado o token " + token.toString(), token.getLine(), token.getColumn()));
            while (token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_LOOP && token.getType() != Token.TK_LOOPFOR && token.getType() != Token.TK_CONDITIONAL) {
                leproximo();
            }
            P();
        }
    }

    private boolean leproximo() {
        if (!scanner.isEOF()) {
            token = scanner.nextToken();
            return true;
        } else {
            errosSintatico.add(new Erro("Expressão incompleta, falta a sequencia do comando", token.getLine(), token.getColumn()));
            return false;
        }
    }

}
