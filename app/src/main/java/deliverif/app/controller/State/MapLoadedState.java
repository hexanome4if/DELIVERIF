/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.State;

import deliverif.app.controller.MenuPageController;
import java.io.IOException;

/**
 * State when only a map is loaded
 * @author zakaria
 */
public class MapLoadedState extends State {

    /**
     * Start a map loaded state when only a map is loaded (no request)
     *
     * @param mpc the current view controller
     */
    public MapLoadedState(MenuPageController mpc) {
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

}
