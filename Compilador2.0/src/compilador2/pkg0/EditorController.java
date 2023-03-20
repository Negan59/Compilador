package compilador2.pkg0;

import static compilador2.pkg0.Compilador20.erro;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lexico.Analisador;
import lexico.Token;
import sintatico.AnalisadoSintatico;

public class EditorController implements Initializable {

    @FXML
    private TextArea txTeste;
    @FXML
    private VBox vboxLateral;
    @FXML
    private VBox vboxResultado;
    private ArrayList<Label> lista = new ArrayList();
    private ArrayList<Label> linhas = new ArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 1; i <= 30; i++) {
            Label nu = new Label();
            nu.setText("" + i);
            linhas.add(nu);
            vboxLateral.getChildren().add(nu);
        }
    }

    @FXML
    private void evtCompila(ActionEvent event) {
        Analisador scanner = new Analisador(txTeste.getText());
        Token token = null;
        Token ant = null;
        if (!lista.isEmpty()) {
            for (int j = 0; j < Analisador.erros.size(); j++) {
                linhas.get(Analisador.erros.get(j).getLinha() - 1).setStyle("-fx-background-color:none");
            }
            Analisador.erros.removeAll(Analisador.erros);
            vboxResultado.getChildren().remove(1, lista.size() + 1);
            lista.removeAll(lista);
        }
        try {
            do {
                token = scanner.nextToken();
                if (token != null) {
                    ant = token;
                    Label l = new Label();
                    l.setText(token.toString());
                    l.setStyle("-fx-text-fill: green;");
                    lista.add(l);
                    vboxResultado.getChildren().add(l);
                }
            } while (token != null);
        } catch (Exception ex) {
            for (int i = 0; i < Analisador.erros.size(); i++) {
                Label l = new Label();
                l.setText("Erro Léxico = " + Analisador.erros.get(i).getMsg() + " linha = " + Analisador.erros.get(i).getLinha());
                linhas.get(Analisador.erros.get(i).getLinha() - 1).setStyle("-fx-background-color:red");
                l.setStyle("-fx-text-fill: red;");
                lista.add(l);
                vboxResultado.getChildren().add(l);
            }

        }
        Analisador scanner2 = new Analisador(txTeste.getText());
        AnalisadoSintatico as = new AnalisadoSintatico(scanner2);
        if(Analisador.erros.size()==0){
        try {
            as.P();
            Label l = new Label();
            l.setText("Compilação realizada com sucesso!!!");
            l.setStyle("-fx-text-fill: green;");
            lista.add(l);
            vboxResultado.getChildren().add(l);
        } catch (Exception ex) {
            Label l = new Label();
            l.setText("Erro Sintático : " + ex.getMessage());
            System.out.println(ex.getMessage());
            l.setStyle("-fx-text-fill: red;");
            lista.add(l);
            vboxResultado.getChildren().add(l);
        }
        }else{
            Label l = new Label();
            l.setText("Erro Sintático : " + "Não é um statement");
            l.setStyle("-fx-text-fill: red;");
            lista.add(l);
            vboxResultado.getChildren().add(l);
        }
    }

    @FXML
    private void evtLimpa(ActionEvent event) {
        for (int j = 0; j < Analisador.erros.size(); j++) {
            linhas.get(Analisador.erros.get(j).getLinha() - 1).setStyle("-fx-background-color:none");
        }
        Analisador.erros.removeAll(Analisador.erros);
        txTeste.setText("");
        vboxResultado.getChildren().remove(1, lista.size() + 1);
        lista.removeAll(lista);
    }

}
