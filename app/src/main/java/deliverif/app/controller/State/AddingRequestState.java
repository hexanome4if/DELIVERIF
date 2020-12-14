/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.State;

import deliverif.app.controller.Command.ListOfCommands;
import deliverif.app.controller.Command.SelectNodeCommand;
import deliverif.app.controller.MenuPageController;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;

/**
 * State in which the user is adding a request to the current tour
 * @author zakaria
 */
public class AddingRequestState extends State {
    /**
     * List of commands to be able to undo and redo
     */
    private final ListOfCommands loc;
    /**
     * Pickup intersection id to add
     */
    private String pickupId;
    /**
     * Delivery intersection id to add
     */
    private String deliveryId;
    /**
     * Pickup duration to add (in seconds)
     */
    private int pickupDuration;
    /**
     * Delivery duration to add (in seconds)
     */
    private int deliveryDuration;

    /**
     * Start a state to add a request to the current tour
     *
     * @param mpc current view controller
     */
    public AddingRequestState(MenuPageController mpc) {
        super(mpc);
        loc = new ListOfCommands();
    }

    @Override
    public void addRequest() {
        if (pickupId == null || deliveryId == null) {
            System.out.println("Il manque des points de selection");
            return;
        }
        mpc.addRequest(pickupId, deliveryId, pickupDuration, deliveryDuration);
        mpc.setCurrentState(new TourComputedState(mpc));
        System.out.println("Requete ajoutée");
    }

    @Override
    public void selectNode(String nodeId) {
        if (mpc.isNodeOnTour(nodeId)) {
            this.mpc.schowInfoAlert("Point already on the tour", "Impossible to add, the point is already on the tour.");
            return;
        }
        if (pickupId == null || deliveryId == null) {
            //super.selectNode(nodeId);
            if (pickupId == null) {
                SelectNodeCommand snc = new SelectNodeCommand(this, false, nodeId);
                loc.addCommand(snc);
                snc.doCommand();
                System.out.println("Pickup added");
                TextInputDialog dialog = new TextInputDialog("Pickup Duration");
                dialog.setTitle("Pickup duration");
                dialog.setHeaderText("Enter pickup duration (secondes) : ");
                dialog.setContentText("Duration :");
                Optional<String> pickupDurationString = dialog.showAndWait();
                while (!pickupDurationString.isPresent() || pickupDurationString.isEmpty() || !isParsable(pickupDurationString.get())) {
                    dialog.setTitle("Pickup duration");
                    dialog.setHeaderText("INVALID ANSWER - Enter pickup duration (secondes) : ");
                    dialog.setContentText("Duration :");
                    pickupDurationString = dialog.showAndWait();
                }
                pickupDuration = Integer.parseInt(pickupDurationString.get());
                this.mpc.schowInfoAlert("Select Delivery point", "Pickup added ! Now select a delivery point on the map please.");
            } else if (deliveryId == null) {
                SelectNodeCommand snc = new SelectNodeCommand(this, true, nodeId);
                loc.addCommand(snc);
                snc.doCommand();
                System.out.println("Delivery added");
                TextInputDialog dialog = new TextInputDialog("Delivery Duration");
                dialog.setTitle("Delivery duration");
                dialog.setHeaderText("Enter delivery duration (secondes) : ");
                dialog.setContentText("Duration :");
                Optional<String> deliveryDurationString = dialog.showAndWait();
                while (!deliveryDurationString.isPresent() || deliveryDurationString.isEmpty() || !isParsable(deliveryDurationString.get())) {
                    dialog.setTitle("Delivery duration");
                    dialog.setHeaderText("INVALID ANSWER - Enter delivery duration (secondes) : ");
                    dialog.setContentText("Duration :");
                    deliveryDurationString = dialog.showAndWait();
                }
                deliveryDuration = Integer.parseInt(deliveryDurationString.get());
                this.addRequest(); //On lance la requete
            }
        } else {
            System.out.println("Déjà 2 points sélectionné");
        }
    }

    @Override
    public void undo() {
        loc.undo();
    }

    @Override
    public void redo() {
        loc.redo();
    }

    /**
     * Set the pickup node id
     *
     * @param id pickup node id
     */
    public void setPickupId(String id) {
        pickupId = id;
    }

    /**
     * Set the delivery node id
     *
     * @param id delivery node id
     */
    public void setDeliveryId(String id) {
        deliveryId = id;
    }

    /**
     * Check if a string is parsable in int
     *
     * @param input the string to check
     * @return whether the string is parsable or not
     */
    private boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

}
