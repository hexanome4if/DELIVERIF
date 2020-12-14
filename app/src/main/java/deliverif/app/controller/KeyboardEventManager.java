/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller;

import deliverif.app.view.App;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles keyboard events on the view
 * @author zakaria
 */
public class KeyboardEventManager implements EventHandler<KeyEvent> {

    /**
     * Current view controller
     */
    private final MenuPageController mpc;

    public KeyboardEventManager() {
        this.mpc = MenuPageController.getInstance();
        Scene scene = App.getScene();
        scene.setOnKeyPressed(this);
    }
    
    /**
     * Overrides handle method of EventHandler interface
     * @param t the event to handle
     */
    @Override
    public void handle(KeyEvent t) {
        KeyCode kc = t.getCode();
        if (kc == KeyCode.DELETE) {
            MenuPageController.stopPathThread();
            mpc.getCurrentState().removeRequest();
        }

        if (kc == KeyCode.Z && t.isControlDown()) {
            MenuPageController.stopPathThread();
            mpc.getCurrentState().undo();
        }

        if (kc == KeyCode.Y && t.isControlDown()) {
            MenuPageController.stopPathThread();
            mpc.getCurrentState().redo();
        }
        
        if (kc == KeyCode.N && t.isControlDown()) {
            MenuPageController.stopPathThread();
            mpc.getCurrentState().startAddRequest();
        }
        
    }
}
