package compilador2.pkg0;

import Erro.Erro;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;

import exceptions.ExcecoesSintaticas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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
    private String textoArquivo = "";
    @FXML
    private VBox vboxResultado1;
    @FXML
    private Tab tabToken;
    @FXML
    private Tab tabErro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabToken.setClosable(false);
        tabErro.setClosable(false);
        for (int i = 1; i <= 60; i++) {
            Label nu = new Label();
            nu.setText("" + i);
            linhas.add(nu);
            vboxLateral.getChildren().add(nu);
        }
    }

    @FXML
    private void evtCompila(ActionEvent event) {
        System.out.println(txTeste.getText());
        Analisador scanner = new Analisador(txTeste.getText());
        Token token = null;
        Token ant = null;
        for(int i = 0;i<linhas.size();i++){
            linhas.get(i).setStyle("-fx-background-color:none");
        }
        Analisador.erros.removeAll(Analisador.erros);
        AnalisadoSintatico.errosSintatico.removeAll(AnalisadoSintatico.errosSintatico);
        vboxResultado.getChildren().remove(1, vboxResultado.getChildren().size());
        vboxResultado1.getChildren().remove(1, vboxResultado1.getChildren().size());
        lista.removeAll(lista);
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
                    if(!scanner.isEOF()){
                        ant = token;
                    }
                }
            } while (token != null);
        } catch (Exception ex) {

            for (int i = 0; i < Analisador.erros.size(); i++) {
                Label l = new Label();
                l.setText("Erro Léxico = " + Analisador.erros.get(i).getMsg() + " linha = " + Analisador.erros.get(i).getLinha());
                linhas.get(Analisador.erros.get(i).getLinha() - 1).setStyle("-fx-background-color:red");
                l.setStyle("-fx-text-fill: red;");
                lista.add(l);
                vboxResultado1.getChildren().add(l);
            }

        }
        Analisador scanner2 = new Analisador(txTeste.getText());
        AnalisadoSintatico as = new AnalisadoSintatico(scanner2);
        try {
            as.P();
            if (AnalisadoSintatico.errosSintatico.size() > 0) {
                throw new ExcecoesSintaticas("Teve erro sintático viu");
            }
            Label l = new Label();
            l.setText("Compilação realizada com sucesso!!!");
            l.setStyle("-fx-text-fill: green;");
            lista.add(l);
            vboxResultado.getChildren().add(l);
        } catch (Exception ex) {
            int i;
            for (i = 0; i < AnalisadoSintatico.errosSintatico.size(); i++) {
                Label l = new Label();
                l.setText("Erro Sintático : " + AnalisadoSintatico.errosSintatico.get(i).getMsg());
                linhas.get(AnalisadoSintatico.errosSintatico.get(i).getLinha() - 1).setStyle("-fx-background-color:red");
                l.setStyle("-fx-text-fill: red;");
                lista.add(l);
                vboxResultado1.getChildren().add(l);

            }
            if (ex.getMessage() == null) {
                Label l = new Label();
                l.setText("Erro Sintático : " + Token.TK_TEXT[Token.TK_END]+" está faltando");
                linhas.get(ant.getLine()-1).setStyle("-fx-background-color:red");
                l.setStyle("-fx-text-fill: red;");
                lista.add(l);
                vboxResultado1.getChildren().add(l);
            }

        }

    }

    @FXML
    private void evtLimpa(ActionEvent event) {
        for(int i = 0;i<linhas.size();i++){
            linhas.get(i).setStyle("-fx-background-color:none");
        }
        Analisador.erros.removeAll(Analisador.erros);
        AnalisadoSintatico.errosSintatico.removeAll(AnalisadoSintatico.errosSintatico);
        vboxResultado.getChildren().remove(1, vboxResultado.getChildren().size());
        vboxResultado1.getChildren().remove(1, vboxResultado1.getChildren().size());
        lista.removeAll(lista);
        txTeste.setText("");
    }

    @FXML
    private void evtAbrir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("c:\\"));
        File arquivoSelecionado = fileChooser.showOpenDialog(null);

        if (arquivoSelecionado != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivoSelecionado)))) {
                StringBuilder sb = new StringBuilder();
                String linha;
                while ((linha = br.readLine()) != null) {
                    sb.append(linha).append("\n");
                }
                txTeste.setText(sb.toString());
            } catch (IOException e) {
                System.out.println("Erro ao ler o arquivo " + arquivoSelecionado.getName() + ": " + e.getMessage());
            }
        }
    }

    @FXML
    private void evtSalvar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("c:\\"));
        File arquivoSelecionado = fileChooser.showSaveDialog(null);

        if (arquivoSelecionado != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoSelecionado))) {
                bw.write(txTeste.getText());
            } catch (IOException e) {
                System.out.println("Erro ao salvar o arquivo " + arquivoSelecionado.getName() + ": " + e.getMessage());
            }
        }
    }

}
