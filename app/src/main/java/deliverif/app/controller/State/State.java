/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.State;

import deliverif.app.controller.MenuPageController;
import java.io.IOException;

/**
 *
 * @author zakaria
 */
public abstract class State {

    protected MenuPageController mpc;

    /**
     * Create a new state
     *
     * @param mpc the main view controller
     */
    public State(MenuPageController mpc) {
        this.mpc = mpc;
    }

    /**
     * Try to load a map if possible
     *
     * @throws IOException when an error occured with the selected file
     */
    public void loadMap() throws IOException {
    }

    /**
     * Try to load a request if possible
     *
     * @throws IOException when an error occured with the selected file
     */
    public void loadRequest() throws IOException {
    }

    /**
     * Try to compute a tour if possible
     */
    public void computeTour() {
    }

    /**
     * Try to undo the last command if possible
     */
    public void undo() {
    }

    /**
     * Try to redo the last command if possible
     */
    public void redo() {
    }

    /**
     * Try to add a request to the current tour if possible
     */
    public void addRequest() {
    }

    /**
     * Try to remove a request from the current tour if possible
     */
    public void removeRequest() {
    }
    
    /**
     * Try to swap a request from the current tour if possible
     */
    public void swapRequest() {
    }

    /**
     * Start adding a request to the current tour if possible
     */
    public void startAddRequest() {
    }
    
    /**
     * Start swapping a request
     */
    public void startSwapRequest() {
    }

    /**
     * Select a node
     *
     * @param nodeId the node id to select
     */
    public void selectNode(String nodeId) {
        mpc.setSelectedSprite(nodeId);
    }
    
    /**
     * Select a sprite
     * @param spriteId 
     */
    public void selectSprite(String spriteId) {}
}
