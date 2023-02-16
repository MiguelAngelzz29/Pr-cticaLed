/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package es.cifpcarlos3.fxled;

import es.cifpcarlos3.fxled.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
/**
 * FXML Controller class
 *
 * @author migue
 */
public class LedController implements Initializable {


    @FXML
    private ToggleButton btnEncender;
    @FXML
    private ColorPicker clrPickerLed;
    @FXML
    private ColorPicker clrPickerShapeLed;
    @FXML
    private ShapeLed shapeLed;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        shapeLed.blinkingProperty().bindBidirectional(btnEncender.selectedProperty());
        shapeLed.ledColorProperty().bindBidirectional(clrPickerLed.valueProperty());
        shapeLed.borderColorProperty().bind(clrPickerShapeLed.valueProperty());
    }    
    
}
