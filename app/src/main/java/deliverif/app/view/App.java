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
    
    private static Stage stageGraph;
    
    public static final String styleSheet = 
          "node {\n"
        + "	size: 3px;\n"
        + "     fill-mode: dyn-plain;"
        + "	fill-color: #222, #555, green, yellow;\n"
        + "	text-mode: hidden;\n"
        + "	z-index: 0;\n"
        + "}\n"
        + "\n"
        + "edge {\n"
        + "	shape: line;\n"
        + "     fill-mode: dyn-plain;"
        + "	fill-color: #222, #555, green, yellow;\n"
        + "	arrow-size: 3px, 2px;"
        + "}\n"
        + "\n"
        + "sprite.depotSprite {\n"
        + "	shape: diamond;\n"
        + "     size: 12px;\n"
        + "     fill-mode: dyn-plain;"
        + "     fill-color: green;\n"
        + "     text-mode: hidden;\n"
        + "     z-index: 99;\n"
        + "}\n"
        + "\n"
        + "sprite.pickupSprite {\n"
        + "     size: 12px;\n"
        + "     fill-mode: dyn-plain;"
        + "     fill-color: red;\n"
        + "     text-mode: hidden;\n"
        + "     z-index: 99;\n"
        + "}\n"
        + "\n"
        + "sprite.deliverySprite {\n"
        + "	shape: triangle;\n"
        + "     size: 12px;\n"
        + "     fill-mode: dyn-plain;"
        + "     fill-color: blue;\n"
        + "     text-mode: hidden;\n"
        + "     z-index: 99;\n"
        + "}\n";

    
    @Override
    public void start(Stage stage) throws IOException {
        App.stageGraph = stage ;
        scene = new Scene(loadFXML("menuPage"));
        App.stageGraph.setScene(scene); 
        App.stageGraph.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {
        launch(args);  
    }
    
    
    // Choix des fichiers XML
    public static Map choseMapFile(XmlReader reader) throws IOException {

        JFileChooser dialogue = new JFileChooser(new File("."));

        PrintWriter sortie;

        String filename = "";
        File fichier;

        System.out.println("Chose a map file");

        if (dialogue.showOpenDialog(null)
                == JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            sortie = new PrintWriter(new FileWriter(fichier.getPath(), true));
            filename = fichier.getPath();
            sortie.close();
        }
        reader.readMap(filename);

        return reader.getMap();
    }

    public static PlanningRequest choseRequestFile(XmlReader reader) throws IOException {

        JFileChooser dialogue = new JFileChooser(new File("."));

        PrintWriter sortie;

        String filename = "";
        File fichier;

        System.out.println("Chose a request file");

        if (dialogue.showOpenDialog(null)
                == JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            sortie = new PrintWriter(new FileWriter(fichier.getPath(), true));
            filename = fichier.getPath();
            sortie.close();
        }
        PlanningRequest pr = reader.readRequest(filename);
        return pr;
    }
}
