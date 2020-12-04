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
 *
 * @author zakaria
 */
public class KeyboardEventManager implements EventHandler<KeyEvent> {

    private final Scene scene;
    private final MenuPageController mpc;

    public KeyboardEventManager() {
        this.mpc = MenuPageController.getInstance();
        this.scene = App.getScene();
        this.scene.setOnKeyPressed(this);
    }

    @Override
    public void handle(KeyEvent t) {
        KeyCode kc = t.getCode();
        if (kc == KeyCode.DELETE) {
            mpc.removeRequest();
        }
    }
}
