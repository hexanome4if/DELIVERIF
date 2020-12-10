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
public class RequestLoadedState extends State {

    /**
     * Start a request loaded state when a request has been selected but no tour
     * has been computed yet
     *
     * @param mpc the current view controller
     */
    public RequestLoadedState(MenuPageController mpc) {
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

}
