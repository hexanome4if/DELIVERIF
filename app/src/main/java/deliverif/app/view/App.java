package deliverif.app.view;

import deliverif.app.controller.*;
import deliverif.app.model.map.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import java.lang.Thread;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("interface"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException{
        
        // Choix des fichiers XML
        /*
        JFileChooser dialogue = new JFileChooser(new File("."));
        
        PrintWriter sortie;
        XmlReader reader = new XmlReader();

        String filename = "";
        File fichier;
        
        System.out.println("Chose a map file");
        try {
            Thread.sleep(1500);
        } catch (Exception e) {         
        }

        if (dialogue.showOpenDialog(null)== 
            JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            sortie = new PrintWriter
            (new FileWriter(fichier.getPath(), true));
            filename = fichier.getPath();
            sortie.close();
        }
        Map map = reader.readMap(filename);

        System.out.println("Chose a request file");

        try {
            Thread.sleep(1500);
        } catch (Exception e) {         
        }
        

        if (dialogue.showOpenDialog(null)== 
            JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            sortie = new PrintWriter
            (new FileWriter(fichier.getPath(), true));
            filename = fichier.getPath();
            sortie.close();
        }
        */
        launch();
    }

}