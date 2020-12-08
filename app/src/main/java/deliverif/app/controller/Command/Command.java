/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Command;

/**
 *
 * @author zakaria
 */
public interface Command {
    public void doCommand();
    public void undoCommand();
}
