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

    /**
     * Execute the command
     */
    public void doCommand();

    /**
     * Reset the state as it was before the command was executed
     */
    public void undoCommand();
}
