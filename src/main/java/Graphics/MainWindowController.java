package Graphics;

import Enums.QualityTypes;
import Enums.SamplingTypes;
import Enums.TransformTypes;
import Enums.YcRcBTypes;
import Jpeg.Process;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class MainWindowController implements Initializable {

    public Button showOriginalImageBtn;

    public Button showOriginalRedBtn;
    public Button showOriginalGreenBtn;
    public Button showOriginalBlueBtn;

    public Button showOriginalYBtn;
    public Button showOriginalCrBtn;
    public Button showOriginalCbBtn;

    public Button convertBtn;
    public Button downSampleBtn;
    public Button transformBtn;
    public Button quantizeBtn;

    public Button iQuantizeBtn;
    public Button iTransformBtn;
    public Button overSampleBtn;
    public Button iConvertBtn;

    public Button countParamsBtn;
    public Button countSSIMparamsBtn;

    public Button showModifiedImageBtn;

    public Button showModifiedRedBtn;
    public Button showModifiedGreenBtn;
    public Button showModifiedBlueBtn;

    public Button showModifiedYBtn;
    public Button showModifiedCrBtn;
    public Button showModifiedCbBtn;

    private Process process;
    private BufferedImage originalImage;
    private final ArrayList<Button> buttons = new ArrayList<>();

//    public MainWindowController() {
  //  }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        samplingtype.getItems().setAll(SamplingTypes.values());
        transformtype.getItems().setAll(TransformTypes.values());

        samplingtype.getSelectionModel().select(SamplingTypes.S_4_4_4);
        transformtype.getSelectionModel().select(TransformTypes.DCT);

        qualityType.getItems().setAll(QualityTypes.values());
        qualityTypeSSIM.getItems().setAll(YcRcBTypes.values());

        qualityType.getSelectionModel().select(QualityTypes.RED);
        qualityTypeSSIM.getSelectionModel().select(YcRcBTypes.Y);

        ObservableList<Integer> blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256,512);
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValueFactory.setValue(8);
        transformblocks.setValueFactory(spinnerValueFactory);

        quantizeQuality.setValue(50);
        quantizeQualityField.textProperty().bindBidirectional(quantizeQuality.valueProperty(), NumberFormat.getIntegerInstance());
        quantizeQualityField.setTextFormatter(new TextFormatter<>(filter));

        showsteps.setSelected(true);

        buttons.add(showOriginalYBtn);
        buttons.add(showOriginalCrBtn);
        buttons.add(showOriginalCbBtn);

        buttons.add(downSampleBtn);
        buttons.add(transformBtn);
        buttons.add(quantizeBtn);

        buttons.add(iQuantizeBtn);
        buttons.add(iTransformBtn);
        buttons.add(overSampleBtn);
        buttons.add(iConvertBtn);

        buttons.add(countParamsBtn);
        buttons.add(countSSIMparamsBtn);

        buttons.add(showModifiedImageBtn);

        buttons.add(showModifiedRedBtn);
        buttons.add(showModifiedGreenBtn);
        buttons.add(showModifiedBlueBtn);

        buttons.add(showModifiedYBtn);
        buttons.add(showModifiedCrBtn);
        buttons.add(showModifiedCbBtn);

        importImage();
        buttonInit();
    }

    private void importImage() {
        try {
            this.originalImage = Dialogs.loadImageFromPath(Dialogs.openFile().getPath());
            this.process = new Process(originalImage);
            System.out.println("Image: w: " + originalImage.getWidth() + " h: " + originalImage.getHeight());
            Dialogs.showImageInWindow(originalImage, "Original");
        } catch (NullPointerException e) {
            System.out.println("No selected image");
        }
    }

    private void buttonInit() {
        for(Button b : buttons) {
            b.setDisable(true);
        }
    }

    @FXML
    private void Closewindows() { Dialogs.closeAllWindows(); }

    @FXML
    private void Changeimage() {
        importImage();
        Dialogs.closeAllWindows();
        buttonInit();
    }

    @FXML
    private void Showimage() { Dialogs.showImageInWindow(process.getOriginalImageFromRGB(), "Original RGB");}

    @FXML
    private CheckBox shadesofgrey;

    @FXML
    private void showOriginalRed() {
        //boolean greyscale = shadesofgrey.isSelected();
        Dialogs.showImageInWindow(process.getOneColorImageFromRGB(process.getOriginalRed(), Process.RED, shadesofgrey.isSelected()),"Original RED");
    }

    @FXML
    private void showOriginalGreen() {
        Dialogs.showImageInWindow(process.getOneColorImageFromRGB(process.getOriginalGreen(), Process.GREEN, shadesofgrey.isSelected()),"Original GREEN");
    }

    @FXML
    private void showOriginalBlue() {
        Dialogs.showImageInWindow(process.getOneColorImageFromRGB(process.getOriginalBlue(), Process.BLUE, shadesofgrey.isSelected()),"Original BLUE");
    }

    @FXML
    private void showModifiedRGB() { Dialogs.showImageInWindow(process.getModifiedImageFromRGB(),"Modified RBG"); }

    @FXML
    private void showModifiedRed() {
        Dialogs.showImageInWindow(process.getOneColorImageFromRGB(process.getModifiedRed(), Process.RED, shadesofgrey.isSelected()),"Modified RED");
    }

    @FXML
    private void showModifiedGreen() {
        Dialogs.showImageInWindow(process.getOneColorImageFromRGB(process.getModifiedGreen(), Process.GREEN, shadesofgrey.isSelected()),"Modified GREEN");
    }

    @FXML
    private void showModifiedBlue() {
        Dialogs.showImageInWindow(process.getOneColorImageFromRGB(process.getModifiedBlue(), Process.BLUE, shadesofgrey.isSelected()),"Modified BLUE");
    }

    @FXML
    private void showOriginalY() {
        Dialogs.showImageInWindow(process.getOneColorImageFromYCbCr(process.getOriginalY()), "Original Y");
    }

    @FXML
    private void showOriginalCr() {
        Dialogs.showImageInWindow(process.getOneColorImageFromYCbCr(process.getOriginalCr()), "Original Cr");
    }

    @FXML
    private void showOriginalCb() {
        Dialogs.showImageInWindow(process.getOneColorImageFromYCbCr(process.getOriginalCb()), "Original cB");
    }

    @FXML
    private void showModifiedY() {
        Dialogs.showImageInWindow(process.getOneColorImageFromYCbCr(process.getModifiedY()), "Modified Y");
    }

    @FXML
    private void showModifiedCr() {
        Dialogs.showImageInWindow(process.getOneColorImageFromYCbCr(process.getModifiedCr()), "Modified cR");
    }

    @FXML
    private void showModifiedCb() {
        Dialogs.showImageInWindow(process.getOneColorImageFromYCbCr(process.getModifiedCb()), "Modified cB");
    }

    @FXML
    private void ConvertToYCbCr() {
        process.convertToYcBcR();
        showOriginalYBtn.setDisable(false);
        showOriginalCrBtn.setDisable(false);
        showOriginalCbBtn.setDisable(false);
        downSampleBtn.setDisable(false);
    }

    @FXML
    private void convertToRGB() {
        process.convertToRBG();
        countParamsBtn.setDisable(false);
        countSSIMparamsBtn.setDisable(false);
        showModifiedImageBtn.setDisable(false);
        showModifiedRedBtn.setDisable(false);
        showModifiedGreenBtn.setDisable(false);
        showModifiedBlueBtn.setDisable(false);
    }

    @FXML
    private ComboBox<SamplingTypes> samplingtype;

    @FXML
    private CheckBox showsteps;

    @FXML
    private void downSample() {
        System.out.println("Down " + samplingtype.getValue());
        process.downSample(samplingtype.getValue());
        samplingtype.setDisable(true);
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Down " + samplingtype.getValue(),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedY()),"Down sample Y"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCr()), "Down sample cR"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCb()), "Down sample cB")
                    );
        }

        ObservableList<Integer> blocks = null;
        if(samplingtype.getValue() == SamplingTypes.S_4_2_2 || samplingtype.getValue() == SamplingTypes.S_4_2_0) {
            blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256);
        } else if (samplingtype.getValue() == SamplingTypes.S_4_1_1) {
            blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128);
        } else if (samplingtype.getValue() == SamplingTypes.S_4_4_4) {
            blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256,512);
        }
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValueFactory.setValue(8);
        transformblocks.setValueFactory(spinnerValueFactory);

        transformBtn.setDisable(false);
    }

    @FXML
    private void oversample() {
        System.out.println("Up " + samplingtype.getValue());
        process.upSample(samplingtype.getValue());
        samplingtype.setDisable(false);
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Up " + samplingtype.getValue(),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedY()),"Up sample Y"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCr()), "Up sample cR"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCb()), "Up sample cB")
            );
        }
        ObservableList<Integer> blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256,512);
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValueFactory.setValue(8);
        transformblocks.setValueFactory(spinnerValueFactory);

        //transformblocks.getValueFactory().ListSpinnerValueFactory<>(blocks);

        showModifiedYBtn.setDisable(false);
        showModifiedCrBtn.setDisable(false);
        showModifiedCbBtn.setDisable(false);

        iConvertBtn.setDisable(false);
    }

    @FXML
    public ComboBox<QualityTypes> qualityType;

    @FXML
    private void countquality() {
        double [] params = process.countQuality(qualityType.getValue());
        qualityMSF.setText(String.format("%.3f",params[0]));
        qualityMAE.setText(String.format("%.3f",params[1]));
        qualityPSNR.setText(String.format("%.3f",params[2]));
        qualitySAE.setText(String.format("%.3f",params[3]));
    }

    @FXML
    private TextField qualityMSF;
    @FXML
    public TextField qualityMAE;
    @FXML
    public TextField qualityPSNR;
    @FXML
    public TextField qualitySAE;

    @FXML
    public ComboBox<YcRcBTypes> qualityTypeSSIM;

    @FXML
    public void countSSIM() {
        double[] params = process.countSSIM(qualityTypeSSIM.getValue());
        qualitySSIM.setText(String.format("%.5f",params[0]));
        qualityMSSIM.setText(String.format("%.5f",params[1]));
    }

    @FXML
    public TextField qualitySSIM;
    @FXML
    public TextField qualityMSSIM;

    @FXML
    void Transform() {
        System.out.println(transformtype.getValue().toString() + ", blocks: " + transformblocks.getValue() + " x " + transformblocks.getValue());
        process.transform(transformtype.getValue(), transformblocks.getValue());
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    transformtype.getValue().toString() + ", blocks: " + transformblocks.getValue() + " x " + transformblocks.getValue(),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedY()),"Transform Y"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCr()), "Transform cR"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCb()), "Transform cB")
            );
        }
        transformtype.setDisable(true);
        transformblocks.setDisable(true);

        quantizeBtn.setDisable(false);
    }

    @FXML
    void itransform() {
        System.out.println("Inverse " + transformtype.getValue().toString() + ", blocks: " + transformblocks.getValue() + " x " + transformblocks.getValue());
        process.inverseTransform(transformtype.getValue(), transformblocks.getValue());
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Inverse " + transformtype.getValue().toString() + ", blocks: " + transformblocks.getValue() + " x " + transformblocks.getValue(),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedY()),"Inverse transform Y"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCr()), "Inverse transform cR"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCb()), "Inverse transform cB")
            );
        }
        transformtype.setDisable(false);
        transformblocks.setDisable(false);

        overSampleBtn.setDisable(false);
    }

    @FXML
    private ComboBox<TransformTypes> transformtype;
    @FXML
    private Spinner<Integer> transformblocks;

    @FXML
    void Quantize() {
        System.out.println("Quantize - quality: " + quantizeQuality.getValue());
        process.quantize(transformblocks.getValue(), quantizeQuality.getValue());
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Quantize",
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedY()),"Quantize Y"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCr()), "Quantize cR"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCb()), "Quantize cB")
            );
        }
        quantizeQuality.setDisable(true);
        quantizeQualityField.setDisable(true);
        iQuantizeBtn.setDisable(false);
    }

    @FXML
    void iquantize() {
        System.out.println("Inverse quantize - quality: " + quantizeQuality.getValue());
        process.inverseQuantize(transformblocks.getValue(), quantizeQuality.getValue());
        if(showsteps.isSelected()) {
            Dialogs.showImageInWindow(
                    "Inverse quantize",
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedY()),"Inverse quantize Y"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCr()), "Inverse quantize cR"),
                    new Pair<>(process.getOneColorImageFromYCbCr(process.getModifiedCb()), "Inverse quantize cB")
            );
        }
        quantizeQuality.setDisable(false);
        quantizeQualityField.setDisable(false);
        iTransformBtn.setDisable(false);
    }

    @FXML
    public Slider quantizeQuality;
    @FXML
    public TextField quantizeQualityField;

    @FXML
    void Close() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void Reset() {
        process = new Process(originalImage);
        buttonInit();
        samplingtype.setDisable(false);
        transformtype.setDisable(false);
        samplingtype.setDisable(false);
        quantizeQuality.setDisable(false);
        quantizeQualityField.setDisable(false);
        Dialogs.closeAllWindows();
    }

    private UnaryOperator<TextFormatter.Change> filter = change -> {
        String text = change.getText();
        if (text.matches("[0-9]*"))  return change;
        return null;
    };
}