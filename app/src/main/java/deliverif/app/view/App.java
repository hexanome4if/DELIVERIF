package deliverif.app.view;

import deliverif.app.controller.*;
import deliverif.app.model.map.Map;
import deliverif.app.model.request.PlanningRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static Stage stageGraph;

    public static final String styleSheet
            = "node {\n"
            + "	size: 3px;\n"
            + "     fill-mode: dyn-plain;"
            + "	fill-color: #222, #555, green, yellow;\n"
            + "	text-mode: hidden;\n"
            + "	z-index: 0;\n"
            + "}\n"
            + "\n"
            + "edge {\n"
            + "     size: 1px;\n"
            + "	shape: line;\n"
            + "     fill-mode: dyn-plain;"
            + "	fill-color: #222, #555, green, yellow;\n"
            + "	arrow-size: 3px, 2px;"
            + "}\n"
            + "\n"
            + "sprite.depotSprite {\n"
            + "	shape: diamond;\n"
            + "     size: 12px;\n"
            + "     fill-color: red;"
            + "     fill-mode: gradient-diagonal1;"
            + "     text-mode: hidden;\n"
            + "     z-index: 98;\n"
            + "}\n"
            + "\n"
            + "sprite.pickupSprite {\n"
            + "     size: 12px;\n"
            + "     fill-mode: dyn-plain;"
            + "     text-mode: hidden;\n"
            + "     z-index: 98;\n"
            + "}\n"
            + "\n"
            + "sprite.deliverySprite {\n"
            + "	shape: triangle;\n"
            + "     size: 12px;\n"
            + "     fill-mode: dyn-plain;"
            + "     text-mode: hidden;\n"
            + "     z-index: 98;\n"
            + "}\n"
            + "sprite.depotSpriteSelected {\n"
            + "	shape: diamond;\n"
            + "     size: 24px;\n"
            + "     fill-color: red;"
            + "     fill-mode: gradient-diagonal1;"
            + "     text-mode: hidden;\n"
            + "     z-index: 99;\n"
            + "     stroke-mode: plain;\n"
            + "     stroke-color: yellow;\n"
            + "}\n"
            + "\n"
            + "sprite.pickupSpriteSelected {\n"
            + "     size: 24px;\n"
            + "     fill-mode: dyn-plain;"
            + "     text-mode: hidden;\n"
            + "     z-index: 99;\n"
            + "     stroke-mode: plain;\n"
            + "     stroke-color: yellow;\n"
            + "}\n"
            + "\n"
            + "sprite.deliverySpriteSelected {\n"
            + "	shape: triangle;\n"
            + "     size: 24px;\n"
            + "     fill-mode: dyn-plain;"
            + "     text-mode: hidden;\n"
            + "     z-index: 99;\n"
            + "     stroke-mode: plain;\n"
            + "     stroke-color: yellow;\n"
            + "}\n"
            + "\n"
            + "sprite.segmentSprite {\n"
            + " shape: cross; size: 7px; z-index: 99;"
            + " fill-color: blue;"
            + "}\n"
            + "\n"
            + "edge.marked {\n"
            + " size: 4px;"
            + " fill-color: blue;"
            + "}\n"
            + "\n"
            + "edge.unmarked {\n"
            + " size: 4px;"
            + " fill-color: red;"
            + "}\n";

    @Override
    public void start(Stage stage) throws IOException {
        App.stageGraph = stage;
        scene = new Scene(loadFXML("menuPage"));
        App.stageGraph.setScene(scene);
        App.stageGraph.show();
        KeyboardEventManager kem = new KeyboardEventManager();
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

    public static Scene getScene() {
        return scene;
    }
}
