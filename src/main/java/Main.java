package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fmxlLoader = new FXMLLoader(Main.class.getResource("Principal.fxml"));
        Scene scene = new Scene(fmxlLoader.load(), 800, 600);

        primaryStage.setTitle("Calculadora de Turmas");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Fechar Calculadora de Turmas");
            alert.setHeaderText("Ao fechar, seus dados serão perdidos...");
            alert.setContentText("Tem certeza que deseja fechar o programa? Exporte o arquivo antes de sair.");

            ButtonType buttonTypeFechar = new ButtonType("Fechar Mesmo Assim");
            ButtonType buttonTypeCancelar = new ButtonType("Cancelar");

            alert.getButtonTypes().setAll(buttonTypeFechar, buttonTypeCancelar);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeCancelar) {
                //Ao clicar em cancelar não fecha o programa.
                event.consume();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
