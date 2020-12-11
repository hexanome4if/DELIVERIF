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
public class InitialState extends State {

    /**
     * Start the initial state with no map selected
     *
     * @param mpc the current view controller
     */
    public InitialState(MenuPageController mpc) {
        super(mpc);
    }

    @Override
    public void loadMap() throws IOException {
        mpc.loadMap();
        mpc.setCurrentState(new MapLoadedState(mpc));
    }

}
