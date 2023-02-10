package Core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JFXMain extends Application {
    
    private static Stage primaryStage;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {
        
        setPrimaryStage(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(FilePaths.GUIMain);
        
        Parent root = fxmlLoader.load();
        
        mainScene = new Scene(root);
        primaryStage.setScene(mainScene);

        primaryStage.setTitle("JPEG: Malcek 211264");
        primaryStage.getIcons().add(FilePaths.favicon);

        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
        
        primaryStage.show();
        
    }

    private void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
