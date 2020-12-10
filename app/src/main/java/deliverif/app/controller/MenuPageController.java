/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.controller.Command.AddRequestCommand;
import deliverif.app.controller.Command.ListOfCommands;
import deliverif.app.controller.Command.RemoveRequestCommand;
import deliverif.app.controller.Observer.Observable;
import deliverif.app.controller.Observer.Observer;
import deliverif.app.controller.State.InitialState;
import deliverif.app.controller.State.State;
import deliverif.app.model.graph.Tour;
import deliverif.app.model.map.Intersection;
import deliverif.app.model.map.Map;
import deliverif.app.model.map.Segment;
import deliverif.app.model.request.Path;
import deliverif.app.model.request.PlanningRequest;
import deliverif.app.model.request.Request;
import deliverif.app.view.App;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.stylesheet.Selector;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.InteractiveElement;

/**
 *
 * @author fabien
 */
public class MenuPageController implements Observer {

    //PRIVATE FXML ATTRIBUTES
    @FXML
    private Button loadCityMapButton;

    @FXML
    private Button loadRequestButton;

    @FXML
    private Button computeTourButton;

    @FXML
    private Button addRequestButton;

    @FXML
    private Button deleteRequestButton;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private Text loadMapText;

    @FXML
    private Text selectionText;

    @FXML
    private Text infosText;

    @FXML
    private Text infosTextTour1;

    @FXML
    private Text infosTextTour2;

    @FXML
    private Text segmentNameText;

    @FXML
    private ListView<Text> requestList;

    @FXML
    private ListView<Text> pathList;

    @FXML
    private ProgressIndicator progressIndicator;

    //PRIVATE ATTRIBUTES
    private Map map;

    private PlanningRequest planningRequest = null;

    private Graph graph = null;

    private FxViewPanel panel;

    private SpriteManager sman;

    private GraphProcessor graphProcessor;

    private Tour tour = null;

    private String[] selectedEdges = null;

    private List<String> graphEdges = new ArrayList<>();

    private volatile String selectedNode = null;

    private ListOfCommands loc = null;

    private State currentState;

    private final XmlReader xmlReader = new XmlReader();

    private static PathThread pathThread = null;

    // Instance (Singleton)
    private static MenuPageController instance = null;

    //CONSTRUCTOR
    public MenuPageController() {
        loc = new ListOfCommands();
        instance = this;
        currentState = new InitialState(this);
    }

    //PUBLIC METHODS
    public static MenuPageController getInstance() {
        return instance;
    }

    public void setCurrentState(State s) {
        currentState = s;
    }

    public void initUI() {
        this.planningRequest = null;
        this.tour = null;
        this.requestList.getItems().clear();
        this.infosText.setText("");
        this.infosTextTour1.setText("Tour infos = ");
        this.infosTextTour2.setText("");
        this.addRequestButton.setVisible(false);
        this.deleteRequestButton.setVisible(false);
        stopThread();
        if (this.graphEdges != null) {
            for (String edgeId : graphEdges) {
                graph.getEdge(edgeId).setAttribute("ui.class", "default");
            }
        }
        this.graphEdges = new ArrayList<>();

    }

    public void updateSelection(GraphicElement element) {

        System.out.println("UPDATE SELECTION");

        // EDGES
        if (element.getSelectorType() == Selector.Type.EDGE) {
            Edge edge = graph.getEdge(element.getId());
            for (String id : this.graphEdges) {
                if (element.getId().equals(id)) {
                    System.out.println(id + " is on the path");
                    for (Text t : this.requestList.getItems()) {
                        if (t.getId().contains(id)) {
                            this.requestList.getSelectionModel().select(t);
                            String[] ids = t.getId().split("#");
                            int endNum = t.getText().indexOf("]", 2);
                            String numString = t.getText().substring(1, endNum);
                            int num = Integer.parseInt(numString);
                            this.setSelectedPath(num);
                            setBigSprite(tour.getPaths().get(num - 1).getDeparture().getId().toString());
                            break;
                        }
                    }

                    break;
                }
            }
            String name = (String) edge.getAttribute("segment.name");
            System.out.println(name);
            if (name != null) {
                segmentNameText.setText(name);
                sman.removeSprite("segmentSprite");

                Sprite segmentSprite = sman.addSprite("segmentSprite");
                double x, y, z;

                segmentSprite.setAttribute("ui.class", "segmentSprite");
                segmentSprite.setAttribute("ui.style", "fill-color: green;");
                Node origin = edge.getNode0();
                Node dest = edge.getNode1();
                Float longitude = (((Float) origin.getAttribute("x")) + ((Float) dest.getAttribute("x"))) / 2;
                Float latitude = (((Float) origin.getAttribute("y")) + ((Float) dest.getAttribute("y"))) / 2;
                segmentSprite.setPosition(longitude, latitude, 0);
            }
            return;
        }
        // NODE

        if (this.planningRequest == null) {
            return;
        }

        if (element.getSelectorType() == Selector.Type.NODE) {
            this.currentState.selectNode(element.getId());
            this.selectedNode = element.getId();
            System.out.println("CHANGE : " + this.selectedNode);
            return;
        }

        if (element.getSelectorType() != Selector.Type.SPRITE) {
            return;
        }

        // SPRITE
        String idElement = element.getId();
        if (idElement.equals("segmentSprite")) {
            return;
        }
        this.currentState.selectNode(element.getId());
    }

    public void loadMap() throws IOException {
        System.out.println("loadCityMapAction");
        Map m = App.choseMapFile(this.xmlReader);

        if (m == null) {
            showErrorAlert("Bad file", "FORMAT ERRORS ON MAP FILE - Load another file");
            return;
        } else if (m.isEmpty()) {
            return;
        } else {
            this.map = m;
        }

        this.chargerGraph(this.map);
        this.graph.setAttribute("ui.stylesheet", App.styleSheet);
        //this.graph.setAutoCreate(true);
        //this.graph.setStrict(false);

        Viewer viewer = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        panel = (FxViewPanel) viewer.addDefaultView(false);
        panel.enableMouseOptions();
        panel.setMouseManager(new MouseOverMouseManager(EnumSet.of(InteractiveElement.EDGE, InteractiveElement.SPRITE, InteractiveElement.NODE), this));
        mapPane.getChildren().remove(loadMapText);
        mapPane.getChildren().add(panel);
        AnchorPane.setTopAnchor(panel, 1.0);
        AnchorPane.setLeftAnchor(panel, 1.0);
        AnchorPane.setRightAnchor(panel, 1.0);
        AnchorPane.setBottomAnchor(panel, 1.0);
        graphProcessor = new GraphProcessor(map);
        sman = new SpriteManager(this.graph);
    }

    public void loadRequest() throws IOException {
        System.out.println("loadRequestAction");
        if (this.xmlReader.getMap() == null) {
            System.out.println("Il faut charger une map avant");
            return;
        }
        this.chargerPlanningRequests();
        //System.out.println(this.planningRequest);
    }

    public void computeTour() {
        System.out.println("computeTourAction");
        tour = graphProcessor.optimalTour(this.planningRequest);
        tour.addObserver(this);

        renderTour();
        this.addRequestButton.setVisible(true);
    }

    public void renderTour() {

        for (String edgeId : graphEdges) {
            resetEdge(edgeId);
        }
        graphEdges.clear();

        this.requestList.getItems().clear();

        Text txt = null;
        int cpt = 1;
        SimpleDateFormat dtf = new SimpleDateFormat("HH:mm:ss");
        int[] rgb = randomColorSprite();

        for (Path p : tour.getPaths()) {
            String id = "";
            for (Segment s : p.getSegments()) {
                String originId = s.getOrigin().getId().toString();
                String destId = s.getDestination().getId().toString();
                Edge edge = graph.getEdge(originId + "|" + destId);
                if (edge != null) {
                    edge.setAttribute("ui.style", "fill-color: red;");
                    edge.setAttribute("ui.style", "size: 4px;");
                    edge.setAttribute("ui.class", "pathEdge");
                    this.graphEdges.add(edge.getId());
                    id = id + edge.getId() + "#";
                } else {
                    edge = graph.getEdge(destId + "|" + originId);
                    if (edge != null) {
                        edge.setAttribute("ui.style", "fill-color: red;");
                        edge.setAttribute("ui.style", "size: 4px;");
                        edge.setAttribute("ui.class", "pathEdge");
                        this.graphEdges.add(edge.getId());
                        id = id + edge.getId() + "#";
                    } else {
                        System.out.println("Edge not found");
                    }
                }
            }
            Long departId = p.getDeparture().getId();
            String typePoint = tour.getPr().researchTypeIntersection(departId);

            if (typePoint != "") {
                txt = new Text("[" + cpt + "] " + typePoint + " from " + dtf.format(p.getDepatureTime())
                        + " to " + dtf.format(p.getArrivalTime()) + " / " + String.format("%.03f", p.getLength() / 1000) + "km");
            } else {
                txt = new Text("[" + cpt + "] Depot from " + dtf.format(p.getDepatureTime())
                        + " to " + dtf.format(p.getArrivalTime()) + " / " + String.format("%.03f", p.getLength() / 1000) + "km");
            }

            txt.setFill(Color.rgb(rgb[0], rgb[1], rgb[2]));
            txt.setId(id);
            this.requestList.getItems().add(txt);
            cpt++;
        }

        float distance = this.tour.getTotalDistance();
        int time = this.tour.getTotalDuration(); //seconds

        System.out.println("getDuration/" + time);
        distance = distance / 1000;

        int duration = time / 60; //minutes
        System.out.println("Duration/" + duration);
        int hours = duration / 60;
        int mins = duration - (60 * hours);
        //System.out.println("hours/" + duration + "/ min/" + min + "/ mins/" + mins);

        Date departure = this.tour.getDepartureTime();
        Date arrival = this.tour.getArrivalTime();

        this.infosTextTour1.setText("TOUR = " + String.format("%.03f", distance) + " km / " + hours + "h" + mins + "min");
        this.infosTextTour2.setText("from " + dtf.format(departure) + " to " + dtf.format(arrival));

        System.out.println("compute tour done");
    }

    public void setSelectedSprite(String spriteId) {
        selectedNode = spriteId;
        Sprite sprite = sman.getSprite(spriteId);
        if (sprite == null) {
            return;
        }
        String spriteType = (String) sprite.getAttribute("ui.class");
        if (spriteType == null || spriteType == "segmentSprite") {
            return;
        }
        this.setBigSprite(spriteId);

        String longitude = "Longitude = ";
        String latitude = "Latitude = ";
        if (spriteType.equals("depotSprite")) {
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            String strDate = hourFormat.format(this.planningRequest.getDepot().getDepartureTime());
            this.infosText.setText("Departure time = " + strDate);
        } else {
            for (Request r : this.planningRequest.getRequests()) {
                String idPickupAddress = r.getPickupAddress().getId().toString();
                String idDeliveryAdress = r.getDeliveryAddress().getId().toString();
                if (idPickupAddress.equals(spriteId)) {
                    this.infosText.setText("Pickup duration = " + String.valueOf(r.getPickupDuration() / 60) + " min");
                    break;
                }
                if (idDeliveryAdress.equals(spriteId)) {
                    this.infosText.setText("Delivery duration = " + String.valueOf(r.getDeliveryDuration() / 60) + " min");
                    break;
                }
            }
        }

        if (tour != null) {
            for (int i = 0; i < tour.getPaths().size(); i++) {
                Path p = tour.getPaths().get(i);
                if (p.getDeparture().getId().toString().equals(spriteId)) {
                    this.setSelectedPath(i + 1);
                    for (Text t : this.requestList.getItems()) {
                        if (t.getText().contains("[" + (i + 1) + "]")) {
                            this.requestList.getSelectionModel().select(t);
                        }
                    }
                }
            }
            this.deleteRequestButton.setVisible(true);
        } else {
            Text spriteText = null;
            for (Text t : this.requestList.getItems()) {
                if (t.getId().equals(spriteId)) {
                    spriteText = t;
                }
            }
            this.requestList.getSelectionModel().select(spriteText);
        }

    }

    public int[] randomColorSprite() {

        Random rand = new Random();

        int min, max;
        min = 55;
        max = 200;

        int r = rand.nextInt((max - min) + 1) + min;
        int g = rand.nextInt((max - min) + 1) + min;
        int b = rand.nextInt((max - min) + 1) + min;

        int[] rgb = {r, g, b};
        return rgb;
    }

    public static void stopThread() {
        if (pathThread != null) {
            pathThread.end();
            while (!pathThread.isIsFinished()) {
            }
        }
    }

    public void removeRequest() {
        System.out.println("Try to remove");
        if (selectedNode == null) {
            return;
        }
        System.out.println("Node selected");
        Request selectedRequest = null;
        if(this.planningRequest.getDepot().getAddress().getId().toString().equals(this.selectedNode)) {
           showErrorAlert("Suppression error","Impossible to remove the deposit");
           return;
        }
        if(this.planningRequest.getRequests().size() == 1) {
            showErrorAlert("Suppression error","Impossible to remove the request because it's the last one");
            return;
        }
        for (Request r : this.planningRequest.getRequests()) {
            String idPickupAddress = r.getPickupAddress().getId().toString();
            String idDeliveryAdress = r.getDeliveryAddress().getId().toString();
            if (idPickupAddress.equals(selectedNode)) {
                selectedRequest = r;
                break;
            }
            if (idDeliveryAdress.equals(selectedNode)) {
                selectedRequest = r;
                break;
            }
        }
        if (selectedRequest == null) {
            return;
        }
        System.out.println("Found request");
        stopThread();
        RemoveRequestCommand rr = new RemoveRequestCommand(graphProcessor, tour, selectedRequest);
        sman.removeSprite(selectedRequest.getDeliveryAddress().getId().toString());
        sman.removeSprite(selectedRequest.getPickupAddress().getId().toString());
        sman.removeSprite("bigSprite");
        loc.addCommand(rr);
        rr.doCommand();

    }
    
    public void schowInfoAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();  
    }

    public void startAddRequest() {
        System.out.println("Start add request");
        this.addRequestMode();
        this.schowInfoAlert("Select Pickup point", "Please select a pickup point on the map");
    }

    public void addRequest(String pickupId, String deliveryId) {
        Intersection pickup = map.getIntersectionParId(Long.parseLong(pickupId));
        Intersection delivery = map.getIntersectionParId(Long.parseLong(deliveryId));
        Request r = new Request(pickup, delivery, 120, 67);
        AddRequestCommand ar = new AddRequestCommand(graphProcessor, tour, r);
        loc.addCommand(ar);
        this.defaultMode();
    }

    @Override
    public void update(Observable observed, Object arg) {
        Tour t = (Tour) observed;
        if (t != tour) {
            return;
        }
        renderTour();
    }

    public void undo() {
        loc.undo();
    }

    public void redo() {
        loc.redo();
    }

    public State getCurrentState() {
        return currentState;
    }

    public Graph getGraph() {
        return graph;
    }

    public Tour getTour() {
        return tour;
    }

    public GraphProcessor getGraphProcessor() {
        return graphProcessor;
    }

    public String getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(String selectedNode) {
        this.selectedNode = selectedNode;
    }

    public boolean isNodeOnTour(String nodeId) {
        Sprite sprite = sman.getSprite(nodeId);
        return sprite != null;
    }

    //PUBLIC FXML METHODS
    @FXML
    public void requestListClick(MouseEvent arg0) {

        if (this.tour == null) {
            System.out.println("clicked on " + requestList.getSelectionModel().getSelectedItem().getText());
            String spriteId = requestList.getSelectionModel().getSelectedItem().getId();
            this.currentState.selectNode(spriteId);
        } else {
            System.out.println("clicked on " + this.requestList.getSelectionModel().getSelectedItem().getText());
            int endNum = this.requestList.getSelectionModel().getSelectedItem().getText().indexOf("]", 2);
            int num = Integer.parseInt(this.requestList.getSelectionModel().getSelectedItem().getText().substring(1, endNum));
            String departureId = this.tour.getPaths().get(num - 1).getDeparture().getId().toString();
            this.currentState.selectNode(departureId);
            this.setSelectedPath(num);
        }
    }

    //PRIVATE FXML METHODS
    @FXML
    private void loadCityMapAction() throws IOException {
        currentState.loadMap();
    }

    @FXML
    private void loadRequestAction() throws IOException {
        currentState.loadRequest();
    }

    @FXML
    private void computeTourAction() throws IOException {
        currentState.computeTour();
    }

    @FXML
    private void addRequestAction() throws IOException {
        currentState.startAddRequest();
    }

    @FXML
    private void deleteRequestAction() throws IOException {
        System.out.println("deleteRequestAction");
        this.currentState.removeRequest();
        this.deleteRequestButton.setVisible(false);
    }

    //PRIVATE METHODS
    private void chargerGraph(Map map) {
        if (graph != null) {
            initUI();
        }

        this.graph = new SingleGraph("Graph test 1");

        map.getIntersections().entrySet().forEach((mapentry) -> {
            String nodeId = mapentry.getKey().toString();
            Intersection intersection = (Intersection) mapentry.getValue();
            graph.addNode(nodeId);
            graph.getNode(nodeId).setAttribute("x", intersection.getLongitude());
            graph.getNode(nodeId).setAttribute("y", intersection.getLatitude());

        });

        map.getSegments().forEach((s) -> {
            String origin = s.getOrigin().getId().toString();
            String destination = s.getDestination().getId().toString();
            try {
                graph.addEdge(origin + "|" + destination, origin, destination);
                graph.getEdge(origin + "|" + destination).setAttribute("segment.name", s.getName());
                graph.getEdge(origin + "|" + destination).setAttribute("ui.class", "default");
            } catch (EdgeRejectedException | ElementNotFoundException | IdAlreadyInUseException e) {
                //System.out.println("Error edge " + origin + " -> " + destination);
                Edge ed = graph.getEdge(destination + "|" + origin);
                if (ed != null) {
                    ed.setAttribute("ui.style", "size: 2px;");
                }
            }
        });
    }

    private void setSelectedPath(int num) {
        try {
            stopThread();
            pathThread = new PathThread(this, num);
            pathThread.start();
        } catch (Exception e) {
            System.out.println("Error in setSelectedPath " + e);
        }

    }

    private void chargerPlanningRequests() throws IOException {
        PlanningRequest pr = App.choseRequestFile(this.xmlReader);
        if (pr == null) {
            showErrorAlert("Bad file", "FORMAT ERRORS ON REQUEST FILE - Load another file");
            return;
        } else if (pr.isEmpty()) {
            return;
        }

        if (planningRequest != null) {
            initUI();

            ArrayList<String> spriteIds = new ArrayList<String>();
            for (Sprite s : this.sman.sprites()) {
                spriteIds.add(s.getId());
            }
            for (String id : spriteIds) {
                sman.removeSprite(id);
            }
        }

        this.planningRequest = pr;

        Text txt;
        Intersection depot = planningRequest.getDepot().getAddress();
        Sprite depotSprite = sman.addSprite(depot.getId().toString());
        depotSprite.setAttribute("ui.class", "depotSprite");
        depotSprite.setPosition(depot.getLongitude(), depot.getLatitude(), 0);
        txt = new Text("Depot");
        txt.setId(depotSprite.getId());
        txt.setFill(Color.RED);
        this.requestList.getItems().add(txt);
        int cpt = 1;
        for (Request r : planningRequest.getRequests()) {

            int[] rgb = randomColorSprite();
            String color = "fill-color: rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ");";
            Intersection pickupAddress = r.getPickupAddress();
            Sprite pickupAddressSprite = sman.addSprite(pickupAddress.getId().toString());
            pickupAddressSprite.setAttribute("ui.class", "pickupSprite");
            pickupAddressSprite.setAttribute("ui.style", color);
            pickupAddressSprite.setPosition(pickupAddress.getLongitude(), pickupAddress.getLatitude(), 0);
            txt = new Text("Pickup " + cpt);
            txt.setId(pickupAddressSprite.getId());
            txt.setFill(Color.rgb(rgb[0], rgb[1], rgb[2]));
            this.requestList.getItems().add(txt);

            Intersection deliveryAdress = r.getDeliveryAddress();
            Sprite deliveryAdressSprite = sman.addSprite(deliveryAdress.getId().toString());
            deliveryAdressSprite.setAttribute("ui.class", "deliverySprite");
            deliveryAdressSprite.setAttribute("ui.style", color);
            deliveryAdressSprite.setPosition(deliveryAdress.getLongitude(), deliveryAdress.getLatitude(), 0);
            txt = new Text("Delivery " + cpt);
            txt.setId(deliveryAdressSprite.getId());
            txt.setFill(Color.rgb(rgb[0], rgb[1], rgb[2]));
            this.requestList.getItems().add(txt);
            cpt++;
        }
    }

    private void resetEdge(String edgeId) {
        graph.getEdge(edgeId).setAttribute("ui.class", "default");
    }

    private void setBigSprite(String spriteId) {
        for (Sprite s : sman.sprites()) {
            if (((String) s.getAttribute("ui.class")).contains("Selected")) {
                s.setAttribute("ui.class", ((String) s.getAttribute("ui.class")).replace("Selected", ""));
            }
        }
        Sprite sprite = sman.getSprite(spriteId);
        sprite.setAttribute("ui.class", ((String) sprite.getAttribute("ui.class")) + "Selected");
    }

    // Show a Information Alert
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);

        alert.showAndWait();
    }
    
    private void addRequestMode() {
        this.loadCityMapButton.setVisible(false);
        this.loadRequestButton.setVisible(false);
        this.computeTourButton.setVisible(false);
        this.addRequestButton.setVisible(false);
        this.deleteRequestButton.setVisible(false);
        this.requestList.setMouseTransparent(true);
        this.requestList.setFocusTraversable(false);
    }
    
    private void defaultMode() {
        this.loadCityMapButton.setVisible(true);
        this.loadRequestButton.setVisible(true);
        this.computeTourButton.setVisible(true);
        this.addRequestButton.setVisible(true);
        this.requestList.setMouseTransparent(false);
        this.requestList.setFocusTraversable(true);
    }
}
