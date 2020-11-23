/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 * @author fabien
 */
public class BaseTemplateController {
    
    @FXML
    private Button loadCityMapButton;
    
    @FXML
    private Button loadRequestButton;
    
    @FXML
    private Button computeTourButton;
    
    @FXML
    private Button editTourButton;
    
    @FXML
    private void loadCityMapAction() throws IOException {
        System.out.println("loadCityMapAction");
    }
    
    @FXML
    private void loadRequestAction() throws IOException {
        System.out.println("loadRequestAction");
    }
    
    @FXML
    private void computeTourAction() throws IOException {
        System.out.println("computeTourAction");
    }
    
    @FXML
    private void editTourAction() throws IOException {
        System.out.println("editTourAction");
    }
    
}
