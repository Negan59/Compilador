package sintatico;

import exceptions.ExcecoesSintaticas;
import lexico.Analisador;
import lexico.Token;

public class AnalisadoSintatico {

    private Analisador scanner;
    private Token token;

    private boolean estouFor = false;

    public AnalisadoSintatico(Analisador scanner) {
        this.scanner = scanner;
        token = scanner.nextToken();
    }

    public void P() {
        if (!scanner.isEOF()) {
            if (token.getType() == Token.TK_CONDITIONAL) {
                C();
            } else if (token.getType() == Token.TK_LOOP || token.getType() == Token.TK_LOOPFOR) {
                R();
            } else if (token.getType() == Token.TK_IDENTIFIER) {
                A();
            } else {
                throw new ExcecoesSintaticas("(Identificador,if ou while)" + " era esperado, foi encontrado o token " + token.toString());
            }
        } else {
            throw new ExcecoesSintaticas("não há continuação na expressão iniciada por : " + token.toString());
        }
    }

    public void A() {
        leproximo();
        if (token.getType() == Token.TK_ASSIGN) {
            leproximo();
            E();
        } else {
            throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_ASSIGN] + " era esperado, foi encontrado o token " + token.toString());
        }

        if (!scanner.isEOF() && token.getType() != Token.TK_CONDITIONAL3 && token.getType() != Token.TK_END && estouFor != true) {
            P();
        }

    }

    public void E() {
        D();
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
            throw new ExcecoesSintaticas("numero ou um identificador" + " era esperado, foi encontrado o token " + token.toString());
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
                throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_END] + " era esperado, foi encontrado o token " + token.toString());
            } else {
                if (!scanner.isEOF()) {
                    token = scanner.nextToken();
                    if (token.getType() != Token.TK_END) {
                        P();
                    }
                }
            }
        } else {
            throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_CONDITIONAL2] + " era esperado, foi encontrado o token " + token.toString());
        }

    }

    public void R() {
        if (token.getType() == Token.TK_LOOP) {
            W();
        } else {
            F();
        }

        if (!scanner.isEOF()) {
            P();
        }
    }

    public void W() {
        leproximo();
        ER();
        leproximo();
        if (token.getType() == Token.TK_LOOP2) {
            leproximo();
            P();
            if (token.getType() != Token.TK_END) {
                throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_END] + " era esperado, foi encontrado o token " + token.toString());
            } else {
                if (!scanner.isEOF()) {
                    token = scanner.nextToken();
                    if(token.getType() != Token.TK_END){
                        P();
                    }
                }
            }
        } else {
            throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_LOOP2] + " era esperado, foi encontrado o token " + token.toString());
        }

    }

    public void F() {
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
            if (token.getType() != Token.TK_END) {
                throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_END] + " era esperado, foi encontrado o token " + token.toString());
            } else {
                if (!scanner.isEOF()) {
                    token = scanner.nextToken();
                    if(token.getType() != Token.TK_END){
                        P();
                    }
                }
            }

        } else {
            throw new ExcecoesSintaticas(Token.TK_TEXT[Token.TK_LOOP2] + " era esperado, foi encontrado o token " + token.toString());
        }
    }

    private boolean leproximo() {
        if (!scanner.isEOF()) {
            token = scanner.nextToken();
            return true;
        } else {
            throw new ExcecoesSintaticas("Expressão incompleta, falta a sequencia do comando");
        }
    }

}
