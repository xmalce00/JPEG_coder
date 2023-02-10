package Core;

import javafx.scene.image.Image;
import java.net.URL;

public class FilePaths {

    public static final URL GUIMain = FilePaths.class.getClassLoader().getResource("Graphics/mainwindow.fxml");

    public static Image favicon = new Image(FilePaths.class.getClassLoader().getResourceAsStream("favicon.png"));

}
