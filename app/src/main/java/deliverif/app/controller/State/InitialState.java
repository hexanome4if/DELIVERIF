/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.State;

import deliverif.app.controller.MenuPageController;
import deliverif.app.controller.State.MapLoadedState;
import deliverif.app.controller.State.State;
import java.io.IOException;

/**
 *
 * @author zakaria
 */
public class InitialState extends State {

    public InitialState(MenuPageController mpc) {
        super(mpc);
    }

    
    @Override
    public void loadMap() throws IOException {
        mpc.loadMap();
        mpc.setCurrentState(new MapLoadedState(mpc));
    }
    
}
