/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Command;

import deliverif.app.controller.State.AddingRequestState;

/**
 *
 * @author zakaria
 */
public class SelectNodeCommand implements Command {

    private AddingRequestState ars;
    private boolean isDelivery;
    private String id;

    /**
     * Create a select node command
     *
     * @param ars the current state
     * @param isDelivery is the selected node a delivery or a pickup
     * @param id the selected node id
     */
    public SelectNodeCommand(AddingRequestState ars, boolean isDelivery, String id) {
        this.ars = ars;
        this.isDelivery = isDelivery;
        this.id = id;
    }

    @Override
    public void doCommand() {
        if (isDelivery) {
            ars.setDeliveryId(id);
        } else {
            ars.setPickupId(id);
        }
    }

    @Override
    public void undoCommand() {
        if (isDelivery) {
            ars.setDeliveryId(null);
        } else {
            ars.setPickupId(null);
        }
    }

}
