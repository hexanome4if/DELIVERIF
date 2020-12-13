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
 * JavaFX App. Main class. Initialize and show the fxml scene of the
 * application.
 */
public class App extends Application {

    // PRIVATES STATIC ATTRIBUTES
    /**
     * JavaFX scene of the application
     */
    private static Scene scene;

    /**
     * JavaFX stage of the application
     */
    private static Stage stage;

    // PUBLIC STATIC ATTRIBUTES 
    /**
     * CSS style sheet for the graph using graphstream library
     */
    public static final String STYLESHEET
            = "node {\n"
            + "     size: 3px;\n"
            + "     fill-mode: dyn-plain;"
            + "     fill-color: #222, #555, green, yellow;\n"
            + "     text-mode: hidden;\n"
            + "     z-index: 0;\n"
            + "}\n"
            + "\n"
            + "edge.default {\n"
            + "     size: 1px;\n"
            + "     shape: line;\n"
            + "     fill-mode: dyn-plain;"
            + "     fill-color: #222, #555, green, yellow;\n"
            + "     arrow-size: 3px, 2px;"
            + "}\n"
            + "\n"
            + "sprite.depotSprite {\n"
            + "     shape: diamond;\n"
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
            + "     shape: triangle;\n"
            + "     size: 12px;\n"
            + "     fill-mode: dyn-plain;"
            + "     text-mode: hidden;\n"
            + "     z-index: 98;\n"
            + "}\n"
            + "sprite.depotSpriteSelected {\n"
            + "     shape: diamond;\n"
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
            + "     shape: triangle;\n"
            + "     size: 24px;\n"
            + "     fill-mode: dyn-plain;"
            + "     text-mode: hidden;\n"
            + "     z-index: 99;\n"
            + "     stroke-mode: plain;\n"
            + "     stroke-color: yellow;\n"
            + "}\n"
            + "\n"
            + "sprite.segmentSprite {\n"
            + "     shape: cross; "
            + "     size: 9px; z-index: 99;"
            + "     fill-color: green;"
            + "}\n"
            + "\n"
            + "edge.marked {\n"
            + "     size: 4px;"
            + "     fill-color: blue;"
            + "}\n"
            + "\n"
            + "edge.unmarked {\n"
            + "     size: 4px;"
            + "     fill-color: red;"
            + "}\n";

    // RUN THE APPLICATION
    /**
     * Method called at the start of the application. Create the scene and add
     * it to the stage.
     *
     * @param stage JavaFX stage
     * @throws IOException may be thrown when reading the fxml file
     */
    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        stage.setTitle("DELIVERIF");
        App.scene = new Scene(loadFXML("menuPage"));
        App.stage.setScene(scene);
        App.stage.show();
        KeyboardEventManager kem = new KeyboardEventManager();
    }

    /**
     * Method called at the end of the application. Stops the application and
     * makes sure that all threads are stopped.
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        MenuPageController.stopPathThread();
        super.stop();
    }

    /**
     * Main method. Just call the method launch.
     *
     * @param args arguments of the application. (no args needed)
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    // PRIVATE STATIC METHODS
    /**
     * Used to load the FXML file from the app resources by its name. Returns
     * the Parent instance of the scene.
     *
     * @param fxml name of the fxml file in the resources of the app.
     * @return Parent used then to create the scene
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // PUBLIC STATIC METHODS 
    // LOAD THE XML FILES
    
    /**
     * Shows a dialogue window to choose a xml map file and returns the map
     * created with this file.
     *
     * @param reader XmlReader to read the map file.
     * @return model.map.Map instance of the map. Null if the reading operation failed. Empty if cancelled.
     * @throws IOException
     */
    public static Map choseMapFile(XmlReader reader) throws IOException {

        JFileChooser dialogue = new JFileChooser(new File("./src/main/resources/deliverif/app/fichiersXML2020"));

        PrintWriter out;
        String filename;
        File file;

        if (dialogue.showOpenDialog(null)
                == JFileChooser.APPROVE_OPTION) {
            file = dialogue.getSelectedFile();
            out = new PrintWriter(new FileWriter(file.getPath(), true));
            filename = file.getPath();
            out.close();
        } else {
            //Canceled
            return new Map();
        }

        if (!reader.readMap(filename)) {
            return null;
        }

        return reader.getMap();
    }
    
    /**
     * Shows a dialogue window to choose a xml request file and returns the planning request
     * created with this file.
     * 
     * @param reader XmlReader to read the request file.
     * @return model.map.PlanningRequest instance of the request. Null if the reading operation failed. Empty if cancelled.
     * @throws IOException
     */
    public static PlanningRequest choseRequestFile(XmlReader reader) throws IOException {

        JFileChooser dialogue = new JFileChooser(new File("./src/main/resources/deliverif/app/fichiersXML2020"));

        PrintWriter out;

        String filename;
        File file;

        if (dialogue.showOpenDialog(null)
                == JFileChooser.APPROVE_OPTION) {
            file = dialogue.getSelectedFile();
            out = new PrintWriter(new FileWriter(file.getPath(), true));
            filename = file.getPath();
            out.close();
        } else {
            //Cancelled
            return new PlanningRequest();
        }
        PlanningRequest pr = reader.readRequest(filename);
        return pr;
    }

    // GETTERS
    
    /**
     * Return the JavaFX scene of the application.
     * @return Scene (JavaFX Scene)
     */
    public static Scene getScene() {
        return scene;
    }
}
