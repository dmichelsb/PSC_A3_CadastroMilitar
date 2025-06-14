package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//v1.0

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource("/views/login.fxml")));
        Parent root = fxmlLoader.load(); // Parent classe base abstrata da interface grafica
        Scene tela = new Scene(root);

        primaryStage.setTitle("Cadastro de Militar");
        primaryStage.setScene(tela);
        primaryStage.show();
    }

}