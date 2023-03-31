package Core;

import javafx.scene.image.Image;

import java.net.URL;

public class FilePaths {

    public static final URL GUIMain = FilePaths.class.getClassLoader().getResource("Graphics/mainwindow.fxml");

    public static Image favicon = new Image(FilePaths.class.getClassLoader().getResourceAsStream("favicon.png"));

    public static final String pathName = "C:\\Users\\User\\IdeaProjects\\JPEG_coder\\src\\Images";
    //public static ImagePlus image = new ImagePlus("lena_std.jpg");p

    //public static String image = "resources/lena_std.jpg";

}
