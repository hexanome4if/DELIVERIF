/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.State;

import deliverif.app.controller.Command.ListOfCommands;
import deliverif.app.controller.Command.SelectNodeCommand;
import deliverif.app.controller.Command.SelectSpriteCommand;
import deliverif.app.controller.MenuPageController;

/**
 * State in which the user is swapping two point of the current tour
 * @author faouz
 */
public class SwapRequestState extends State {
    /**
     * List of commands to perform undo and redo
     */
    private final ListOfCommands loc;
    /**
     * First request intersection id
     */
    private String firstRequestId;
    /**
     * Second request intersection id
     */
    private String secondRequestId;


    /**
     * Start a swap request state
     * @param mpc the view controller
     */
    public SwapRequestState(MenuPageController mpc) {
        super(mpc);
        loc = new ListOfCommands();
    }
    
    
    @Override
    public void swapRequest() {
         if (firstRequestId == null || secondRequestId == null) {
            System.out.println("Il manque des requetes dans la selection");
            return;
        }
        mpc.swapRequest(firstRequestId, secondRequestId);
        mpc.setCurrentState(new TourComputedState(mpc));
        System.out.println("Requete swapée");
    }
    
    @Override
    public void selectSprite(String spriteId) {
        
        if (firstRequestId == null || secondRequestId == null) {
            //super.selectNode(nodeId);
            if (firstRequestId == null) {
                SelectSpriteCommand ssc = new SelectSpriteCommand(this, true, spriteId);
                loc.addCommand(ssc);
                ssc.doCommand();
                System.out.println("First added");
                this.mpc.schowInfoAlert("Select a first point to swap", "First point added ! Now select a second point on the tour please.");
            } else if (secondRequestId == null) {
                SelectSpriteCommand ssc = new SelectSpriteCommand(this, false, spriteId);
                loc.addCommand(ssc);
                ssc.doCommand();
                System.out.println("Second added");
                this.swapRequest(); //On lance la requete
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
     * Set the first request node id
     *
     * @param id first request node id
     */
    public void setFirstId(String id) {
        firstRequestId = id;
    }

    /**
     * Set the second request node id
     *
     * @param id second request node id
     */
    public void setSecondId(String id) {
        secondRequestId = id;
    }

    
}
