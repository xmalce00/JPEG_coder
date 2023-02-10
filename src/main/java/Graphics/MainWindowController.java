package Graphics;

import Enums.SamplingTypes;
import Enums.TransformTypes;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class MainWindowController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        samplingtype.getItems().setAll(SamplingTypes.values());
        transformtype.getItems().setAll(TransformTypes.values());
//
        samplingtype.getSelectionModel().select(SamplingTypes.S_4_4_4);
        transformtype.getSelectionModel().select(TransformTypes.DCT);

        ObservableList<Integer> blocks = FXCollections.observableArrayList(2,4,8,16,32,64,128,256,512);

        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValueFactory.setValue(8);

        transformblocks.setValueFactory(spinnerValueFactory);

        samplingQuality.setValue(50);
        samplingQualityField.textProperty().bindBidirectional(samplingQuality.valueProperty(), NumberFormat.getIntegerInstance());
        samplingQualityField.setTextFormatter(new TextFormatter<>(filter));
    }

    @FXML
    private TextField qualityMSF;

    @FXML
    private TextField qualityPsNR;

    @FXML
    private Slider samplingQuality;

    @FXML
    private TextField samplingQualityField;

    @FXML
    private ComboBox<SamplingTypes> samplingtype;

    @FXML
    private CheckBox shadesofgrey;

    @FXML
    private CheckBox showsteps;

    @FXML
    private ComboBox<TransformTypes> transformtype;

    @FXML
    private Spinner<Integer> transformblocks;

    @FXML
    void Changeimage(ActionEvent event) {

    }

    @FXML
    void Close(ActionEvent event) {

    }

    @FXML
    void Closewindows(ActionEvent event) {

    }

    @FXML
    void ConvertToYCbCr(ActionEvent event) {

    }

    @FXML
    void Quantize(ActionEvent event) {

    }

    @FXML
    void Reset(ActionEvent event) {

    }

    @FXML
    void Showimage(ActionEvent event) {

    }

    @FXML
    void Transform(ActionEvent event) {

    }

    @FXML
    void convertToRGB(ActionEvent event) {

    }

    @FXML
    void countquality(ActionEvent event) {

    }

    @FXML
    void downSample(ActionEvent event) {

    }

    @FXML
    void iquantize(ActionEvent event) {

    }

    @FXML
    void itransform(ActionEvent event) {

    }

    @FXML
    void oversample(ActionEvent event) {

    }

    @FXML
    void showModifiedBlue(ActionEvent event) {

    }

    @FXML
    void showModifiedCb(ActionEvent event) {

    }

    @FXML
    void showModifiedCr(ActionEvent event) {

    }

    @FXML
    void showModifiedGreen(ActionEvent event) {

    }

    @FXML
    void showModifiedRGB(ActionEvent event) {

    }

    @FXML
    void showModifiedRed(ActionEvent event) {

    }

    @FXML
    void showModifiedY(ActionEvent event) {

    }

    @FXML
    void showOriginalBlue(ActionEvent event) {

    }

    @FXML
    void showOriginalCb(ActionEvent event) {

    }

    @FXML
    void showOriginalCr(ActionEvent event) {

    }

    @FXML
    void showOriginalGreen(ActionEvent event) {

    }

    @FXML
    void showOriginalRed(ActionEvent event) {

    }

    @FXML
    void showOriginalY(ActionEvent event) {

    }

    private UnaryOperator<TextFormatter.Change> filter = change -> {
        String text = change.getText();
        if (text.matches("[0-9]*"))  return change;
        return null;
    };

}
