package compilador2.pkg0;

import Erro.Erro;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Compilador20 extends Application {

    public static Erro erro = new Erro();
    
    private WritableImage createImageFromPath(String imagePath) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(imagePath);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Editor.fxml"));
        // Carrega o ícone
        WritableImage icon = createImageFromPath("Gui-piler.png");

        // Define o ícone da aplicação
        stage.getIcons().add(icon);

        Scene scene = new Scene(root);
        stage.setTitle("Compilador");
        stage.setScene(scene);
        stage.setFullScreen(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }

}
