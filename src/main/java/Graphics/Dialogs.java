package Graphics;

import Core.FilePaths;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Dialogs {

    private static ArrayList<Stage> openStages = new ArrayList<>();

    private static Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    //Show one image - keepOpen false
    public static void showImageInWindow(BufferedImage bufferedImage, String title) {
        showImageInWindow(title, false, false, new Pair<BufferedImage, String>(bufferedImage,title));
    }

    //Show one image - keepOpen optional
    public static void showImageInWindow(BufferedImage bufferedImage, String title, boolean keepOpen) {
        showImageInWindow(title, false, keepOpen, new Pair<BufferedImage, String>(bufferedImage,title));
    }

    //Show multiple images - keepOpen false
    public static void showImageInWindow(String title, Pair<BufferedImage, String>... images) {
        showImageInWindow(title, true, false, images);
    }

    //Universal function
    public static void showImageInWindow(String title, boolean showTitle, boolean keepOpen, Pair<BufferedImage, String>... images) {

        Stage stage = new Stage();
        stage.getIcons().add(FilePaths.favicon);
        HBox hBox = new HBox(1);
        BorderPane root = new BorderPane();
        root.setCenter(hBox);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setUserData(keepOpen);

        stage.setOnCloseRequest((e) -> openStages.remove(stage));
        openStages.add(stage);

        int jointWidth = 0;
        int maxHeight = 0;

        // Count max images width and height
        for (Pair<BufferedImage, String> image : images) {
            jointWidth += image.getKey().getWidth();
            maxHeight = Math.max(maxHeight, image.getKey().getHeight());
        }

        ImageView firstImageView = null;
        int imageWidth = 0;
        int imageHeight = 0;

        for (Pair<BufferedImage, String> imagePair : images) {
            BufferedImage image = imagePair.getKey();
            StackPane stackPane = setImageStackPane(image, imagePair.getValue(), showTitle, scene, jointWidth, maxHeight);

            if (firstImageView == null) {
                firstImageView = (ImageView) stackPane.getChildren().get(0);
                imageWidth = image.getWidth();
                imageHeight = image.getHeight();
            }

            hBox.getChildren().add(stackPane);
        }

        handleMaxSize(stage, jointWidth, maxHeight);

        // Count image percentage of window size
        setUpSizeListener(firstImageView, imageWidth, imageHeight, stage, scene, title);

        stage.show();
    }

    /**
     * Show buffered image in new window.
     *
     * @param //bufferedImage
     * @param title
     * @param //keepOpen      If it is set to true, window can be close only manually. Close All button wont work on it.
     */

    private static StackPane setImageStackPane(BufferedImage image, String title, boolean showTitle, Scene scene, int maxWidth, int maxHeight) {
        StackPane stackPane = new StackPane();

        Image fxImage = convertImage(image);

        ImageView imageView = new ImageView(fxImage);

        // Handle image resize
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(Bindings.multiply(scene.widthProperty(), fxImage.getWidth() / maxWidth));
        imageView.fitHeightProperty().bind(Bindings.multiply(scene.heightProperty(), fxImage.getHeight() / maxHeight));

        // Add image label
        stackPane.getChildren().add(imageView);

        if(showTitle) {
            Label label = new Label(title);
            label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-text-fill: white;");
            stackPane.getChildren().add(label);
            stackPane.setAlignment(Pos.TOP_LEFT);
        }

        return stackPane;
    }

    private static void setUpSizeListener(ImageView imageView, int imageWidth, int imageHeight, Stage stage, Scene scene, String title) {
        // Count image percentage of window size
        ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
            double scaleWidth = imageView.getFitWidth() / imageWidth;
            double scaleHeight = imageView.getFitHeight() / imageHeight;
            double percentageScale = Math.min(scaleWidth, scaleHeight) * 100;
            stage.setTitle(String.format("%s (%.2f%%)", title, percentageScale));
        };

        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);
    }

    private static Image convertImage(BufferedImage image) {
        // Convert buffered image to JavaFX image. Equivalent to SwingFXUtils.toFXImage(bufferedImage, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            baos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Image(new ByteArrayInputStream(baos.toByteArray()));
    }

    private static void handleMaxSize(Stage stage, int maxWidth, int maxHeight) {
        // Handle bigger images
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        if (maxWidth > screenWidth) {
            stage.setWidth(screenWidth);
        }

        if (maxHeight > screenHeight) {
            stage.setHeight(screenHeight);
        }
    }

    public static void closeAllWindows() {
        for (Stage stage : openStages) {
            stage.close();
        }
        openStages.clear();
    }

    public static BufferedImage loadImageFromPath(String pathToIImage) {
        File imageFile = new File(pathToIImage);

        if(!imageFile.exists()) throw new RuntimeException("Image file does not exist.");

        try {
           return ImageIO.read(imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Change default image");
        fileChooser.setInitialDirectory(new File(FilePaths.pathName));
        //fileChooser.setInitialDirectory(parent.toFile());

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.bmp", "*.jpeg"));

        File ff = fileChooser.showOpenDialog(null);

        if (ff != null) {
            return ff;
        }

        System.out.println("Wrong file");
        return null;
    }
}
