/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Observer;

/**
 *
 * @author zakaria
 */
public interface Observer {

    /**
     * Update method which is called when an observed object has changed
     *
     * @param observed the observed object
     * @param arg some data about the update
     */
    public void update(Observable observed, Object arg);
}
