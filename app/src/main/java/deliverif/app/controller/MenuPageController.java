/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.model.map.Intersection;
import deliverif.app.model.map.Map;
import deliverif.app.model.request.PlanningRequest;
import deliverif.app.view.App;
import java.io.IOException;
import java.util.EnumSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.InteractiveElement;

/**
 *
 * @author fabien
 */
public class MenuPageController {
        @FXML
    private Button loadCityMapButton;
    
    @FXML
    private Button loadRequestButton;
    
    @FXML
    private Button computeTourButton;
    
    @FXML
    private Button editTourButton;
    
    @FXML 
    private Pane mapPane;
    
    private XmlReader xmlReader = new XmlReader();
    
    private Map map;
    
    private PlanningRequest planningRequest;
    
    private Graph graph;
    
    private FxViewPanel panel;
    
    private SpriteManager sman;
    
    
    
    @FXML
    private void loadCityMapAction() throws IOException {
        System.out.println("loadCityMapAction");
        this.map = App.choseMapFile(this.xmlReader);
        this.chargerGraph(this.map);
        this.graph.setAttribute("ui.stylesheet", App.styleSheet);
        Viewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        panel = (FxViewPanel)viewer.addDefaultView(false); 
        panel.enableMouseOptions();
        panel.setMouseManager(new MouseOverMouseManager(EnumSet.of(InteractiveElement.EDGE, InteractiveElement.NODE, InteractiveElement.SPRITE)));
        mapPane.getChildren().add(panel);
    }
    
    @FXML
    private void loadRequestAction() throws IOException {
        System.out.println("loadRequestAction");
        if(this.xmlReader.getMap() == null) {
            System.out.println("Il faut charger une map avant");
            return;
        }
        this.chargerPlanningRequests();
        System.out.println(this.planningRequest);
    }
    
    @FXML
    private void computeTourAction() throws IOException {
        System.out.println("computeTourAction");
    }
    
    @FXML
    private void editTourAction() throws IOException {
        System.out.println("editTourAction");
    }
    
    private void chargerGraph(Map map) {
        this.graph = new SingleGraph("Graph test 1");
        
        map.getIntersections().entrySet().forEach((mapentry) -> {
            String nodeId = mapentry.getKey().toString();
            Intersection intersection = (Intersection) mapentry.getValue();
            graph.addNode(nodeId);
            graph.getNode(nodeId).setAttribute("xy", intersection.getLongitude(), intersection.getLatitude());
        });

        map.getSegments().forEach((s) -> {
            String origin = s.getOrigin().getId().toString();
            String destination = s.getDestination().getId().toString();
            try {
                graph.addEdge(origin + destination, origin, destination);
            } catch (EdgeRejectedException | ElementNotFoundException | IdAlreadyInUseException e) {
                //System.out.println("Error edge " + origin + " -> " + destination);
            }
        });
    }
    
    private void chargerPlanningRequests() throws IOException {
        sman = new SpriteManager(this.graph);
        this.planningRequest = App.choseRequestFile(this.xmlReader);
        Intersection depot = planningRequest.getDepot().getAddress();
        Sprite depotSprite = sman.addSprite(depot.getId().toString());
        depotSprite.setAttribute("ui.class", "depotSprite");
        depotSprite.setPosition(depot.getLongitude(), depot.getLatitude(), 0);
        planningRequest.getRequests().stream().map((r) -> {
            Intersection pickupAddress = r.getPickupAddress();
            Sprite pickupAddressSprite = sman.addSprite(pickupAddress.getId().toString());
            pickupAddressSprite.setAttribute("ui.class", "pickupSprite");
            pickupAddressSprite.setPosition(pickupAddress.getLongitude(), pickupAddress.getLatitude(), 0);
            return r;
        }).forEachOrdered((r) -> {
            Intersection deliveryAdress = r.getDeliveryAddress();
            Sprite deliveryAdressSprite = sman.addSprite(deliveryAdress.getId().toString());
            deliveryAdressSprite.setAttribute("ui.class", "deliverySprite");
            deliveryAdressSprite.setPosition(deliveryAdress.getLongitude(), deliveryAdress.getLatitude(), 0);
        });
    }
}
