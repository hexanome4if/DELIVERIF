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
public class TourComputedState extends State {
    public TourComputedState(MenuPageController mpc) {
        super(mpc);
    }
    
    @Override
    public void loadMap() throws IOException {
        mpc.loadMap();
        mpc.setCurrentState(new MapLoadedState(mpc));
    }

    @Override
    public void loadRequest() throws IOException {
        mpc.loadRequest();
        mpc.setCurrentState(new RequestLoadedState(mpc));
    }

    @Override
    public void computeTour() {
        mpc.computeTour();
        mpc.setCurrentState(new TourComputedState(mpc));
    }

    @Override
    public void removeRequest() {
        mpc.removeRequest();
    }

    @Override
    public void startAddRequest() {
        // todo
        mpc.setCurrentState(new AddingRequestState(mpc));
    }


    @Override
    public void redo() {
        mpc.redo();
    }

    @Override
    public void undo() {
        mpc.undo();
    }
    
    
    
}
