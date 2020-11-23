package deliverif.app.view;

import deliverif.app.controller.*;
import deliverif.app.model.map.Map;
import deliverif.app.model.request.PlanningRequest;

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

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
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
    
    // Choix des fichiers XML
    private static Map choseMapFile(XmlReader reader) throws IOException {
        
        JFileChooser dialogue = new JFileChooser(new File("."));
        
        PrintWriter sortie;
        

        String filename = "";
        File fichier;
        
        System.out.println("Chose a map file");

        if (dialogue.showOpenDialog(null)== 
            JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            sortie = new PrintWriter
            (new FileWriter(fichier.getPath(), true));
            filename = fichier.getPath();
            sortie.close();
        }
        reader.readMap(filename);
        
        return reader.getMap();
    }
    
    private static PlanningRequest  choseRequestFile(XmlReader reader) throws IOException {
        
        JFileChooser dialogue = new JFileChooser(new File("."));
        
        PrintWriter sortie;

        String filename = "";
        File fichier;
        
        System.out.println("Chose a request file");
        
        if (dialogue.showOpenDialog(null)== 
            JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            sortie = new PrintWriter
            (new FileWriter(fichier.getPath(), true));
            filename = fichier.getPath();
            sortie.close();
        }
        
        PlanningRequest pr = reader.readRequest(filename);
        return pr;
    }

    

    public static void main(String[] args) throws IOException{
        
        XmlReader reader = new XmlReader();
        Map map = choseMapFile(reader);
        
        if(map != null){
            PlanningRequest pr = choseRequestFile(reader);
        }
        
        System.out.println(map);
        launch();
    }

}