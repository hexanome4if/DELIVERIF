/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.model.request;

/**
 *
 * @author zakaria
 */
public interface Observer {
    public void update (Observable observed, Object arg);
}
