/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.State;

import deliverif.app.controller.Command.ListOfCommands;
import deliverif.app.controller.MenuPageController;
import deliverif.app.controller.Command.SelectNodeCommand;
import deliverif.app.controller.State.TourComputedState;
import deliverif.app.controller.State.State;

/**
 *
 * @author zakaria
 */
public class AddingRequestState extends State {
    String pickupId;
    String deliveryId;
    ListOfCommands loc;
    
    public AddingRequestState(MenuPageController mpc) {
        super(mpc);
        loc = new ListOfCommands();
    }
    @Override
    public void addRequest() {
        if (pickupId == null || deliveryId == null) return;
        mpc.addRequest(pickupId,deliveryId);
        mpc.setCurrentState(new TourComputedState(mpc));
    }

    @Override
    public void selectNode(String nodeId) {
        if (pickupId == null || deliveryId == null) {
            //super.selectNode(nodeId);
            if (pickupId == null){
                SelectNodeCommand snc = new SelectNodeCommand(this,false,nodeId);
                loc.addCommand(snc);
                snc.doCommand();
            }
            else if (deliveryId == null){
                SelectNodeCommand snc = new SelectNodeCommand(this,true,nodeId);
                loc.addCommand(snc);
                snc.doCommand();
            }
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
    
      
    public void setPickupId(String id){
        pickupId = id;
    }
    
    public void setDeliveryId(String id){
        deliveryId = id;
    }
    
    
}
