/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Observer;

import deliverif.app.controller.Observer.Observable;

/**
 *
 * @author zakaria
 */
public interface Observer {
    public void update (Observable observed, Object arg);
}
