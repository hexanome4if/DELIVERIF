package deliverif.app.view;

import deliverif.app.controller.*;
import deliverif.app.model.map.Intersection;
import deliverif.app.model.map.Map;
import deliverif.app.model.map.Segment;
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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JFileChooser;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.InteractiveElement;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("baseTemplate"));
        
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

    public static void readTest() throws IOException {
        XmlReader reader = new XmlReader();
        Map map = choseMapFile(reader);
    }

    public static void main(String[] args) throws IOException{
        
        System.setProperty("org.graphstream.ui", "javafx"); 
        Graph graph = new SingleGraph("Graph test 1");

        XmlReader reader = new XmlReader();
        Map map = choseMapFile(reader);

        Iterator iterator = map.getIntersections().entrySet().iterator();
        while (iterator.hasNext()) {
          HashMap.Entry mapentry = (HashMap.Entry) iterator.next();          
          String nodeId = mapentry.getKey().toString();
          Intersection intersection = (Intersection) mapentry.getValue();
          graph.addNode(nodeId);
          graph.getNode(nodeId).setAttribute("xy", intersection.getLatitude(), intersection.getLongitude());   
        } 

        for(Segment s : map.getSegments()) {
            String origin = s.getOrigin().getId().toString();
            String destination = s.getDestination().getId().toString();
            try {
                graph.addEdge(origin+destination, origin, destination);
            } catch(Exception e) {
                //System.out.println("Error edge " + origin + " -> " + destination);
            }
        }

        Viewer viewer = graph.display();
        viewer.disableAutoLayout();
        viewer.getDefaultView().enableMouseOptions();
        viewer.getDefaultView().setMouseManager(new MouseOverMouseManager(EnumSet.of(InteractiveElement.EDGE, InteractiveElement.NODE, InteractiveElement.SPRITE)));
        
        /*for(Intersection i : map.getIntersections().values()) {
            graph.addNode(i.getId().toString());
        }*/
        /*graph.addNode("A" );
        graph.addNode("B" );
        graph.addNode("C" );
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        
        graph.addEdge( "AB", "A", "B" );
        graph.addEdge( "BC", "B", "C" );
        graph.addEdge( "CA", "C", "A" );
        Node A = graph.getNode("A");
        Node B = graph.getNode("B");
        Node C = graph.getNode("C");
        Edge AB = graph.getEdge("AB");
        System.out.println(A.getId());
        System.out.println(A.getDegree());
        System.out.println(AB.isDirected());
        
        for(Node n:graph) {
            System.out.println(n.getId());
        }*/
        //Viewer viewer = graph.display();
        //viewer.disableAutoLayout();
        /*A.setAttribute("xyz", 1, 3, 0);
        B.setAttribute("xyz", 1, 2, 0);
        C.setAttribute("xyz", 2, 3, 0);*/
    }

}