/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.State;

import deliverif.app.controller.Command.ListOfCommands;
import deliverif.app.controller.Command.SelectNodeCommand;
import deliverif.app.controller.MenuPageController;

/**
 *
 * @author zakaria
 */
public class AddingRequestState extends State {

    private String pickupId;
    private String deliveryId;
    private ListOfCommands loc;

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
        mpc.addRequest(pickupId, deliveryId);
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
                this.mpc.schowInfoAlert("Select Delivery point", "Pickup added ! Now select a delivery point on the map please.");
            } else if (deliveryId == null) {
                SelectNodeCommand snc = new SelectNodeCommand(this, true, nodeId);
                loc.addCommand(snc);
                snc.doCommand();
                System.out.println("Delivery added");
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

}
