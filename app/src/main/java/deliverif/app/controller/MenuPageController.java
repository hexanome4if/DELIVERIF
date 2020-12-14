package deliverif.app.controller;

import deliverif.app.controller.Command.AddRequestCommand;
import deliverif.app.controller.Command.ListOfCommands;
import deliverif.app.controller.Command.RemoveRequestCommand;
import deliverif.app.controller.Command.SwapRequestCommand;
import deliverif.app.controller.Observer.Observable;
import deliverif.app.controller.Observer.Observer;
import deliverif.app.controller.State.InitialState;
import deliverif.app.controller.State.State;
import deliverif.app.controller.thread.*;
import deliverif.app.controller.tsp.TourGenerator;
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
import java.util.Collections;
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
 * @author H4314
 */
public class MenuPageController implements Observer {

    //PRIVATE FXML ATTRIBUTES
    
    /**
    * Button used to load an xml map
    */
    @FXML
    private Button loadCityMapButton;

    /**
    * Button used to load an xml request
    */
    @FXML
    private Button loadRequestButton;

    /**
    * Button used to compute a tour
    */
    @FXML
    private Button computeTourButton;

    /**
    * Button used to add a request in an existing tour
    */
    @FXML
    private Button addRequestButton;

    /**
    * Button used to delete a request in an existing tour
    */
    @FXML
    private Button deleteRequestButton;

    /**
    * Button used to swap 2 requests
    */
    @FXML
    private Button swapRequestButton;
    
    /**
    * Button used to stop the algorithm
    */
    @FXML
    private Button stopResearchButton;

    /**
    * Panel of the map
    */
    @FXML
    private AnchorPane mapPane;

    /**
    * Text inside the map panel
    */
    @FXML
    private Text loadMapText;

    /**
    * Text "Selection :"
    */
    @FXML
    private Text selectionText;

    /**
    * Text under "Selection", information of the selected element
    */
    @FXML
    private Text infosText;

    /**
    * Text "Tour infos"
    */
    @FXML
    private Text infosTextTour1;

    /**
    * Text under "Tour infos", information about the current tour
    */
    @FXML
    private Text infosTextTour2;

    /**
    * Text informations about the selected segment
    */
    @FXML
    private Text segmentNameText;

    /**
    * List of requests
    */
    @FXML
    private ListView<Text> requestList;

    /**
    * Streets list of the selected path
    */
    @FXML
    private ListView<String> streetsList;
        
    /**
    * Indicator about the progression of the tour compute
    */
    @FXML
    private ProgressIndicator progressIndicator;

    /**
    * Panel of the progression and compute time
    */
    @FXML
    private AnchorPane timerPane;

    /**
    * Text of the time
    */
    @FXML
    private Text timerText;

    //PRIVATE ATTRIBUTES
    
    /**
    * Map
    */
    private Map map;

    /**
    * Planning request loaded
    */
    private PlanningRequest planningRequest = null;

    /**
    * Graph loaded
    */
    private Graph graph = null;

    /**
    * From GraphStream to display the map on a panel
    */
    private FxViewPanel panel;

    /**
    * Sprite manager (requests points)
    */
    private SpriteManager sman;

    /**
    * Graph processor
    */
    private GraphProcessor graphProcessor;

    /**
    * Tour loaded
    */
    private Tour tour = null;

    /**
    * Edges currently selected
    */
    private final String[] selectedEdges = null;

    /**
    * Edges of the graph
    */
    private List<String> graphEdges = new ArrayList<>();

    /**
    * Node currently selected
    */
    private volatile String selectedNode = null;

    /**
    * List of commands
    */
    private ListOfCommands loc = null;

    /**
    * CUrrent state of the Application
    */
    private State currentState;

    /**
    * Used to read xml files
    */
    private final XmlReader xmlReader = new XmlReader();

    /**
    * Thread to display the selected path
    */
    private static PathThread pathThread = null;

    /**
    * Thread to compute the tour
    */
    private static ComputeTourThread computeTourThread = null;

    /**
    * Thread to display the timer and progression
    */
    private static TimerThread timerThread = null;

    /**
    * Instance (singleton)
    */
    private static MenuPageController instance = null;

    //CONSTRUCTOR

    /**
     * Constructor of the MenuPageController, used only when we lauched the app (Singleton pattern)
     */
    public MenuPageController() {
        loc = new ListOfCommands();
        instance = this;
        currentState = new InitialState(this);
    }

    //PUBLIC METHODS

    /**
     * Return the instance of MenuPageController (Singleton pattern)
     * @return MenuPageCntroller instance
     */
    public static MenuPageController getInstance() {
        return instance;
    }

    /**
     * Set current state
     * @param s State we want to set
     */
    public void setCurrentState(State s) {
        currentState = s;
    }

    /**
     * Init the UI
     */
    public void initUI() {
        this.planningRequest = null;
        this.tour = null;
        this.requestList.getItems().clear();
        this.infosText.setText("");
        this.infosTextTour1.setText("Tour infos = ");
        this.infosTextTour2.setText("");
        this.addRequestButton.setVisible(false);
        this.deleteRequestButton.setVisible(false);
        stopPathThread();
        if (this.graphEdges != null) {
            for (String edgeId : graphEdges) {
                graph.getEdge(edgeId).setAttribute("ui.class", "default");
            }
        }
        this.graphEdges = new ArrayList<>();

    }

    /**
     * Update the selection, called by the MouseOverMouseManager
     * @param element GraphicElement Element that was clicked.
     */
    public void updateSelection(GraphicElement element) {

        System.out.println("UPDATE SELECTION");

        // EDGES, when we click on an edge
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
                            this.setSelectedPath(num); //Select the good path
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
                segmentNameText.setText(name); //Set the segment selected
                sman.removeSprite("segmentSprite");

                Sprite segmentSprite = sman.addSprite("segmentSprite");

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

        if (this.planningRequest == null) { // We need a loaded planning request
            return;
        }

        if (element.getSelectorType() == Selector.Type.NODE) { //Update the selected node
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
        this.currentState.selectSprite(element.getId()); //Update the selected sprite
        this.currentState.selectNode(element.getId());
    }

    /**
     * Load a map using XML reader
     * @throws IOException
     */
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
        this.graph.setAttribute("ui.stylesheet", App.STYLESHEET);

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

    /**
     * Load a Request using XML reader
     * @throws IOException
     */
    public void loadRequest() throws IOException {
        System.out.println("loadRequestAction");
        if (map == null) {
            if (map == null) {
                showErrorAlert("Load a map", "You need to load a city map first");
                return;
            }
            System.out.println("Il faut charger une map avant");
            return;
        }
        this.chargerPlanningRequests();
        //System.out.println(this.planningRequest);
    }

    /**
     * COmpute a tour
     */
    public void computeTour() {

        if (map == null) {
            showErrorAlert("Load a map", "You need to load a city map first");
            return;
        }
        if (this.planningRequest == null) {
            showErrorAlert("Load a request", "You need to load a request first");
            return;
        }

        addRequestMode();
        System.out.println("computeTourAction");
        //tour = graphProcessor.optimalTour(this.planningRequest);
        TourGenerator tourGenerator = graphProcessor.optimalTour(planningRequest);
        System.out.println("Optimal");
        tourGenerator.addObserver(this);
        stopResearchButton.setVisible(true);
        computeTourThread = new ComputeTourThread(this);
        computeTourThread.start();
        timerThread = new TimerThread(this);
        timerThread.start();
    }

    /**
     * Render a tour
     */
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

        distance = distance / 1000;

        int duration = time / 60; //minutes
        int hours = duration / 60;
        int mins = duration - (60 * hours);
        //System.out.println("hours/" + duration + "/ min/" + min + "/ mins/" + mins);

        Date departure = this.tour.getDepartureTime();
        Date arrival = this.tour.getArrivalTime();

        this.infosTextTour1.setText("TOUR = " + String.format("%.03f", distance) + " km / " + hours + "h" + mins + "min");
        this.infosTextTour2.setText("from " + dtf.format(departure) + " to " + dtf.format(arrival));

    }

    /**
     * Set the selected sprite
     * @param spriteId new sprite selected
     */
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

    /**
     * Return a random color
     * @return an RGB color --> index 0 = RED, index 1 = Green, index 2 = Blue
     */
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

    /**
     * Stop the path thread
     */
    public static void stopPathThread() {
        if (pathThread != null) {
            pathThread.end();
            while (!pathThread.isIsFinished()) {
            }
        }
    }

    /**
     * Remove the selected request
     */
    public void removeRequest() {
        System.out.println("Try to remove");
        if (selectedNode == null) {
            return;
        }
        System.out.println("Node selected");
        Request selectedRequest = null;
        if (this.planningRequest.getDepot().getAddress().getId().toString().equals(this.selectedNode)) {
            showErrorAlert("Suppression error", "Impossible to remove the deposit");
            return;
        }
        if (this.planningRequest.getRequests().size() == 1) {
            showErrorAlert("Suppression error", "Impossible to remove the request because it's the last one");
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
            System.out.println("Request not found");
            return;
        }
        System.out.println("Found request");
        stopPathThread();
        RemoveRequestCommand rr = new RemoveRequestCommand(graphProcessor, tour, selectedRequest);
        sman.removeSprite("bigSprite");
        loc.addCommand(rr);

    }

    /**
     * Show an info alert
     * @param title title of the info
     * @param content content of the info
     */
    public void schowInfoAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Start adding a request
     */
    public void startAddRequest() {
        System.out.println("Start add request");
        this.addRequestMode();
        this.schowInfoAlert("Select Pickup point", "Please select a pickup point on the map");
    }

    /**
     * Start swap 2 requests
     */
    public void startSwapRequest() {
        System.out.println("Start swap request");
        this.addRequestMode();
        this.schowInfoAlert("Select a first point to swap", "Please select a point on the tour");
    }

    /**
     * Add a request to the current tour
     * @param pickupId
     * @param deliveryId
     * @param pickupDuration
     * @param deliveryDuration
     */
    public void addRequest(String pickupId, String deliveryId, int pickupDuration, int deliveryDuration) {

        Intersection pickup = map.getIntersectionParId(Long.parseLong(pickupId));
        Intersection delivery = map.getIntersectionParId(Long.parseLong(deliveryId));
        Request r = new Request(pickup, delivery, pickupDuration, deliveryDuration);
        AddRequestCommand ar = new AddRequestCommand(graphProcessor, tour, r);
        loc.addCommand(ar);
        this.defaultMode();
    }

    /**
     * Swap 2 requests
     * @param firstId First request, ID of the departure
     * @param secondId Second request, ID of the departure
     */
    public void swapRequest(String firstId, String secondId) {
        ArrayList<Long> requestIds = new ArrayList<>();
        for (Path p : tour.getPaths()) {
            requestIds.add(p.getDeparture().getId());
        }
        Collections.swap(requestIds, requestIds.indexOf(Long.parseLong(firstId)), requestIds.indexOf(Long.parseLong(secondId)));
        SwapRequestCommand sr = new SwapRequestCommand(graphProcessor, tour, requestIds);
        loc.addCommand(sr);
        this.defaultMode();
    }

    /**
     * Remove the sprite of a request
     * @param r Request removed
     */
    public void removeSpriteRequest(Request r) {
        String pickupId = r.getPickupAddress().getId().toString();
        String deliveryId = r.getDeliveryAddress().getId().toString();
        sman.removeSprite(pickupId);
        sman.removeSprite(deliveryId);
    }

    /**
     * Display a request on the map
     * @param r
     */
    public void displayRequest(Request r) {
        int[] rgb = randomColorSprite();
        String color = "fill-color: rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ");";
        Intersection pickupAddress = r.getPickupAddress();
        Sprite pickupAddressSprite = sman.addSprite(pickupAddress.getId().toString());
        pickupAddressSprite.setAttribute("ui.class", "pickupSprite");
        pickupAddressSprite.setAttribute("ui.style", color);
        pickupAddressSprite.setPosition(pickupAddress.getLongitude(), pickupAddress.getLatitude(), 0);

        Intersection deliveryAdress = r.getDeliveryAddress();
        Sprite deliveryAdressSprite = sman.addSprite(deliveryAdress.getId().toString());
        deliveryAdressSprite.setAttribute("ui.class", "deliverySprite");
        deliveryAdressSprite.setAttribute("ui.style", color);
        deliveryAdressSprite.setPosition(deliveryAdress.getLongitude(), deliveryAdress.getLatitude(), 0);
    }

    @Override
    public void update(Observable observed, Object arg) {
        if (observed instanceof Tour) {
            Tour t = (Tour) observed;
            if (t != tour) {
                return;
            }
            this.planningRequest = t.getPr();
            renderTour();
        } else if (observed instanceof TourGenerator) {
            boolean isFinished = (boolean) arg;
            this.requestList.getItems().clear();
            if (isFinished) {
                defaultMode();
                stopResearchButton.setVisible(false);
                this.timerPane.setVisible(false);
                this.addRequestButton.setVisible(true);
                this.swapRequestButton.setVisible(true);

            }
            TourGenerator tourGenerator = (TourGenerator) observed;
            this.tour = tourGenerator.getTour();
            this.tour.addObserver(this);
            this.planningRequest = tour.getPr();
            renderTour();
        }
    }

    /**
     * Undo an action
     */
    public void undo() {
        loc.undo();
    }

    /**
     * Redo an action
     */
    public void redo() {
        loc.redo();
    }

    /**
     * Get the current state
     * @return current state
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Get the current graph
     * @return graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Get the current tour
     * @return tour
     */
    public Tour getTour() {
        return tour;
    }

    /**
     * Get the current Compute tour thread
     * @return compute tour thread
     */
    public static ComputeTourThread getComputeTourThread() {
        return computeTourThread;
    }

    /**
     * Get the current graph processor
     * @return graph processor
     */
    public GraphProcessor getGraphProcessor() {
        return graphProcessor;
    }

    /**
     * Get the current selected node
     * @return selected node
     */
    public String getSelectedNode() {
        return selectedNode;
    }

    /**
     * Get the current selected planning request
     * @return planning request
     */
    public PlanningRequest getPlanningRequest() {
        return planningRequest;
    }

    /**
     * Get the timer pane
     * @return timer pane
     */
    public AnchorPane getTimerPane() {
        return timerPane;
    }

    /**
     * Get the timer text
     * @return timer text
     */
    public Text getTimerText() {
        return timerText;
    }
        
    /**
     * Get the progress indicator
     * @return progress indicator
     */
    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }
    
    public Button getStopResearchButton() {
        return stopResearchButton;
    }
    

    /**
     * Set the selected node
     * @param selectedNode id of the node
     */
    public void setSelectedNode(String selectedNode) {
        this.selectedNode = selectedNode;
    }

    /**
     * Check if the node is on the tour
     * @param nodeId
     * @return true if the node is on the tour
     */
    public boolean isNodeOnTour(String nodeId) {
        Sprite sprite = sman.getSprite(nodeId);
        return sprite != null;
    }

    //PUBLIC FXML METHODS

    /**
     * Action when we click on remove request
     * @param arg0
     */
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
    
    /**
     * Action when we click on load a map
     * @throws IOException
     */
    @FXML
    private void loadCityMapAction() throws IOException {
        currentState.loadMap();
    }

    /**
     * Action when we click on load a request
     * @throws IOException
     */
    @FXML
    private void loadRequestAction() throws IOException {
        currentState.loadRequest();
    }

    /**
     * Action when we click on compute a tour
     * @throws IOException
     */
    @FXML
    private void computeTourAction() throws IOException {
        currentState.computeTour();
    }

    /**
     * Action when we click on add a request
     * @throws IOException
     */
    @FXML
    private void addRequestAction() throws IOException {
        currentState.startAddRequest();
    }

    /**
     * Action when we click on delete a request
     * @throws IOException
     */
    @FXML
    private void deleteRequestAction() throws IOException {
        System.out.println("deleteRequestAction");
        this.currentState.removeRequest();
        this.deleteRequestButton.setVisible(false);
    }

    /**
     * Action when we want to render a tour
     */
    @FXML
    private void renderTourAction() {
        defaultMode();
        this.stopResearchButton.setVisible(false);
        this.timerPane.setVisible(false);
        System.out.println("cancel");
        computeTourThread.stop();
        tour.addObserver(this);
        renderTour();
        this.addRequestButton.setVisible(true);
        this.swapRequestButton.setVisible(true);
    }

    /**
     * Action when we click on swap requests
     */
    @FXML
    private void swapRequestAction() {
        System.out.println("swapRequestAction");
        currentState.startSwapRequest();

    }

    //PRIVATE METHODS
    
    /**
     * Load a graph
     * @param map current map
     */
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

    /**
     * Set the selected path
     * @param num position of the path in the tour (start at 1)
     */
    private void setSelectedPath(int num) {
        // complete list
        completePathList(num);
        try {
            stopPathThread();
            pathThread = new PathThread(this, num);
            pathThread.start();
        } catch (Exception e) {
            System.out.println("Error in setSelectedPath " + e);
        }

    }

    /**
     * Complete the path list
     * @param num position of the path in the tour (start at 1)
     */
    private void completePathList(int num) {
        streetsList.getItems().clear();
        Path p = tour.getPaths().get(num - 1);
        for (int i = p.getSegments().size() - 1; i >= 0; i--) {
            Segment s = p.getSegments().get(i);
            if (!this.streetsList.getItems().contains(s.getName()) && (s.getName() != "")) {
                this.streetsList.getItems().add(s.getName());
            }
        }
    }

    /**
     * Load a planning request
     * @throws IOException
     */
    private void chargerPlanningRequests() throws IOException {
        PlanningRequest pr = App.choseRequestFile(this.xmlReader);
        if (pr == null) {
            showErrorAlert("Bad file", "FORMAT ERRORS ON REQUEST FILE \nLoad another file");
            return;
        } else if (pr.isEmpty()) {
            showErrorAlert("Bad file", "REQUEST FILE DO NOT MATCH WITH THE MAP \nLoad another file");

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
            this.displayRequestWithAddToListView(r, cpt);
            cpt++;
        }
    }

    /**
     * Display a request on the map and add the request to the list view
     * @param r request to display
     * @param cpt position on the list view
     */
    private void displayRequestWithAddToListView(Request r, int cpt) {
        Text txt;
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
    }

    /**
     * Reset the appearance of the edge
     * @param edgeId
     */
    private void resetEdge(String edgeId) {
        graph.getEdge(edgeId).setAttribute("ui.class", "default");
    }

    /**
     * Set sprite appearance as a selected sprite
     * @param spriteId
     */
    private void setBigSprite(String spriteId) {
        for (Sprite s : sman.sprites()) {
            if (((String) s.getAttribute("ui.class")).contains("Selected")) {
                s.setAttribute("ui.class", ((String) s.getAttribute("ui.class")).replace("Selected", ""));
            }
        }
        Sprite sprite = sman.getSprite(spriteId);
        sprite.setAttribute("ui.class", ((String) sprite.getAttribute("ui.class")) + "Selected");
    }

    /**
     * Show an error alert
     * @param title title of the error
     * @param content content of the error
     */
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Currently adding a request, all button aren't visible and the list view is not selectable
     */
    private void addRequestMode() {
        this.loadCityMapButton.setVisible(false);
        this.loadRequestButton.setVisible(false);
        this.computeTourButton.setVisible(false);
        this.addRequestButton.setVisible(false);
        this.deleteRequestButton.setVisible(false);
        this.requestList.setMouseTransparent(true);
        this.requestList.setFocusTraversable(false);
        this.swapRequestButton.setVisible(false);

    }

    /**
     * Default appearence mode of the application
     */
    private void defaultMode() {
        this.loadCityMapButton.setVisible(true);
        this.loadRequestButton.setVisible(true);
        this.computeTourButton.setVisible(true);
        this.addRequestButton.setVisible(true);
        this.requestList.setMouseTransparent(false);
        this.requestList.setFocusTraversable(true);
        this.swapRequestButton.setVisible(true);
    }
}