/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deliverif.app.controller.Command;

/**
 * Interface to a command which can be done and undone
 * @author zakaria
 */
public interface Command {

    /**
     * Execute the command
     */
    void doCommand();

    /**
     * Reset the state as it was before the command was executed
     */
    void undoCommand();
}
