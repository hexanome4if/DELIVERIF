/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import java.io.IOException;

/**
 *
 * @author zakaria
 */
public abstract class State {
    public MenuPageController mpc;
    public State(MenuPageController mpc) {
        this.mpc = mpc;
    }
    public  void loadMap() throws IOException {};
    public  void loadRequest() throws IOException {};
    public  void computeTour() {};
    public  void undo() {};
    public  void redo() {};
    public  void addRequest() {};
    public void removeRequest() {};
    public void startAddRequest() {};
    public void selectNode(String nodeId) {
        mpc.setSelectedSprite(nodeId);
    }
}
